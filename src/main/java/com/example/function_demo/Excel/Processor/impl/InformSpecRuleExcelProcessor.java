package com.example.function_demo.Excel.Processor.impl;

import ai.futurefab.wise.Excel.ExcelUtils;
import ai.futurefab.wise.Excel.FormatTemplate.ExcelTemplateEnum;
import ai.futurefab.wise.Excel.Processor.ExcelProcessor;
import ai.futurefab.wise.Excel.data.dto.InformRuleImportDTO;
import ai.futurefab.wise.Excel.data.module.InformRuleImportModule;
import ai.futurefab.wise.constant.WiseConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@Slf4j
@Service
public class InformSpecRuleExcelProcessor implements ExcelProcessor<InformRuleImportDTO, InformRuleImportModule> {

    private static final ExcelTemplateEnum excelTemplate = ExcelTemplateEnum.INFORM_SPEC_RULE_TEMPLATE;

    private static final Class excelDataTemplateClass = InformRuleImportDTO.class;

    @Override
    public ExcelTemplateEnum getTemplate() {
        return this.excelTemplate;
    }

    @Override
    public List<InformRuleImportDTO> checkExcelData(MultipartFile uploadExcelFile) {

        List<InformRuleImportDTO> informRuleImportDTOS = this.fileToDataObject(uploadExcelFile);
        for (InformRuleImportDTO informRuleImportDTO : informRuleImportDTOS) {
            this.checkExcelData(informRuleImportDTO);
        }
        return informRuleImportDTOS;
    }



    @Override
    public Boolean checkExcelHeadRow(MultipartFile uploadExcelFile) {

        try {
            List<Map<Integer, String>> uploadExcelHeadRow = ExcelUtils.getExcelHeadRow(uploadExcelFile, excelTemplate);
            List<Map<Integer, String>> excelTemplateHeadRow = ExcelUtils.getExcelTemplateHeadRow(excelTemplate);

            long count1 = uploadExcelHeadRow.get(0).keySet().stream().count();
            long count2 = excelTemplateHeadRow.get(0).keySet().stream().count();

            if (count1 != count2){
                return false;
            }

            Map<Integer, String> uploadExcelHead = uploadExcelHeadRow.get(0);
            Map<Integer, String> excelTemplateHead = excelTemplateHeadRow.get(0);

            for (int i = 0; i < count1; i++) {
                boolean equals = excelTemplateHead.get(i).equals(uploadExcelHead.get(i));
                if (!equals){
                    return false;
                }
            }

        }catch (IOException e){
            log.error("load excel template fail : {} ,error msg :{}",excelTemplate.getTemplateName(),e.toString());
        }
        return true;

    }

    @Override
    public List<InformRuleImportDTO> fileToDataObject(MultipartFile uploadExcelFile) {
        return ExcelUtils.fileToDataObject(uploadExcelFile, excelDataTemplateClass,excelTemplate.getDataStartRowNo());
    }

    @Override
    public InformRuleImportDTO checkExcelData(InformRuleImportDTO excelInputDTO) {

        Boolean checkSuccess = true;
        StringBuilder checkInfo = new StringBuilder();
        // 校验 tech
        String tech = excelInputDTO.getTech();
        if (StringUtils.isEmpty(tech)){
            checkInfo.append(" tech is empty , must has value.");
            checkSuccess = false;
        }
        // 校验 productId
        String productId = excelInputDTO.getProductId();
        if (StringUtils.isEmpty(productId)){
            checkInfo.append(" productId is empty , must has value.");
            checkSuccess = false;
        }
        // 校验 loop
        String loop = excelInputDTO.getLoop();
        if (StringUtils.isEmpty(loop)){
            checkInfo.append(" loop is empty , must has value.");
            checkSuccess = false;
        }
        // 校验 layer
        String layer = excelInputDTO.getLayer();
        if (StringUtils.isEmpty(layer)){
            checkInfo.append(" layer is empty , must has value.");
            checkSuccess = false;
        }
        // 校验 defect Type
        String classNumber = excelInputDTO.getDefectType();
        if (StringUtils.isEmpty(classNumber)){
            checkInfo.append(" defectType is empty , must has value.");
            checkSuccess = false;
        }
        if (!StringUtils.isEmpty(classNumber)) {
            String[] split = classNumber.split("-");
            if (split.length == 1 && !StringUtils.equalsIgnoreCase(WiseConstants.ALL, split[0])) {
                try {
                    Integer.parseInt(split[0]);
                } catch (Exception e) {
                    checkInfo.append(" defectType rule value is wrong .");
                    checkSuccess = false;
                }
            } else if (split.length > 1) {
                try {
                    Integer.parseInt(split[0]);
                } catch (Exception e) {
                    checkInfo.append(" defectType rule value is wrong .");
                    checkSuccess = false;
                }
            }
        }

        // 校验 defectSpec
        String norDefectSpec = excelInputDTO.getNorDefectSpec();
        if (!StringUtils.isEmpty(norDefectSpec)) {
            if (!StringUtils.equalsIgnoreCase("BSL",norDefectSpec)){
                try {
                    Integer.parseInt(norDefectSpec);
                } catch (Exception e) {
                    checkInfo.append(" impactDieSpec value is wrong .");
                    checkSuccess = false;
                }
            }
        }
        String actDefectSpec = excelInputDTO.getActDefectSpec();
        if (!StringUtils.isEmpty(actDefectSpec)) {
            if (!StringUtils.equals("BSL", actDefectSpec)) {
                try {
                    Integer.parseInt(actDefectSpec);
                } catch (Exception e) {
                    checkInfo.append(" impactDieSpec value is wrong .");
                    checkSuccess = false;
                }
            }
        }
        if ((!StringUtils.equals("BSL", actDefectSpec) && StringUtils.equals("BSL", norDefectSpec))
            || (!StringUtils.equals("BSL", norDefectSpec) && StringUtils.equals("BSL", actDefectSpec))) {
            checkInfo.append(" act spec and nor spec bsl must at same time .");
            checkSuccess = false;
        }

        // 校验 impactDieSpec
        String impactDieSpec = excelInputDTO.getImpactDieSpec();
        if (!StringUtils.isEmpty(impactDieSpec)) {
            try {
                Integer.parseInt(impactDieSpec);
            } catch (Exception e) {
                checkInfo.append(" impactDieSpec value is wrong .");
                checkSuccess = false;
            }
        }
        // 校验 overSpecRatio rule value
        String overSpecRatio = excelInputDTO.getOverSpecRatio();
        if (!StringUtils.isEmpty(overSpecRatio)){
            String[] split = overSpecRatio.split("/");
            if (split.length !=2 ){
                checkInfo.append(" overSpecRatio rule value is wrong .");
                checkSuccess = false;
            }else {
                try {
                    int i = Integer.parseInt(split[0]);
                    int i1 = Integer.parseInt(split[1]);
                    if (i1 < i || i1 < 0 || i <=0 ){
                        throw new RuntimeException();
                    }
                }catch (Exception e){
                    checkInfo.append(" overSpecRatio rule value is wrong .");
                    checkSuccess = false;
                }
            }
        }
        // 校验 trendUp rule value
        String trendUp = excelInputDTO.getTrendUp();
        if (!StringUtils.isEmpty(trendUp)) {
            try {
                Integer.parseInt(trendUp);
            } catch (Exception e) {
                checkInfo.append(" trendUp rule  value is wrong .");
                checkSuccess = false;
            }
        }
        // 校验 suddenAppear rule value
        String suddenAppear = excelInputDTO.getSuddenAppear();
        if (!StringUtils.isEmpty(suddenAppear)) {
            try {
                Integer.parseInt(suddenAppear);
            } catch (Exception e) {
                checkInfo.append(" suddenAppear rule  value is wrong .");
                checkSuccess = false;
            }
        }
        // 校验 defectRuleEnableFlag
        String defectRuleEnableFlag = excelInputDTO.getDefectRuleEnableFlag();
        if (!StringUtils.isEmpty(defectRuleEnableFlag)) {
            if (!StringUtils.equalsIgnoreCase(defectRuleEnableFlag,"yes") && !StringUtils.equalsIgnoreCase(defectRuleEnableFlag,"no")) {
                checkInfo.append(" defectRuleEnableFlag  value is wrong .");
                checkSuccess = false;
            }
        }
        // 校验 defectRuleHLModuleFlag
        String defectRuleHLModuleFlag = excelInputDTO.getDefectRuleHLModuleFlag();
        if (!StringUtils.isEmpty(defectRuleHLModuleFlag)) {
            if (!StringUtils.equalsIgnoreCase(defectRuleHLModuleFlag,"yes") && !StringUtils.equalsIgnoreCase(defectRuleHLModuleFlag,"no")){
                checkInfo.append(" defectRuleHLModuleFlag  value is wrong .");
                checkSuccess = false;
            }
        }
        // 校验 impactDieRuleHLModuleFlag
        String impactDieRuleHLModuleFlag = excelInputDTO.getImpactDieRuleHLModuleFlag();
        if (!StringUtils.isEmpty(impactDieRuleHLModuleFlag)) {
            if (!StringUtils.equalsIgnoreCase(defectRuleHLModuleFlag,"yes") && !StringUtils.equalsIgnoreCase(defectRuleHLModuleFlag,"no")){
                checkInfo.append(" impactDieRuleHLModuleFlag  value is wrong .");
                checkSuccess = false;
            }

        }
        // 校验 impactDieRuleEnableFlag
        String impactDieRuleEnableFlag = excelInputDTO.getImpactDieRuleEnableFlag();
        if (!StringUtils.isEmpty(impactDieRuleEnableFlag)) {
            if (!StringUtils.equalsIgnoreCase(impactDieRuleEnableFlag,"yes") && !StringUtils.equalsIgnoreCase(impactDieRuleEnableFlag,"no")){
                checkInfo.append(" impactDieRuleEnableFlag  value is wrong .");
                checkSuccess = false;
            }
        }
        // 校验 overSpecRatioRuleEnableFlag
        String overSpecRatioRuleEnableFlag = excelInputDTO.getOverSpecRatioRuleEnableFlag();
        if (!StringUtils.isEmpty(overSpecRatioRuleEnableFlag)) {
            if (!StringUtils.equalsIgnoreCase(overSpecRatioRuleEnableFlag,"yes") && !StringUtils.equalsIgnoreCase(overSpecRatioRuleEnableFlag,"no")){
                checkInfo.append(" overSpecRatioRuleEnableFlag  value is wrong .");
                checkSuccess = false;
            }

        }
        // 校验 overSpecRatioRuleHLModuleFlag
        String overSpecRatioRuleHLModuleFlag = excelInputDTO.getOverSpecRatioRuleHLModuleFlag();
        if (!StringUtils.isEmpty(overSpecRatioRuleHLModuleFlag)) {
            if (!StringUtils.equalsIgnoreCase(overSpecRatioRuleHLModuleFlag,"yes") && !StringUtils.equalsIgnoreCase(overSpecRatioRuleHLModuleFlag,"no")){
                checkInfo.append(" overSpecRatioRuleHLModuleFlag  value is wrong .");
                checkSuccess = false;
            }

        }
        // 校验 trendUpRuleEnableFlag
        String trendUpRuleEnableFlag = excelInputDTO.getTrendUpRuleEnableFlag();
        if (!StringUtils.isEmpty(trendUpRuleEnableFlag)) {
            if (!StringUtils.equalsIgnoreCase(trendUpRuleEnableFlag,"yes") && !StringUtils.equalsIgnoreCase(trendUpRuleEnableFlag,"no")){
                checkInfo.append(" trendUpRuleEnableFlag  value is wrong .");
                checkSuccess = false;
            }
        }
        // 校验 trendUpRuleHLModuleFlag
        String trendUpRuleHLModuleFlag = excelInputDTO.getTrendUpRuleHLModuleFlag();
        if (!StringUtils.isEmpty(trendUpRuleHLModuleFlag)) {
            if (!StringUtils.equalsIgnoreCase(trendUpRuleHLModuleFlag, "yes") && !StringUtils.equalsIgnoreCase(trendUpRuleHLModuleFlag, "no")) {
                checkInfo.append(" trendUpRuleHLModuleFlag  value is wrong .");
                checkSuccess = false;
            }
        }

        // 校验 suddenAppearRuleEnableFlag
        String suddenAppearRuleEnableFlag = excelInputDTO.getSuddenAppearRuleEnableFlag();
        if (!StringUtils.isEmpty(trendUpRuleHLModuleFlag)) {
            if (!StringUtils.equalsIgnoreCase(suddenAppearRuleEnableFlag,"yes") && !StringUtils.equalsIgnoreCase(suddenAppearRuleEnableFlag,"no")) {
                checkInfo.append(" suddenAppearRuleEnableFlag  value is wrong .");
                checkSuccess = false;
            }
        }
        // 校验 suddenAppearRuleHLModuleFlag
        String suddenAppearRuleHLModuleFlag = excelInputDTO.getSuddenAppearRuleHLModuleFlag();
        if (!StringUtils.isEmpty(suddenAppearRuleHLModuleFlag)) {
            if (!StringUtils.equalsIgnoreCase(suddenAppearRuleHLModuleFlag,"yes") && !StringUtils.equalsIgnoreCase(suddenAppearRuleHLModuleFlag,"no")){
                checkInfo.append(" suddenAppearRuleHLModuleFlag  value is wrong .");
                checkSuccess = false;
            }
        }
        excelInputDTO.setCheckSuccessFlag(checkSuccess);
        if (checkSuccess){
            excelInputDTO.setCheckResult("check success");
        }else {
            excelInputDTO.setCheckResult(checkInfo.toString());
        }

        return excelInputDTO;
    }

    @Override
    public List<InformRuleImportDTO> batchCheckExcelData(List<InformRuleImportDTO> excelInputDTOList) {
        List<InformRuleImportDTO> list = new ArrayList<>();
        for (InformRuleImportDTO item : excelInputDTOList) {
            list.add(checkExcelData(item));
        }
        return list;
    }

    @Override
    public List<InformRuleImportModule> parseToRealTypes(List<InformRuleImportDTO> dataObject) {
        ArrayList<InformRuleImportModule> result = new ArrayList<>();
        for (InformRuleImportDTO informRuleImportDTO : dataObject) {
            InformRuleImportModule informRuleImportModule = new InformRuleImportModule();
            informRuleImportModule.setTech(informRuleImportDTO.getTech());
            if (StringUtils.equalsIgnoreCase(informRuleImportDTO.getLoop(),"all")){
                informRuleImportModule.setLoop(WiseConstants.ALL);
            }else{
                informRuleImportModule.setLoop(informRuleImportDTO.getLoop());
            }
            if (StringUtils.equalsIgnoreCase(informRuleImportDTO.getLayer(),"all")){
                informRuleImportModule.setLayer(WiseConstants.ALL);
            }else{
                informRuleImportModule.setLayer(informRuleImportDTO.getLayer());
            }
            if (StringUtils.equalsIgnoreCase(informRuleImportDTO.getProductId(),"all")){
                informRuleImportModule.setProductId(WiseConstants.ALL);
            }else{
                informRuleImportModule.setProductId(informRuleImportDTO.getProductId());
            }
            if (StringUtils.equalsIgnoreCase(informRuleImportDTO.getDefectType(),"all")){
                informRuleImportModule.setDefectCode(Integer.MIN_VALUE);
            }else{
                String[] split = informRuleImportDTO.getDefectType().split("-");
                informRuleImportModule.setDefectCode(Integer.parseInt(split[0]));
            }
            if (!StringUtils.equalsIgnoreCase(informRuleImportDTO.getActDefectSpec(),"bsl")){
                informRuleImportModule.setActDefectSpec(informRuleImportDTO.getActDefectSpec() == null ? null :Integer.parseInt(informRuleImportDTO.getActDefectSpec()));
            }
            if (!StringUtils.equalsIgnoreCase(informRuleImportDTO.getNorDefectSpec(),"bsl")){
                informRuleImportModule.setNorDefectSpec(informRuleImportDTO.getNorDefectSpec() == null ? null : Integer.parseInt(informRuleImportDTO.getNorDefectSpec()));
            }
            informRuleImportModule.setImpactDieSpec(informRuleImportDTO.getImpactDieSpec() == null ? null: Integer.parseInt(informRuleImportDTO.getImpactDieSpec()));

            String overSpecRatio = informRuleImportDTO.getOverSpecRatio();
            if (StringUtils.isNotEmpty(overSpecRatio)){
                String[] overRatio = overSpecRatio.split("/");
                informRuleImportModule.setOverSpecRatioNumerator(Integer.parseInt(overRatio[0])); //分子
                informRuleImportModule.setOverSpecRatioDenominator(Integer.parseInt(overRatio[1]));//分母
            }
            informRuleImportModule.setTrendUp(informRuleImportDTO.getTrendUp() == null ? null : Integer.parseInt(informRuleImportDTO.getTrendUp()));
            informRuleImportModule.setSuddenAppear(informRuleImportDTO.getSuddenAppear() == null ? null : Integer.parseInt(informRuleImportDTO.getSuddenAppear()));
            informRuleImportModule.setDefectRuleHLModuleFlag(ExcelProcessor.stringFlagToBoolean(informRuleImportDTO.getDefectRuleHLModuleFlag()));
            informRuleImportModule.setDefectRuleEnableFlag(ExcelProcessor.stringFlagToBoolean(informRuleImportDTO.getDefectRuleEnableFlag()));
            informRuleImportModule.setImpactDieRuleHLModuleFlag(ExcelProcessor.stringFlagToBoolean(informRuleImportDTO.getImpactDieRuleHLModuleFlag()));
            informRuleImportModule.setImpactDieRuleEnableFlag(ExcelProcessor.stringFlagToBoolean(informRuleImportDTO.getImpactDieRuleEnableFlag()));
            informRuleImportModule.setOverSpecRatioRuleHLModuleFlag(ExcelProcessor.stringFlagToBoolean(informRuleImportDTO.getOverSpecRatioRuleHLModuleFlag()));
            informRuleImportModule.setOverSpecRatioRuleEnableFlag(ExcelProcessor.stringFlagToBoolean(informRuleImportDTO.getOverSpecRatioRuleEnableFlag()));
            informRuleImportModule.setTrendUpRuleHLModuleFlag(ExcelProcessor.stringFlagToBoolean(informRuleImportDTO.getTrendUpRuleHLModuleFlag()));
            informRuleImportModule.setTrendUpRuleEnableFlag(ExcelProcessor.stringFlagToBoolean(informRuleImportDTO.getTrendUpRuleEnableFlag()));
            informRuleImportModule.setSuddenAppearRuleHLModuleFlag(ExcelProcessor.stringFlagToBoolean(informRuleImportDTO.getSuddenAppearRuleHLModuleFlag()));
            informRuleImportModule.setSuddenAppearRuleEnableFlag(ExcelProcessor.stringFlagToBoolean(informRuleImportDTO.getSuddenAppearRuleEnableFlag()));

            if(StringUtils.isEmpty(informRuleImportDTO.getActDefectSpec()) && StringUtils.isEmpty(informRuleImportDTO.getNorDefectSpec())){
                informRuleImportModule.setSetDefectRuleFlag(false);
            }else{
                informRuleImportModule.setSetDefectRuleFlag(true);
            }
            if(StringUtils.isEmpty(informRuleImportDTO.getImpactDieSpec())){
                informRuleImportModule.setSetImpactDieRuleFlag(false);
            }else{
                informRuleImportModule.setSetImpactDieRuleFlag(true);
            }
            if(StringUtils.isEmpty(informRuleImportDTO.getOverSpecRatio())){
                informRuleImportModule.setSetOverRatioRuleFlag(false);
            }else{
                informRuleImportModule.setSetOverRatioRuleFlag(true);
            }
            if(StringUtils.isEmpty(informRuleImportDTO.getSuddenAppear())){
                informRuleImportModule.setSetSuddenAppearRuleFlag(false);
            }else{
                informRuleImportModule.setSetSuddenAppearRuleFlag(true);
            }
            if(StringUtils.isEmpty(informRuleImportDTO.getTrendUp())){
                informRuleImportModule.setSetTrendUpRuleFlag(false);
            }else{
                informRuleImportModule.setSetTrendUpRuleFlag(true);
            }
            result.add(informRuleImportModule);
        }

        return result;
    }

}
