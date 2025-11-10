package com.example.function_demo.Excel.data.module;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformRuleImportModule {

    private String tech;

    private String productId;

    private String loop;

    private String layer;

    private Integer defectCode;

    private String defectName;

    private Integer actDefectSpec;

    private Integer norDefectSpec;

    private Integer impactDieSpec;

    private Integer overSpecRatioNumerator; // 分子

    private Integer overSpecRatioDenominator; // 分母

    private Integer trendUp;

    private Integer suddenAppear;

    private Boolean defectRuleHLModuleFlag;

    private Boolean defectRuleEnableFlag;

    private Boolean impactDieRuleHLModuleFlag;

    private Boolean impactDieRuleEnableFlag;

    private Boolean overSpecRatioRuleHLModuleFlag;

    private Boolean overSpecRatioRuleEnableFlag;

    private Boolean trendUpRuleHLModuleFlag;

    private Boolean trendUpRuleEnableFlag;

    private Boolean suddenAppearRuleHLModuleFlag;

    private Boolean suddenAppearRuleEnableFlag;

    private Boolean setDefectRuleFlag;

    private Boolean setImpactDieRuleFlag;

    private Boolean setTrendUpRuleFlag;

    private Boolean setOverRatioRuleFlag;

    private Boolean setSuddenAppearRuleFlag;




}
