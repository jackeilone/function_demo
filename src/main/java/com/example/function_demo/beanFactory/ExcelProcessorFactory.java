package com.example.function_demo.beanFactory;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelProcessorFactory {

    private final Map<ExcelTemplateEnum, ExcelProcessor> engineMap = new HashMap<>();

    @Autowired
    public ExcelProcessorFactory(List<ExcelProcessor> engines) {
        for (ExcelProcessor engine : engines) {
            engineMap.put(engine.getTemplate(), engine);
        }
    }

    public ExcelProcessor getExcelProcessor(ExcelTemplateEnum template) {
        return engineMap.get(template);
    }


}
