package com.example.function_demo.Excel.data.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defect Limit Yield Config Import DTO
 * 用于 Excel 导入功能
 * 
 * @date 2025-10-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DLYConfigImportDTO {

    @ExcelProperty(index = 0)
    private String checkResult;

    @ExcelProperty(index = 1)
    private String tech;

    @ExcelProperty(index = 2)
    private String productId;

    @ExcelProperty(index = 3)
    private String loop;

    @ExcelProperty(index = 4)
    private String layerId;

    @ExcelProperty(index = 5)
    private String defectType;

    @ExcelProperty(index = 6)
    private String killRatio;

    @ExcelProperty(index = 7)
    private String potentialFailBin;

    @ExcelProperty(index = 8)
    private String autoUpdateKillRatio;

    @ExcelProperty(index = 9)
    private String description;

    @ExcelIgnore
    private Boolean checkSuccessFlag;

}

