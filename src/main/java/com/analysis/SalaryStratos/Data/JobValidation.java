package com.analysis.SalaryStratos.Data;

import lombok.Data;

@Data
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
}