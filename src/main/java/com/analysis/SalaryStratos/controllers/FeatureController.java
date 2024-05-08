package com.analysis.SalaryStratos.controllers;

import com.analysis.SalaryStratos.dataStructures.array.SortedArray;
import com.analysis.SalaryStratos.dataStructures.trie.TrieDS;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Autowired
    private  final PageRanking pageRanking;
    @Autowired
    private  final CompareRumTimes compareRumTimes;


    public FeatureController(FetchAndUpdateData jobService, DataValidation dataValidation, JobSearchAndSort jobSearchAndSort, SearchFrequency searchFrequency, SimplyHiredScraper simplyHiredScraper, RemoteOk remoteOk, GlassDoorScraper glassDoorScraper, JobDataTrie jobData, SpellChecker spellChecker, PageRanking pageRanking, CompareRumTimes compareRumTimes) {
        this.jobService = jobService;
        this.dataValidation = dataValidation;
        this.jobSearchAndSort = jobSearchAndSort;
        this.searchFrequency = searchFrequency;
        this.simplyHiredScraper = simplyHiredScraper;
        this.remoteOk = remoteOk;
        this.glassDoorScraper = glassDoorScraper;
        this.jobData = jobData;
        this.spellChecker = spellChecker;
        this.pageRanking = pageRanking;
        this.compareRumTimes = compareRumTimes;
    }

    @CrossOrigin
    @PostMapping(value = "/correctWords")
    public List<SpellCheckerResponse> getCorrectWord(@RequestParam String searchTerm, @RequestParam int suggestionCount) {
        System.out.println(searchTerm + " " + suggestionCount);
        List<String> validatedSearchTerms = DataValidation.validateRequest(searchTerm);
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
    public SortedArray<WordFrequency> getRecentSearches() {
        return  searchFrequency.displaySearchFrequencies();
    }

    @CrossOrigin
    @PostMapping(value = "/wordSuggestions")
    public WordSuggestionResponse getSuggestions(@RequestParam String searchTerm, @RequestParam int suggestionCount) {
        List<String> validatedSearchTerms = DataValidation.validateRequest(searchTerm);
        return WordCompletion.getWordSuggestions(validatedSearchTerms, jobData, suggestionCount);
    }


    //sortBy will sort the response based on frequency or salary
    @CrossOrigin
    @PostMapping(value = "/pageRanking/searchJobs")
    public SortedArray<Job> searchJobs(@RequestParam String searchTerm,
                                       @RequestParam(required = false) String sortBy) throws FileNotFoundException, InterruptedException {

        String[] validatedSearchTerms = searchTerm.split(" ");

        searchFrequency.updateSearchFrequency(validatedSearchTerms);
        if(Objects.nonNull(sortBy) && sortBy.equals("salary"))
            return  pageRanking.searchInvertedIndexedDataBySalary(validatedSearchTerms, jobData.getInitTrie(), jobData );
        else
            return  pageRanking.searchInvertedIndexedData(validatedSearchTerms, jobData.getInitTrie(), jobData );
    }

    @CrossOrigin
    @GetMapping(value = "/runTimes")
    public CompareRunTimesData compareRunTime(@RequestParam String searchTerm) throws FileNotFoundException, InterruptedException {

        String[] validatedSearchTerms = searchTerm.split(" ");

        return  compareRumTimes.compareRunTimeForSortingAlgorithms(validatedSearchTerms, jobData.getInitTrie(), jobData );

    }

    //Crawling the data
    @CrossOrigin
    @PostMapping(value = "/crawl")
    @ResponseBody
    public Boolean crawlData(@RequestBody CrawlerRequest crawlerRequest) throws InterruptedException, FileNotFoundException {
        System.out.println(Arrays.toString(crawlerRequest.getSearchTerms()));
        System.out.println(crawlerRequest.isSimplyHired());
        //If delete is true then delete the file and create new one
        if(crawlerRequest.isDelete()) {
            String jsonFilePath = "src/main/resources/database.json";
            Path path = Paths.get(jsonFilePath);
            try {
                Files.delete(path);
                System.out.println("JSON file deleted successfully.");
            } catch (IOException e) {
                System.err.println("Error deleting JSON file: " + e.getMessage());
            }
        }

        boolean simplyHiredBoolean = crawlerRequest.isSimplyHired();
        boolean remoteOkBoolean = crawlerRequest.isRemoteOk();
        boolean glassDoorBoolean = crawlerRequest.isGlassDoor();

        String[] searchTerms = getSearchTerms(crawlerRequest);

        if(simplyHiredBoolean) {
            System.out.println("SimplyHired Crawling Started");
            simplyHiredScraper.crawlWebPage(searchTerms);
            System.out.println("SimplyHired Crawling Ended");
        }
        if(remoteOkBoolean) {
            System.out.println("RemoteOk Crawling Started");
            remoteOk.crawlRemoteOk(searchTerms);
            System.out.println("RemoteOk Crawling Ended");
        }
        if(glassDoorBoolean) {
            System.out.println("Glassdoor Crawling Started");
            glassDoorScraper.crawlWebPage(searchTerms);
            System.out.println("Glassdoor Crawling Ended");
        }

        TrieDS trie = jobData.initializeTrie();
        spellChecker.initializeSpellChecker(trie);
        return true;
    }

    private static String[] getSearchTerms(CrawlerRequest crawlerRequest) {
        String[] searchTerms = new String[]{
                "Engineer", "Exec", "Senior", "Developer", "Finance", "Sys Admin", "JavaScript", "Backend", "Golang", "Cloud", "Front End"
        };

        if (Objects.nonNull(crawlerRequest.getSearchTerms()) && crawlerRequest.getSearchTerms().length > 0) {
            searchTerms = crawlerRequest.getSearchTerms();
        }
        return searchTerms;
    }

}
