package com.example.function_demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ParamSpecKeyRequestDTO {

    private List<String> tech;
    private List<String> productId;
    private List<String> loop;
    private List<String> layer;
    private List<String> defectClass = List.of("CLASS_NUMBER");
    private List<Integer> defectCode;
    private List<String> defectType;
    private List<String> informParamKey;
    private List<String> ruleId;
    private Boolean orderByInspectTimeDesc;
    private Integer pageSize = 10;
    private Integer pageNo = 1;
    private String columnName;

}
