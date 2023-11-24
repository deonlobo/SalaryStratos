package com.analysis.SalaryStratos.models;

public class JobValidation {
    private long id;
    private Boolean jobTitle;
    private Boolean companyName;
    private Boolean jobWebsiteName;
    private Boolean jobWebsiteLink;
    private Boolean minSalary;
    private Boolean maxSalary;
    private Boolean location;
    private Boolean jobDescription;

    private Boolean allFieldsValid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(Boolean jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Boolean getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Boolean companyName) {
        this.companyName = companyName;
    }

    public Boolean getJobWebsiteName() {
        return jobWebsiteName;
    }

    public void setJobWebsiteName(Boolean jobWebsiteName) {
        this.jobWebsiteName = jobWebsiteName;
    }

    public Boolean getJobWebsiteLink() {
        return jobWebsiteLink;
    }

    public void setJobWebsiteLink(Boolean jobWebsiteLink) {
        this.jobWebsiteLink = jobWebsiteLink;
    }

    public Boolean getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Boolean minSalary) {
        this.minSalary = minSalary;
    }

    public Boolean getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Boolean maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Boolean getLocation() {
        return location;
    }

    public void setLocation(Boolean location) {
        this.location = location;
    }

    public Boolean getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(Boolean jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Boolean getAllFieldsValid() {
        return allFieldsValid;
    }

    public void setAllFieldsValid(Boolean allFieldsValid) {
        this.allFieldsValid = allFieldsValid;
    }
}