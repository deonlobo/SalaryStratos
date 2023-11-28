package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.models.Job;
import com.analysis.SalaryStratos.models.Jobs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FetchAndUpdateData {
    public static List<Job> readJsonData() {
       /* ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new ClassPathResource("database.json").getFile();
            //return Arrays.asList(objectMapper.readValue(file, Job[].class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }*/
        Gson jsonHandler = new Gson();
        Jobs jobs = null;
        try {
            jobs = jsonHandler.fromJson(new FileReader("src/main/resources/database.json"), Jobs.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (ArrayList<Job>) jobs.getJobs();
    }

  /*  public static List<Job> appendListToJson(List<Job> jobList) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("src/main/resources/database.json");
            List<Job> fileData;

            // Check if the file exists
            if (file.exists() && file.length() > 0) {
                fileData = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Job[].class)));
            }else{
                // Create a new file if it doesn't exist
                file.createNewFile();
                fileData = new ArrayList<>();
            }

            // Append the new jobs
            fileData.addAll(jobList);

            System.out.println(jobList.size());
            System.out.println(fileData.size());
            // Write the updated list of jobs back to the JSON file
            objectMapper.writeValue(file, fileData);

            return fileData;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
