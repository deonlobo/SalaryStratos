package com.analysis.SalaryStratos.models;

public class JobValidation {
    private long id;
    private Boolean isIdValid;
    private Boolean isJobTitleValid;
    private Boolean isCompanyNameValid;
    private Boolean isLinkValid;
    private Boolean isSalaryValid;
    private Boolean isLocationValid;
    private Boolean isYearsExperienceValid;
    private Boolean isSkillsValid;
    private Boolean isJobDescriptionValid;
    private Boolean allFieldsValid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getIdValid() {
        return isIdValid;
    }

    public void setIdValid(Boolean idValid) {
        isIdValid = idValid;
    }

    public Boolean getJobTitleValid() {
        return isJobTitleValid;
    }

    public void setJobTitleValid(Boolean jobTitleValid) {
        isJobTitleValid = jobTitleValid;
    }

    public Boolean getCompanyNameValid() {
        return isCompanyNameValid;
    }

    public void setCompanyNameValid(Boolean companyNameValid) {
        isCompanyNameValid = companyNameValid;
    }

    public Boolean getLinkValid() {
        return isLinkValid;
    }

    public void setLinkValid(Boolean linkValid) {
        isLinkValid = linkValid;
    }

    public Boolean getSalaryValid() {
        return isSalaryValid;
    }

    public void setSalaryValid(Boolean salaryValid) {
        isSalaryValid = salaryValid;
    }

    public Boolean getLocationValid() {
        return isLocationValid;
    }

    public void setLocationValid(Boolean locationValid) {
        isLocationValid = locationValid;
    }

    public Boolean getYearsExperienceValid() {
        return isYearsExperienceValid;
    }

    public void setYearsExperienceValid(Boolean yearsExperienceValid) {
        isYearsExperienceValid = yearsExperienceValid;
    }

    public Boolean getSkillsValid() {
        return isSkillsValid;
    }

    public void setSkillsValid(Boolean skillsValid) {
        isSkillsValid = skillsValid;
    }

    public Boolean getJobDescriptionValid() {
        return isJobDescriptionValid;
    }

    public void setJobDescriptionValid(Boolean jobDescriptionValid) {
        isJobDescriptionValid = jobDescriptionValid;
    }

    public Boolean getAllFieldsValid() {
        return allFieldsValid;
    }

    public void setAllFieldsValid(Boolean allFieldsValid) {
        this.allFieldsValid = allFieldsValid;
    }
}