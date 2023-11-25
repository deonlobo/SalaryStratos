package com.analysis.SalaryStratos.controllers;

import com.analysis.SalaryStratos.features.JobSearchAndSort;
import com.analysis.SalaryStratos.features.SearchFrequency;
import com.analysis.SalaryStratos.models.Job;
import com.analysis.SalaryStratos.models.JobValidation;
import com.analysis.SalaryStratos.features.DataValidation;
import com.analysis.SalaryStratos.features.FetchAndUpdateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class FeatureController {
    @Autowired
    private final FetchAndUpdateData jobService;
    @Autowired
    private final DataValidation dataValidation;
    @Autowired
    private final JobSearchAndSort jobSearchAndSort;
    @Autowired
    private final SearchFrequency searchFrequency;

    public FeatureController(FetchAndUpdateData jobService, DataValidation dataValidation, JobSearchAndSort jobSearchAndSort, SearchFrequency searchFrequency) {
        this.jobService = jobService;
        this.dataValidation = dataValidation;
        this.jobSearchAndSort = jobSearchAndSort;
        this.searchFrequency = searchFrequency;
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

    //http://localhost:8080/search?searchTerm=account
    @PutMapping(value = "/search")
    @ResponseBody
    public List<Job> jobSearch(@RequestParam String searchTerm) {
        return jobSearchAndSort.searchAndSortJobs(searchTerm.toLowerCase());
    }

    @GetMapping(value = "/search/frequency")
    @ResponseBody
    public Map<String, Integer> jobSearchFrequency() {
        return searchFrequency.displaySearchFrequencies();
    }

}
