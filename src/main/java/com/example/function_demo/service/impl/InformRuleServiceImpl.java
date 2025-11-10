package com.example.function_demo.service.impl;


import com.example.function_demo.Excel.FormatTemplate.ExcelTemplateEnum;
import com.example.function_demo.Excel.Processor.ExcelProcessor;
import com.example.function_demo.Excel.data.dto.InformRuleImportDTO;
import com.example.function_demo.Excel.data.module.InformRuleImportModule;
import com.example.function_demo.beanFactory.ExcelProcessorFactory;
import com.example.function_demo.common.conatants.SystemConstants;
import com.example.function_demo.dto.ParamSpecKeyRequestDTO;
import com.example.function_demo.service.InformRuleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class InformRuleServiceImpl implements InformRuleService {



   @Autowired
    private ExcelProcessorFactory excelProcessorFactory;

    private static final Class excelDataTemplateClass = InformRuleImportDTO.class;


    @Override
    public List<InformRuleImportDTO> checkInformSpecData(MultipartFile file, String userName) {

        ExcelProcessor<InformRuleImportDTO, InformRuleImportModule> excelProcessor = excelProcessorFactory.getExcelProcessor(ExcelTemplateEnum.INFORM_SPEC_RULE_TEMPLATE);
        Boolean checkSuccessFlag = excelProcessor.checkExcelHeadRow(file);
        if (!checkSuccessFlag) {
            throw new RuntimeException(" The template formats are inconsistent ");
        }
        List<InformRuleImportDTO> informRuleImportDTOS = excelProcessor.checkExcelData(file);
        return informRuleImportDTOS;
    }


    @Override
    public List<InformRuleImportDTO> checkInformSpecDataByKey(List<InformRuleImportDTO > fileData, String userName) {
        ExcelProcessor<InformRuleImportDTO, InformRuleImportModule> excelProcessor = excelProcessorFactory.getExcelProcessor(ExcelTemplateEnum.INFORM_SPEC_RULE_TEMPLATE);
        for (InformRuleImportDTO data : fileData) {
            excelProcessor.checkExcelData(data);
        }
        return fileData;
    }

    @Override
    public void importInformRuleFileData(MultipartFile file, String userName) {

        ExcelProcessor<InformRuleImportDTO, InformRuleImportModule> excelProcessor = excelProcessorFactory.getExcelProcessor(ExcelTemplateEnum.INFORM_SPEC_RULE_TEMPLATE);
        Boolean checkSuccessFlag = excelProcessor.checkExcelHeadRow(file);
        if (!checkSuccessFlag) {
            throw new RuntimeException( " The template formats are inconsistent ");
        }
        List<InformRuleImportDTO> informRuleImportDTOS = excelProcessor.checkExcelData(file);
        List<InformRuleImportDTO> checkSuccessData = informRuleImportDTOS.stream().filter(item -> item.getCheckSuccessFlag()).collect(Collectors.toList());
        List<InformRuleImportDTO> checkFailData = informRuleImportDTOS.stream().filter(item -> !item.getCheckSuccessFlag()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(checkFailData)){
            throw new RuntimeException( " The file contains wrong data, please check again ! ");
        }
        List<InformRuleImportModule> informRuleImportModules = excelProcessor.parseToRealTypes(checkSuccessData);
        //saveInformSpecRule(informRuleImportModules,userName);


    }

    @Override
    public List<Object> queryInformSpecDTO(ParamSpecKeyRequestDTO paramSpecKeyRequestDTO) {

        LinkedList<Object> result = new LinkedList<>();

        paramSpecKeyRequestDTO.setPageSize(100000);
        paramSpecKeyRequestDTO.setPageNo(1);
//        IPage<ParamSpecKeyDTO> paramSpecKeyDTOIPage = this.queryInformSpecKeyByCondition(paramSpecKeyRequestDTO);
//        List<BusinessRuleConf> reportRuleList = businessRuleConfDAO.queryAllReportRule(BusinessRuleType.REPORT);
//        List<ParamSpecKeyDTO> records = paramSpecKeyDTOIPage.getRecords();
//        List<ParamSpecKeyDTO> records = new ArrayList<>();
//        for (ParamSpecKeyDTO record : records) {
//             InformRuleImportDTO dto = spectDetailToInformRuleImportDTO(record,reportRuleList);
//             result.add(dto);
//        }
        return result;
    }

    @Override
    public void deleteInformSpecByCondition(ParamSpecKeyRequestDTO paramSpecKeyRequestDTO) {

        List<String> tech = paramSpecKeyRequestDTO.getTech();
        List<String> loop = paramSpecKeyRequestDTO.getLoop();
        List<String> layer = paramSpecKeyRequestDTO.getLayer();
        List<String> productId = paramSpecKeyRequestDTO.getProductId();
        List<String> defectClass = new ArrayList<>();
        defectClass.add("CLASS_NUMBER");

        List<Integer> defectCodes = new ArrayList<>();
        List<String> defectTypes = paramSpecKeyRequestDTO.getDefectType();

        if (Objects.isNull(defectTypes)) {
            defectTypes = new ArrayList<>();
        }
        for (String defectType : defectTypes) {
            Integer defectCode = null;
            try {
                if (StringUtils.equals(defectType, SystemConstants.ALL)) {
                    defectCode = Integer.MIN_VALUE;
                    defectCodes.add(defectCode);
                } else {
                    String[] split = defectType.split("-");
                    int i = Integer.parseInt(split[0]);
                    defectCode = i;
                    defectCodes.add(defectCode);
                }
            } catch (Exception e) {
                throw new RuntimeException("defect type value is wrong,please check!");
            }

        }
//        List<String> informParamSpecKeys = informParamSpecKeyDAO.selectParamSpecKeyByCondition(tech, loop, layer, productId, defectClass, defectCodes);
//        informParamSpecDetailDAO.deleteByParamKeys(informParamSpecKeys);
//        defectSpecDAO.deleteByParamKeys(informParamSpecKeys);
//        informParamSpecKeyDAO.deleteByParamKeys(informParamSpecKeys);

    }

    @Override
    public void importInformRuleData(List<InformRuleImportDTO> fileData, String userName) {

        ExcelProcessor<InformRuleImportDTO, InformRuleImportModule> excelProcessor = excelProcessorFactory.getExcelProcessor(ExcelTemplateEnum.INFORM_SPEC_RULE_TEMPLATE);

        for (InformRuleImportDTO fileDatum : fileData) {
            excelProcessor.checkExcelData(fileDatum);
        }

        List<InformRuleImportDTO> checkSuccessData = fileData.stream().filter(item -> item.getCheckSuccessFlag()).collect(Collectors.toList());
        List<InformRuleImportDTO> checkFailData = fileData.stream().filter(item -> !item.getCheckSuccessFlag()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(checkFailData)){
            throw new RuntimeException(" The file contains wrong data, please check again ! ");
        }
        List<InformRuleImportModule> informRuleImportModules = excelProcessor.parseToRealTypes(checkSuccessData);
        //saveInformSpecRule(informRuleImportModules,userName);
    }




    private static Boolean safeEnableStatus(Boolean flag){
        if (flag == null) return true;
        return flag;
    };
    private static Boolean safeHLModuleStatus(Boolean flag){
        if (flag == null) return false;
        return flag;
    };


}
