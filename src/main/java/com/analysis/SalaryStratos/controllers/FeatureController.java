package com.analysis.SalaryStratos.controllers;

import com.analysis.SalaryStratos.Data.Job;
import com.analysis.SalaryStratos.features.FetchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FeatureController {
    @Autowired
    private final FetchData jobService;

    public FeatureController(FetchData jobService) {
        this.jobService = jobService;
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
}
