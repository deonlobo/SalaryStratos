package com.analysis.SalaryStratos.Data;

import lombok.Data;

@Data
public class Job {
    private long id;
    private String jobTitle;
    private String companyName;
    private String link;
    private int salary;
    private String location;
    private String jobWebsiteName;
    private String industry;
    private int yearsExperience;
    private String educationLevel;
    private String skills;
    private String jobDescription;
}