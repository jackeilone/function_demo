package com.example.function_demo.Excel.FormatTemplate;


import lombok.Getter;

@Getter
public enum ExcelTemplateEnum {

    INFORM_SPEC_RULE_TEMPLATE("inform_spec_rule_template", "template_1", 1, 2),

    DEFECT_LIMIT_YIELD_CONFIG_TEMPLATE("defect_limit_yield_config_template", "template_2", 1, 2);

    private final String templateName; //模板名称

    private final String templateNo; //模板编号

    private final int headerRowNo; //表头所在的行

    private final int dataStartRowNo; //数据的起始行


    ExcelTemplateEnum(String templateName, String templateNo, int headerRowNo, int dataStartRowNo) {
        this.templateName = templateName;
        this.templateNo = templateNo;
        this.headerRowNo = headerRowNo;
        this.dataStartRowNo = dataStartRowNo;

    }


}
