package com.analysis.SalaryStratos.controllers;

import com.analysis.SalaryStratos.models.Job;
import com.analysis.SalaryStratos.models.JobValidation;
import com.analysis.SalaryStratos.features.DataValidation;
import com.analysis.SalaryStratos.features.FetchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FeatureController {
    @Autowired
    private final FetchData jobService;
    @Autowired
    private final DataValidation dataValidation;

    public FeatureController(FetchData jobService, DataValidation dataValidation) {
        this.jobService = jobService;
        this.dataValidation = dataValidation;
    }

    @RequestMapping(value = "")
    @ResponseBody
    public String index() {
        return "helloooo world !!";
    }

    @RequestMapping(value = "/jobs")
    @ResponseBody
    public List<Job> getJobs() {
        return jobService.readJsonData();
    }

    @GetMapping(value = "/validate")
    @ResponseBody
    public List<JobValidation> validateJobs() {
        return dataValidation.validateScrapedData();
    }
}
