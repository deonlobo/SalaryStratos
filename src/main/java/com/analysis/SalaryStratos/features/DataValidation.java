package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.Data.JobValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

@Service
public class DataValidation {
    @Autowired
    private final FetchData jobService;

    public DataValidation(FetchData jobService) {
        this.jobService = jobService;
    }

    //Data validation using regular expression
    public List<JobValidation> validateScrapedData(){
        List<JobValidation> jobValidationList = new ArrayList<>();
        //Validate the ID field, ensuring it's a positive integer.
        String idRegex = "^[1-9]\\d*$";
        //Make sure that they contain only letters and spaces.
        String jobTitleRegex = "^[A-Za-z\\s]+$";
        //Validate that company names consist of letters, spaces, &, ,
        String companyNameRegex = "^[A-Za-z\\s&,-]+$";
        //Validate Link of the website
        String linkRegex = "^(https?|ftp)://[\\w-]+(.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$";
        //salary format ensure it's a positive integer
        String salaryRegex = "^[1-9]\\d*$";
        //Ensure that locations contain anything other than !@#$%^&*+={}[]|\/'"?<>
        String locationRegex = "^[^!@#$%^&*+={}\\[\\]|\\\\/'\"?<>]+$";
        //Experience format ensure it's > 0
        String experienceRegex = "^[0-9]\\d*$";
        //Ensure that skills contain anything other than !@#$%^&*+={}[]|\/'"?<>
        String skillsRegex = "^[^!@#$%^&*+={}\\[\\]|\\\\/'\"?<>]+$";
        //Ensure there are at least 10 words (one or more non-whitespace characters followed by zero or more whitespace characters){10,}
        String descriptionRegex = "(\\S+\\s*){10,}";

        jobService.readJsonData().forEach(job->{
            JobValidation jobValidation = new JobValidation();
            AtomicBoolean allValid = new AtomicBoolean(true);
            jobValidation.setId(job.getId());

            //Validate id
            if(Objects.nonNull(job.getId())){
                jobValidation.setIsIdValid(validateField(String.valueOf(job.getId()),idRegex,allValid));
            }else{
                jobValidation.setIsIdValid(Boolean.FALSE);
            }

            //Validate Job Title
            jobValidation.setIsJobTitleValid(validateField(job.getJobTitle(),jobTitleRegex, allValid));

            //Validate Company Name
            jobValidation.setIsCompanyNameValid(validateField(job.getCompanyName(),companyNameRegex, allValid));

            //Validate Link
            jobValidation.setIsLinkValid(validateField(job.getLink(),linkRegex, allValid));

            //Validate Salary
            if(Objects.nonNull(job.getSalary())){
                jobValidation.setIsSalaryValid(validateField(String.valueOf(job.getSalary()),salaryRegex,allValid));
            }else{
                jobValidation.setIsSalaryValid(Boolean.FALSE);
            }

            //Validate Location
            jobValidation.setIsLocationValid(validateField(job.getLocation(),locationRegex, allValid));

            //Validate Experience
            if(Objects.nonNull(job.getYearsExperience())){
                jobValidation.setIsYearsExperienceValid(validateField(String.valueOf(job.getYearsExperience()),experienceRegex,allValid));
            }else{
                jobValidation.setIsYearsExperienceValid(Boolean.FALSE);
            }

            //Validate Skills
            jobValidation.setIsSkillsValid(validateField(job.getSkills(),skillsRegex, allValid));

            //Validate Description
            jobValidation.setIsJobDescriptionValid(validateField(job.getJobDescription(),descriptionRegex, allValid));

            jobValidation.setAllFieldsValid(allValid.get());
            jobValidationList.add(jobValidation);

        });
        return jobValidationList;
    }

    // Generic validation method for fields using regex
    private Boolean validateField(String field, String regex, AtomicBoolean allValid) {
        if(Objects.nonNull(field) && !field.isBlank()) {
            Boolean patternMatched = Pattern.matches(regex, field);
            //If field is not valid then set all fields valid as false
            if(!patternMatched)
                allValid.set(Boolean.FALSE);
            return patternMatched;
        }else {
            allValid.set(Boolean.FALSE);
            return Boolean.FALSE;
        }
    }
}
