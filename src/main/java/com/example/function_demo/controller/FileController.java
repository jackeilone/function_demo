package com.example.function_demo.controller;

import com.example.function_demo.Excel.FormatTemplate.ExcelTemplateEnum;
import com.example.function_demo.Excel.Processor.ExcelProcessor;
import com.example.function_demo.Excel.data.dto.InformRuleImportDTO;
import com.example.function_demo.dto.ParamSpecKeyRequestDTO;
import com.example.function_demo.dto.Response;
import com.example.function_demo.service.InformRuleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private InformRuleService informRuleService;

    @RequestMapping(value = "/inform/paramSpec/checkFileData", method = RequestMethod.POST)
    @Operation(description = " 校验 excel file 中 每条inform Rule data 的数据格式是否正确 ")
    public Response<List<InformRuleImportDTO>> checkInformSpecFileData(@RequestParam("file") MultipartFile file, String userName) throws Exception {

        List<InformRuleImportDTO> checkResult = informRuleService.checkInformSpecData(file,userName);
        return Response.ok(checkResult);
    }

    @RequestMapping(value = "/inform/paramSpec/checkTableData", method = RequestMethod.POST)
    @Operation(description = " 校验 单条 inform Rule data 的数据格式是否正确 ")
    public Response<List<InformRuleImportDTO>> checkInformSpecDataByKey(@RequestBody List<InformRuleImportDTO> fileData,  String userName)  {
        List<InformRuleImportDTO> checkResult = informRuleService.checkInformSpecDataByKey(fileData,userName);
        return Response.ok(checkResult);
    }

    @RequestMapping(value = "/inform/paramSpec/importFile", method = RequestMethod.POST)
    @Operation(description = " inform rule excel 配置表导入 ")
    public Response<Void> importInformRuleFileData(@RequestParam("file") MultipartFile file,  String userName) {
        informRuleService.importInformRuleFileData(file,userName);
        return Response.ok();
    }

    @RequestMapping(value = "/inform/paramSpec/importList", method = RequestMethod.POST)
    @Operation(description = " inform rule 页面数据 配置表导入 ")
    public Response<Void> importInformRuleData(@RequestBody List<InformRuleImportDTO> fileData,String userName) {

        informRuleService.importInformRuleData(fileData,userName);
        return Response.ok();
    }

    @RequestMapping(value = "/inform/paramSpec/exportByFilter", method = RequestMethod.POST)
    @Operation(description = "根据条件导出 inform rule excel ")
    public void exportInformSpecKeyByCondition(@RequestBody ParamSpecKeyRequestDTO paramSpecKeyRequestDTO,
                                               HttpServletResponse response,
                                                String userName) throws IOException {

        List<Object> data = informRuleService.queryInformSpecDTO(paramSpecKeyRequestDTO);
        ExcelProcessor.downLoadExcelData(response, ExcelTemplateEnum.INFORM_SPEC_RULE_TEMPLATE,data);
    }

    @RequestMapping(value = "/inform/paramSpec/deleteByFilter", method = RequestMethod.POST)
    @Operation(description = "删除 inform rule excel ")
    public Response<Void> deleteInformSpecKeyByCondition(@RequestBody ParamSpecKeyRequestDTO paramSpecKeyRequestDTO,
                                                          String userName) throws IOException {

        informRuleService.deleteInformSpecByCondition(paramSpecKeyRequestDTO);
        return Response.ok();
    }

}
