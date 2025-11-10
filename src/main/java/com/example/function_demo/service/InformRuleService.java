package com.example.function_demo.service;


import com.example.function_demo.Excel.data.dto.InformRuleImportDTO;
import com.example.function_demo.dto.ParamSpecKeyRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InformRuleService {


    List<InformRuleImportDTO> checkInformSpecData(MultipartFile file, String userName);

    List<InformRuleImportDTO> checkInformSpecDataByKey(List<InformRuleImportDTO> fileData, String userName);

    void importInformRuleFileData(MultipartFile file, String userName);

    void importInformRuleData(List<InformRuleImportDTO> fileData, String userName);

    List<Object> queryInformSpecDTO(ParamSpecKeyRequestDTO paramSpecKeyRequestDTO);

    void deleteInformSpecByCondition(ParamSpecKeyRequestDTO paramSpecKeyRequestDTO);
}
