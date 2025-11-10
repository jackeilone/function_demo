package com.example.function_demo.Excel.Processor.impl;

import ai.futurefab.wise.Excel.ExcelUtils;
import ai.futurefab.wise.Excel.FormatTemplate.ExcelTemplateEnum;
import ai.futurefab.wise.Excel.Processor.ExcelProcessor;
import ai.futurefab.wise.Excel.data.dto.DLYConfigImportDTO;
import ai.futurefab.wise.constant.WiseConstants;
import ai.futurefab.wise.dao.DefectCodeNameDAO;
import ai.futurefab.wise.dao.ProductLayerInfoDAO;
import ai.futurefab.wise.entity.report.DefectCodeName;
import ai.futurefab.wise.enums.DefectClassTypeEnum;
import ai.futurefab.ypa.entity.config.DefectLimitYieldConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Defect Limit Yield Config Excel Processor
 * 处理 DLY Config 的 Excel 导入
 *
 * @date 2025-10-13
 */
@Slf4j
@Service
public class DefectLimitYieldExcelProcessor implements ExcelProcessor<DLYConfigImportDTO, DefectLimitYieldConfig> {

    private static final ExcelTemplateEnum excelTemplate = ExcelTemplateEnum.DEFECT_LIMIT_YIELD_CONFIG_TEMPLATE;

    @Resource
    private ProductLayerInfoDAO productLayerInfoDAO;

    @Resource
    private DefectCodeNameDAO defectCodeNameDAO;

    @Override
    public ExcelTemplateEnum getTemplate() {
        return excelTemplate;
    }

    @Override
    public List<DLYConfigImportDTO> checkExcelData(MultipartFile uploadExcelFile) {
        List<DLYConfigImportDTO> dlyConfigImportDTOS = this.fileToDataObject(uploadExcelFile);
        return batchCheckExcelData(dlyConfigImportDTOS);
    }

    @Override
    public Boolean checkExcelHeadRow(MultipartFile uploadExcelFile) {
        try {
            List<Map<Integer, String>> uploadExcelHeadRow = ExcelUtils.getExcelHeadRow(uploadExcelFile, excelTemplate);
            List<Map<Integer, String>> excelTemplateHeadRow = ExcelUtils.getExcelTemplateHeadRow(excelTemplate);

            long count1 = uploadExcelHeadRow.get(0).keySet().size();
            long count2 = excelTemplateHeadRow.get(0).keySet().size();

            if (count1 != count2) {
                return Boolean.FALSE;
            }

            Map<Integer, String> uploadExcelHead = uploadExcelHeadRow.get(0);
            Map<Integer, String> excelTemplateHead = excelTemplateHeadRow.get(0);

            for (int i = 0; i < count1; i++) {
                boolean equals = excelTemplateHead.get(i).equals(uploadExcelHead.get(i));
                if (!equals) {
                    return Boolean.FALSE;
                }
            }

        } catch (IOException e) {
            log.error("load excel template fail : {} ,error msg :{}", excelTemplate.getTemplateName(), e.toString());
        }
        return true;
    }

    @Override
    public List<DLYConfigImportDTO> fileToDataObject(MultipartFile uploadExcelFile) {
        return ExcelUtils.fileToDataObject(uploadExcelFile, DLYConfigImportDTO.class, excelTemplate.getDataStartRowNo());
    }

    @Override
    public DLYConfigImportDTO checkExcelData(DLYConfigImportDTO excelInputDTO) {
        return batchCheckExcelData(List.of(excelInputDTO)).get(0);
    }


    @Override
    public List<DLYConfigImportDTO> batchCheckExcelData(List<DLYConfigImportDTO> list) {

        if (list == null) {
            return Collections.emptyList();
        }

        // 1. 查询系统中所有的可选值
        Map<String, Map<String, String>> systemValuesMap = getSystemValidValuesMap();

        // 2. 对每条数据进行校验
        for (DLYConfigImportDTO dto : list) {
            boolean checkSuccess = true;
            StringBuilder checkInfo = new StringBuilder();

            // 校验并规范化 Tech
            String normalizedTech = validateAndNormalize(dto.getTech(), systemValuesMap.get(WiseConstants.TECH));
            if (normalizedTech == null) {
                checkInfo.append("Tech is not exist in system or not all;");
                checkSuccess = false;
            } else {
                dto.setTech(normalizedTech);
            }

            // 校验并规范化 Product
            String normalizedProduct = validateAndNormalize(dto.getProductId(), systemValuesMap.get(WiseConstants.PRODUCT));
            if (normalizedProduct == null) {
                checkInfo.append("Product is not exist in system or not all;");
                checkSuccess = false;
            } else {
                dto.setProductId(normalizedProduct);
            }

            // 校验并规范化 Loop
            String normalizedLoop = validateAndNormalize(dto.getLoop(), systemValuesMap.get(WiseConstants.LOOP));
            if (normalizedLoop == null) {
                checkInfo.append("Loop is not exist in system or not all;");
                checkSuccess = false;
            } else {
                dto.setLoop(normalizedLoop);
            }

            // 校验并规范化 LayerId
            String normalizedLayer = validateAndNormalize(dto.getLayerId(), systemValuesMap.get(WiseConstants.LAYER));
            if (normalizedLayer == null) {
                checkInfo.append("Layer ID is not exist in system or not all;");
                checkSuccess = false;
            } else {
                dto.setLayerId(normalizedLayer);
            }

            // 校验并规范化 DefectType
            String defectType = dto.getDefectType();
            String normalizedDefectType = validateAndNormalize(defectType, systemValuesMap.get(WiseConstants.DEFECT_TYPE));

            if (normalizedDefectType == null) {
                // 未匹配到 defect type, 尝试匹配 defect code
                normalizedDefectType = validateAndNormalize(defectType, systemValuesMap.get(WiseConstants.DEFECT_CODE));
            }

            if (normalizedDefectType == null) {
                // 未匹配到 defect code, 尝试匹配 defect name
                normalizedDefectType = validateAndNormalize(defectType, systemValuesMap.get(WiseConstants.DEFECT_NAME));
            }

            if (normalizedDefectType == null) {
                checkInfo.append("Defect Type is not exist in system or not all;");
                checkSuccess = false;
            } else {
                dto.setDefectType(normalizedDefectType);
            }

            // 校验 killRatio
            String killRatio = dto.getKillRatio();
            if (StringUtils.isEmpty(killRatio)) {
                checkInfo.append("Kill Ratio is empty;");
            } else {
                try {
                    double ratio = Double.parseDouble(killRatio);
                    if (ratio < 0 || ratio > 100) {
                        checkInfo.append(killRatio).append(" is not within the range of 0 to 100;");
                        checkSuccess = false;
                    }
                } catch (Exception e) {
                    checkInfo.append(killRatio).append(" is not number;");
                    checkSuccess = false;
                }
            }

            // TODO 校验 potentialFailBin

            // 校验 autoUpdateKillRatio
            String autoUpdateKillRatio = dto.getAutoUpdateKillRatio();
            if (!StringUtils.isEmpty(autoUpdateKillRatio)) {
                if (stringFlagToBoolean(autoUpdateKillRatio) == null) {
                    checkInfo.append("Auto Update Kill Ratio value is wrong, should be yes/no;");
                    checkSuccess = false;
                }
            }

            dto.setCheckSuccessFlag(checkSuccess);
            if (checkSuccess) {
                dto.setCheckResult("Success");
            } else {
                dto.setCheckResult(checkInfo.toString());
            }
        }

        return list;
    }

    /**
     * 获取系统中所有可选值的 Map
     *
     * @return 系统可选值 Map，外层 key 为字段名，内层 Map 为 小写值->原始值 的映射
     */
    private Map<String, Map<String, String>> getSystemValidValuesMap() {
        Map<String, Map<String, String>> systemValuesMap = new HashMap<>();

        // 查询 Tech
        List<String> techList = productLayerInfoDAO.selectDistinctColumnByCondition(null, null, null, null, null, null, null, "tech");
        Map<String, String> techMap = buildNormalizationMap(techList);
        systemValuesMap.put(WiseConstants.TECH, techMap);

        // 查询 Product
        List<String> productList = productLayerInfoDAO.selectDistinctColumnByCondition(null, null, null, null, null, null, null, "productId");
        Map<String, String> productMap = buildNormalizationMap(productList);
        systemValuesMap.put(WiseConstants.PRODUCT, productMap);

        // 查询 Loop
        List<String> loopList = productLayerInfoDAO.selectDistinctColumnByCondition(null, null, null, null, null, null, null, "loop");
        Map<String, String> loopMap = buildNormalizationMap(loopList);
        systemValuesMap.put(WiseConstants.LOOP, loopMap);

        // 查询 Layer
        List<String> layerList = productLayerInfoDAO.selectDistinctColumnByCondition(null, null, null, null, null, null, null, "layer");
        Map<String, String> layerMap = buildNormalizationMap(layerList);
        systemValuesMap.put(WiseConstants.LAYER, layerMap);

        // 查询 DefectType（CLASS_NUMBER 类型）
        List<DefectCodeName> defectCodeNames = defectCodeNameDAO.selectAllDefects();
        Map<String, String> defectTypeMap = new HashMap<>();
        Map<String, String> defectCodeMap = new HashMap<>();
        Map<String, String> defectNameMap = new HashMap<>();
        if (defectCodeNames != null) {
            List<DefectCodeName> classNumberList = defectCodeNames.stream()
                    .filter(item -> DefectClassTypeEnum.CLASS_NUMBER.equals(item.getClassType()))
                    .toList();
            for (DefectCodeName defect : classNumberList) {
                if (defect.getDefectCode() == null) {
                    continue;
                }
                String code = String.valueOf(defect.getDefectCode());
                String name = defect.getDefectName() == null ? "" : defect.getDefectName();
                String originalValue = code + "-" + name;

                // 如果导入时用户输入的是 defect code, defect name, defect type 其中之一, 都可以匹配成功
                defectTypeMap.put(originalValue, originalValue);
                defectCodeMap.put(code, originalValue);
                defectNameMap.put(name, originalValue);
            }
        }
        defectTypeMap.put(WiseConstants.ALL, WiseConstants.ALL);
        defectCodeMap.put(WiseConstants.ALL, WiseConstants.ALL);
        defectNameMap.put(WiseConstants.ALL, WiseConstants.ALL);
        systemValuesMap.put(WiseConstants.DEFECT_TYPE, defectTypeMap);
        systemValuesMap.put(WiseConstants.DEFECT_CODE, defectCodeMap);
        systemValuesMap.put(WiseConstants.DEFECT_NAME, defectNameMap);

        return systemValuesMap;
    }

    /**
     * 构建规范化映射 Map
     *
     * @param valueList 原始值列表
     * @return 规范化 Map
     */
    private Map<String, String> buildNormalizationMap(List<String> valueList) {
        Map<String, String> map = new HashMap<>();
        if (valueList != null) {
            for (String value : valueList) {
                if (StringUtils.isNotEmpty(value)) {
                    map.put(value, value);
                    //map.put(value.toLowerCase(), value);
                }
            }
        }
        map.put(WiseConstants.ALL, WiseConstants.ALL);
        return map;
    }

    /**
     * 验证并规范化值
     *
     * @param inputValue       用户输入的值
     * @param normalizationMap 规范化映射 Map
     * @return 系统标准值，如果验证失败返回 null
     */
    private String validateAndNormalize(String inputValue, Map<String, String> normalizationMap) {
        if (StringUtils.isEmpty(inputValue)) {
            return null;
        }
        if (normalizationMap == null) {
            return null;
        }
        // 按照原值匹配
        String value = normalizationMap.get(inputValue);
        if (value == null) {
            // 使用小写 key 查找系统标准值
            return normalizationMap.get(inputValue.toLowerCase());
        }

        return value;
    }

    @Override
    public List<DefectLimitYieldConfig> parseToRealTypes(List<DLYConfigImportDTO> dataObject) {
        ArrayList<DefectLimitYieldConfig> result = new ArrayList<>();
        for (DLYConfigImportDTO dto : dataObject) {
            DefectLimitYieldConfig config = new DefectLimitYieldConfig();

            config.setTech(dto.getTech());
            config.setProductId(dto.getProductId());
            config.setLoop(dto.getLoop());
            config.setLayerId(dto.getLayerId());
            config.setDefectType(dto.getDefectType());

            config.setDefectCodeByType();

            // 转换 killRatio
            config.setKillRatio(Double.parseDouble(dto.getKillRatio()));

            // 转换 potentialFailBin (字符串转 List)
            if (!StringUtils.isEmpty(dto.getPotentialFailBin())) {
                String[] bins = dto.getPotentialFailBin().split(",");
                List<String> binList = Arrays.stream(bins)
                        .map(String::trim)
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toList());
                config.setPotentialFailBin(binList);
            }

            // 转换 autoUpdateKillRatio
            if (!StringUtils.isEmpty(dto.getAutoUpdateKillRatio())) {
                config.setAutoUpdateKillRatio(this.stringFlagToBoolean(dto.getAutoUpdateKillRatio()));
            } else {
                config.setAutoUpdateKillRatio(true);
            }

            config.setDescription(dto.getDescription());

            result.add(config);
        }
        return result;
    }

    /**
     * 将字符串标志转换为布尔值
     * 支持 yes/no
     */
    private Boolean stringFlagToBoolean(String flag) {
        return ExcelProcessor.stringFlagToBoolean(flag);
    }
}

