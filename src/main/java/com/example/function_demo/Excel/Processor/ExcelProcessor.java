package com.example.function_demo.Excel.Processor;

import ai.futurefab.wise.Excel.ExcelUtils;
import ai.futurefab.wise.Excel.FormatTemplate.ExcelTemplateEnum;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public interface ExcelProcessor<T, V> {

    ExcelTemplateEnum getTemplate();

    Boolean checkExcelHeadRow(MultipartFile uploadExcelFile);


    List<T> fileToDataObject(MultipartFile file);


    List<T> checkExcelData(MultipartFile uploadExcelFile);

    T checkExcelData(T excelInputDTO);

    List<T> batchCheckExcelData(List<T> excelInputDTOList);

    List<V> parseToRealTypes(List<T> dataObject);


    default void downLoadExcelTemplate(HttpServletResponse response, ExcelTemplateEnum excelTemplate) {
        // 读取 resources 目录下的 Excel 文件
        try (InputStream in = ExcelUtils.getExcelTemplateByNo(excelTemplate);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * 如果 data 是空，则下载对应的 模板，如果data 有值，则把data 填充进模板返给前端
    * */

    static void downLoadExcelData(HttpServletResponse response, ExcelTemplateEnum excelTemplate, List<Object> data) throws IOException {
        String fileName = URLEncoder.encode(excelTemplate.getTemplateName() + "_" + System.currentTimeMillis() + ".xlsx", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        try (InputStream templateStream = ExcelUtils.getExcelTemplateByNo(excelTemplate);
             Workbook workbook = WorkbookFactory.create(templateStream);
             OutputStream outputStream = response.getOutputStream()) {
             Sheet originalSheet = workbook.getSheetAt(0);

             Workbook newWorkbook = WorkbookFactory.create(true); // 创建新的工作簿
             Sheet newSheet = newWorkbook.createSheet("数据");

            if (data == null || data.isEmpty()) {
                // 只输出模板的前几行（保留样式）
                copyFirstNRowsToNewWorkbook(originalSheet, newSheet,excelTemplate.getDataStartRowNo());
                newWorkbook.write(outputStream);
                newWorkbook.close();
                return;
            }

            // 有数据的情况：先复制前几行，然后在后面添加数据

            // 1. 复制模板的前几行（保留样式）
            copyRowsWithStyle(originalSheet, newSheet, 0, excelTemplate.getDataStartRowNo());
            // 2. 添加业务数据（使用默认样式）
            ObjectMapper mapper = new ObjectMapper();
            for (int i = 0; i < data.size(); i++) {
                Object item = data.get(i);
                Object dto = mapper.convertValue(item, ExcelUtils.getDataClassByEnum(excelTemplate));
                createSimpleDataRow(newSheet, excelTemplate.getDataStartRowNo() + i, dto);
            }
            newWorkbook.write(outputStream);
            newWorkbook.close();

        } catch (Exception e) {
            throw new IOException("Excel导出失败: " + e.getMessage(), e);
        }
    }

    /**
     * 只复制模板的前N行到新工作簿
     */
    private static void copyFirstNRowsToNewWorkbook(Sheet originalSheet,Sheet newSheet ,int rowCount) throws IOException {
        // 复制前N行，保留样式
        copyRowsWithStyle(originalSheet, newSheet, 0, rowCount);
    }

    /**
     * 复制行并保留样式
     */
    private static void copyRowsWithStyle(Sheet sourceSheet, Sheet targetSheet, int startRow, int endRow) {
        for (int i = startRow; i < endRow; i++) {
            Row sourceRow = sourceSheet.getRow(i);
            if (sourceRow == null) continue;
            Row targetRow = targetSheet.createRow(i);
            // 复制行高
            targetRow.setHeight(sourceRow.getHeight());
            // 复制每个单元格
            for (int j = 0; j < sourceRow.getLastCellNum(); j++) {
                Cell sourceCell = sourceRow.getCell(j);
                if (sourceCell == null) continue;
                Cell targetCell = targetRow.createCell(j);
                // 复制单元格值
                copyCellValue(sourceCell, targetCell);
                // 复制单元格样式
                if (sourceCell.getCellStyle() != null) {
                    CellStyle newStyle = targetSheet.getWorkbook().createCellStyle();
                    newStyle.cloneStyleFrom(sourceCell.getCellStyle());
                    targetCell.setCellStyle(newStyle);
                }
            }
        }
    }

    /**
     * 复制单元格值
     */
    private static void copyCellValue(Cell sourceCell, Cell targetCell) {
        switch (sourceCell.getCellType()) {
            case STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(sourceCell)) {
                    targetCell.setCellValue(sourceCell.getDateCellValue());
                } else {
                    targetCell.setCellValue(sourceCell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                targetCell.setCellFormula(sourceCell.getCellFormula());
                break;
            default:
                targetCell.setCellValue("");
        }
    }

    /**
     * 创建简单的数据行（使用默认样式）
     */
    private static void createSimpleDataRow(Sheet sheet, int rowIndex, Object dto) {
        Row row = sheet.createRow(rowIndex);
        try {
            // 获取所有字段
            Field[] fields = dto.getClass().getDeclaredFields();
            // 按ExcelProperty注解的index排序，过滤没有注解的字段
            List<Field> sortedFields = Arrays.stream(fields)
                    .filter(field -> field.isAnnotationPresent(ExcelProperty.class))
                    .sorted(Comparator.comparingInt(field -> {
                        ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                        return annotation.index();
                    }))
                    .collect(Collectors.toList());

            for (int i = 0; i < sortedFields.size(); i++) {
                Field field = sortedFields.get(i);
                field.setAccessible(true);
                Object value = field.get(dto);
                Cell cell = row.createCell(i);
                // 直接设置单元格值
                if (value == null) {
                    cell.setCellValue("");
                } else if (value instanceof String) {
                    cell.setCellValue((String) value);
                } else if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else if (value instanceof Long) {
                    cell.setCellValue((Long) value);
                } else if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                } else if (value instanceof Float) {
                    cell.setCellValue((Float) value);
                } else if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                } else if (value instanceof Date) {
                    cell.setCellValue((Date) value);
                } else if (value instanceof Calendar) {
                    cell.setCellValue((Calendar) value);
                } else {
                    cell.setCellValue(value.toString());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("创建数据行失败", e);
        }
    }

    public static Boolean stringFlagToBoolean(String flag){
        if (StringUtils.equalsIgnoreCase(flag,"yes")){
            return true;
        }
        if (StringUtils.equalsIgnoreCase(flag,"no")){
            return false;
        }
        return null;
    }

    public static String booleanToString(Boolean flag){
        if (Boolean.TRUE.equals(flag)){
            return "yes";
        }
        if (Boolean.FALSE.equals(flag)){
            return "no";
        }
        return "no";
    }
}
