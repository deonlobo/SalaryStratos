package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.Data.Job;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class FetchData {
    public List<Job> readJsonData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new ClassPathResource("database.json").getFile();
            return Arrays.asList(objectMapper.readValue(file, Job[].class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
