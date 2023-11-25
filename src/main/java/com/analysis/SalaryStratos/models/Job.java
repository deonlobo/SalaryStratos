package com.analysis.SalaryStratos.models;

public class Job {
    private int id;
    private String jobTitle;
    private String companyName;
    private String jobWebsiteName;
    private String jobWebsiteLink;
    private int minSalary;
    private int maxSalary;
    private String location;
    private String jobDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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