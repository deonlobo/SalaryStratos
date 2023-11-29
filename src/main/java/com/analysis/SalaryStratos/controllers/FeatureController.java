package com.analysis.SalaryStratos.controllers;

import com.analysis.SalaryStratos.dataStructures.array.SortedArray;
import com.analysis.SalaryStratos.models.WordFrequency;
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

import java.util.*;

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

    @Autowired
    private  final SpellChecker spellChecker;

    public FeatureController(FetchAndUpdateData jobService, DataValidation dataValidation, JobSearchAndSort jobSearchAndSort, SearchFrequency searchFrequency, SimplyHiredScraper simplyHiredScraper, RemoteOk remoteOk, GlassDoorScraper glassDoorScraper, JobDataTrie jobData, SpellChecker spellChecker) {
        this.jobService = jobService;
        this.dataValidation = dataValidation;
        this.jobSearchAndSort = jobSearchAndSort;
        this.searchFrequency = searchFrequency;
        this.simplyHiredScraper = simplyHiredScraper;
        this.remoteOk = remoteOk;
        this.glassDoorScraper = glassDoorScraper;
        this.jobData = jobData;
        this.spellChecker = spellChecker;
    }

    @CrossOrigin
    @PostMapping(value = "/correctWords")
    public List<SpellCheckerResponse> getCorrectWord(@RequestParam String searchTerm, @RequestParam int suggestionCount) {
        System.out.println(searchTerm + " " + suggestionCount);
        List<String> validatedSearchTerms = DataValidation.validateSuggestionRequest(searchTerm);
        List<SpellCheckerResponse> response = new ArrayList<>();
        for(String eachString: validatedSearchTerms) {
            System.out.println("each: " + eachString);

            TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> list = spellChecker.suggestSimilarWord(eachString,jobData.getInitTrie(), suggestionCount);
            SpellCheckerResponse res = new SpellCheckerResponse(eachString, list);
            response.add(res);
        }


        return response;
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
    public WordSuggestionResponse getSuggestions(@RequestParam String searchTerm, @RequestParam int suggestionCount) {
        List<String> validatedSearchTerms = DataValidation.validateSuggestionRequest(searchTerm);
        return WordCompletion.getWordSuggestions(validatedSearchTerms, jobData, suggestionCount);
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
    public SortedArray<WordFrequency> jobSearchFrequency() {
        return searchFrequency.displaySearchFrequencies();
    }

    //Crawling the data
    @PostMapping(value = "/crawl")
    @ResponseBody
    public Boolean crawlData(@RequestBody CrawlerRequest crawlerRequest) throws InterruptedException {
        // Access the parameters from crawlRequest and perform the necessary logic
        boolean simplyHiredBoolen = crawlerRequest.isSimplyHired();
        boolean remoteOkBoolean = crawlerRequest.isRemoteOk();
        boolean glassDoorBoolean = crawlerRequest.isGlassDoor();
        String[] searchTerms = crawlerRequest.getSearchTerms();

        if(simplyHiredBoolen)
            simplyHiredScraper.crawlWebPage(crawlerRequest.getSearchTerms());
        if(remoteOkBoolean)
            remoteOk.crawlRemoteOk(crawlerRequest.getSearchTerms());
        if(glassDoorBoolean)
            glassDoorScraper.crawlWebPage(crawlerRequest.getSearchTerms());

        
        return true;
    }

}
