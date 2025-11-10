package com.example.function_demo.Excel;


import com.alibaba.excel.EasyExcel;
import com.example.function_demo.Excel.data.dto.InformRuleImportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;
import com.example.function_demo.Excel.FormatTemplate.ExcelTemplateEnum;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Slf4j
public class ExcelUtils {


    public static <T> List<T> fileToDataObject(MultipartFile file,  Class  clazzType,int startRow) {

        try (InputStream inputStream = file.getInputStream()) {
            return EasyExcel.read(inputStream).head(clazzType).sheet(0)
                    .headRowNumber(startRow) // 实际上这里需要的是数据的起始行
                    .doReadSync();
        } catch (IOException e) {
            log.error("transfer to data Object error : {}", e.getMessage());
            return Collections.emptyList();
        }
    }


    public static List<Map<Integer, String>> getExcelHeadRow(MultipartFile file,ExcelTemplateEnum excelTemplate) throws IOException{

        InputStream inputStream = file.getInputStream();
        return EasyExcel.read(inputStream)
                .headRowNumber(excelTemplate.getHeaderRowNo())
                .sheet()
                .doReadSync();

    };

    public static InputStream getExcelTemplateByNo(ExcelTemplateEnum excelTemplate) throws IOException {
        String fileName = "ExcelTemplate/" + excelTemplate.getTemplateNo() + ".xlsx";
        ClassPathResource resource = new ClassPathResource(fileName);
        return resource.getInputStream();
    }

    public static List<Map<Integer, String>> getExcelTemplateHeadRow(ExcelTemplateEnum excelTemplate) throws IOException{

        InputStream excelTemplateByNo = ExcelUtils.getExcelTemplateByNo(excelTemplate);

        return EasyExcel.read(excelTemplateByNo)
                .headRowNumber(excelTemplate.getHeaderRowNo())
                .sheet()
                .doReadSync();

    };

    public static Class<?> getDataClassByEnum(ExcelTemplateEnum excelTemplate) {
        if (excelTemplate == null) {
            throw new IllegalArgumentException("枚举类不能为空");
        }
        // 根据不同的枚举类返回不同的Class
        if (ExcelTemplateEnum.INFORM_SPEC_RULE_TEMPLATE.equals(excelTemplate)) {
            return InformRuleImportDTO.class;
        }
        // 如果没匹配上，返回null或者抛异常
        return null;
    }



}
