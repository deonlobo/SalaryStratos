package com.analysis.SalaryStratos.controllers;

import com.analysis.SalaryStratos.features.*;
import com.analysis.SalaryStratos.features.scraper.GlassDoorScraper;
import com.analysis.SalaryStratos.features.scraper.RemoteOk;
import com.analysis.SalaryStratos.features.scraper.SimplyHiredScraper;
import com.analysis.SalaryStratos.models.*;
import com.analysis.SalaryStratos.features.DataValidation;
import com.analysis.SalaryStratos.features.FetchAndUpdateData;
import com.analysis.SalaryStratos.services.JobDataTrie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FeatureController {
    @Autowired
    private final FetchAndUpdateData jobService;
    @Autowired
    private final DataValidation dataValidation;
    @Autowired
    private final JobSearchAndSort jobSearchAndSort;
    @Autowired
    private final SearchFrequency searchFrequency;
    @Autowired
    private final SimplyHiredScraper simplyHiredScraper;
    @Autowired
    private final RemoteOk remoteOk;
    @Autowired
    private final GlassDoorScraper glassDoorScraper;

    @Autowired
    private final JobDataTrie jobData;

    public FeatureController(FetchAndUpdateData jobService, DataValidation dataValidation, JobSearchAndSort jobSearchAndSort, SearchFrequency searchFrequency, SimplyHiredScraper simplyHiredScraper, RemoteOk remoteOk, GlassDoorScraper glassDoorScraper, JobDataTrie jobData) {
        this.jobService = jobService;
        this.dataValidation = dataValidation;
        this.jobSearchAndSort = jobSearchAndSort;
        this.searchFrequency = searchFrequency;
        this.simplyHiredScraper = simplyHiredScraper;
        this.remoteOk = remoteOk;
        this.glassDoorScraper = glassDoorScraper;
        this.jobData = jobData;
    }

    @CrossOrigin
    @PostMapping(value = "/correctWords")
    @ResponseBody
    public String[] getCorrectWord(@RequestBody String  request) {
        System.out.println(request);
        return new String[]{"abc", "123"};
    }

    @CrossOrigin
    @GetMapping(value = "/recentSearches")
    @ResponseBody
    public Map<String, Integer> getRecentSearches() {

        Map<String, Integer> m1 = new HashMap<>();
        m1.put("Software", 4);
        m1.put("Developer", 2);
        m1.put("Deon", 1);
        return m1;
//        return new String[]{"abc", "123"};
    }

    @CrossOrigin
    @PostMapping(value = "/wordSuggestions")
    @ResponseBody
    public WordSuggestionResponse getSuggestions(@RequestBody String searchTerm) {
        List<String> validatedSearchTerms = DataValidation.validateSuggestionRequest(searchTerm);

    }



    @RequestMapping(value = "/jobs")
    @ResponseBody
    public List<Job> getJobs() {
        return jobService.readJsonData();
    }

    //Used for testing
    @GetMapping(value = "/validate")
    @ResponseBody
    public List<JobValidation> validateJobs() {
        return dataValidation.validateScrapedData();
    }

    //Used for testing
    //http://localhost:8080/search?searchTerm=account
    @PutMapping(value = "/search")
    @ResponseBody
    public List<Job> jobSearch(@RequestParam String searchTerm) {
        return jobSearchAndSort.searchAndSortJobs(searchTerm.toLowerCase());
    }

    //Returns all the searchFrequencies
    @GetMapping(value = "/search/frequency")
    @ResponseBody
    public Map<String, Integer> jobSearchFrequency() {
        return searchFrequency.displaySearchFrequencies();
    }

    //Crawling the data
    @PostMapping(value = "/crawl")
    @ResponseBody
    public Boolean crawlData(@RequestBody CrawlRequest crawlRequest) throws InterruptedException {
        // Access the parameters from crawlRequest and perform the necessary logic
        boolean simplyHiredBoolen = crawlRequest.isSimplyHired();
        boolean remoteOkBoolean = crawlRequest.isRemoteOk();
        boolean glassDoorBoolean = crawlRequest.isGlassDoor();
        String[] searchTerms = crawlRequest.getSearchTerms();

        if(simplyHiredBoolen)
            simplyHiredScraper.crawlWebPage(crawlRequest.getSearchTerms());
        if(remoteOkBoolean)
            remoteOk.crawlRemoteOk(crawlRequest.getSearchTerms());
        if(glassDoorBoolean)
            glassDoorScraper.crawlWebPage(crawlRequest.getSearchTerms());

        
        return true;
    }

}
