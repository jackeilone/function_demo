package com.example.function_demo.Excel.data.dto;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformRuleImportDTO {

    @ExcelProperty(index = 0)
    private String checkResult;

    @ExcelProperty(index = 1)
    private String tech;

    @ExcelProperty(index = 2)
    private String productId;

    @ExcelProperty(index = 3)
    private String loop;

    @ExcelProperty(index = 4)
    private String layer;

    @ExcelProperty(index = 5)
    private String defectType;

    @ExcelProperty(index = 6)
    private String norDefectSpec;

    @ExcelProperty(index = 7)
    private String actDefectSpec;

    @ExcelProperty(index = 8)
    private String impactDieSpec;

    @ExcelProperty(index = 9)
    private String overSpecRatio;

    @ExcelProperty(index = 10)
    private String trendUp;

    @ExcelProperty(index = 11)
    private String suddenAppear;

    @ExcelProperty(index = 12)
    private String defectRuleHLModuleFlag;

    @ExcelProperty(index = 13)
    private String defectRuleEnableFlag;

    @ExcelProperty(index = 14)
    private String impactDieRuleHLModuleFlag;

    @ExcelProperty(index = 15)
    private String impactDieRuleEnableFlag;

    @ExcelProperty(index = 16)
    private String overSpecRatioRuleHLModuleFlag;

    @ExcelProperty(index = 17)
    private String overSpecRatioRuleEnableFlag;

    @ExcelProperty(index = 18)
    private String trendUpRuleHLModuleFlag;

    @ExcelProperty(index = 19)
    private String trendUpRuleEnableFlag;

    @ExcelProperty(index = 20)
    private String suddenAppearRuleHLModuleFlag;

    @ExcelProperty(index = 21)
    private String suddenAppearRuleEnableFlag;

    @ExcelIgnore
    private Boolean checkSuccessFlag;


}
