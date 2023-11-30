package com.analysis.SalaryStratos.models;

import java.util.Map;

public class Job {

    private String id;
    private String jobTitle;
    private String companyName;
    private String jobWebsiteName;
    private String jobWebsiteLink;
    private int minSalary;
    private int maxSalary;
    private String location;
    private String jobDescription;
    private int  wordFrequency = 0;
    private String  word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public void setWordFrequency(int wordFrequency) {
        this.wordFrequency = wordFrequency;
    }

    public int getWordFrequency() {
        return wordFrequency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobWebsiteName() {
        return jobWebsiteName;
    }

    public void setJobWebsiteName(String jobWebsiteName) {
        this.jobWebsiteName = jobWebsiteName;
    }

    public String getJobWebsiteLink() {
        return jobWebsiteLink;
    }

    public void setJobWebsiteLink(String jobWebsiteLink) {
        this.jobWebsiteLink = jobWebsiteLink;
    }

    public int getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}