package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.models.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class JobSearchAndSort {

    @Autowired
    private final FetchData jobService;
    @Autowired
    private final SearchFrequency searchFrequency;

    public JobSearchAndSort(FetchData jobService, SearchFrequency searchFrequency) {
        this.jobService = jobService;
        this.searchFrequency = searchFrequency;
    }


    public List<Job> searchAndSortJobs(String searchTerm) {
        List<Job> allJobs = jobService.readJsonData();
        // Use a regular expression to match jobTitle partially or completely
        Pattern pattern = Pattern.compile("(?i).*" + searchTerm + ".*");

        // Filter jobs based on the regular expression
        List<Job> matchingJobs = allJobs.stream()
                .filter(job -> pattern.matcher(job.getJobTitle()).matches())
                .collect(Collectors.toList());

        // Sort matching jobs in descending order based on salary
        matchingJobs.sort(Comparator.comparingInt(Job::getMinSalary).reversed());

        //Update SearchFrequency
        searchFrequency.updateSearchFrequency(searchTerm);
        return matchingJobs;
    }

}
