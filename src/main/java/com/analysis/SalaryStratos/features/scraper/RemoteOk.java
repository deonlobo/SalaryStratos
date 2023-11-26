package com.analysis.SalaryStratos.features.scraper;

import com.analysis.SalaryStratos.features.DataValidation;
import com.analysis.SalaryStratos.features.FetchAndUpdateData;
import com.analysis.SalaryStratos.models.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoteOk {
    static String JOBWEBSITENAME = "Remote Ok";


    public static void main(String[] args) throws InterruptedException {
        List<String> jobTitle = Arrays.asList(
                "Engineer", "Exec", "Senior", "Developer", "Finance", "Sys Admin", "JavaScript", "Backend", "Golang", "Cloud", "Medical", "Front End", "Full Stack", "Ops", "Design", "React", "InfoSec", "Marketing", "Mobile", "Content Writing", "SaaS", "Recruiter", "Full Time", "API", "Sales", "Ruby", "Education", "DevOps", "Stats", "Python", "Node", "English", "Non Tech", "Video", "Travel", "Quality Assurance", "Ecommerce", "Teaching", "Linux", "Java", "Crypto", "Junior", "Git", "Legal", "Android", "Accounting", "Admin", "Microsoft", "Excel", "PHP"
        );

        // Iterate through the jobTitle list using a for loop
        for (String title : jobTitle) {
            String websiteUrl= "https://remoteok.com/";
            ScaperBot bot = new ScaperBot();
            WebDriver scraperBot = bot.getScraperBot();
            WebDriverWait wait = bot.getScraperBotWithWait(scraperBot);

            scraperBot.get(websiteUrl);

            try {
                // Locate the input element using its class name
                WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("search")));
                // Enter "React" into the input field
                searchInput.sendKeys(title);
                Thread.sleep(2000);

                // Find the div element
                WebElement filterResultsDiv = scraperBot.findElement(By.className("search-filter-results"));
                // Check if the div is not empty
                if (filterResultsDiv.getText().trim().isEmpty()) {
                    System.out.println("this title is not there "+title);
                    // Close the browser window
                    scraperBot.quit();
                    continue;
                }

                // Press Enter using the Keys.ENTER constant from the Keys class
                searchInput.sendKeys(Keys.ENTER);
                Thread.sleep(2000);
                // Print the source code of the new website
                String pageSource = scraperBot.getPageSource();

                // Close the browser window
                scraperBot.quit();

                //System.out.println("Page Source:\n" + pageSource);
                List<Job> validatedJobList = jsoupExtractor(pageSource);

                //Append to json
                FetchAndUpdateData.appendListToJson(validatedJobList);
            }catch (NoSuchElementException | TimeoutException e) {
                // Close the browser window and continue to the next loop if it fails
                scraperBot.quit();
                continue;
            }


        }


    }

    public static List<Job> jsoupExtractor(String pageSource){
        List<Job> jobList = new ArrayList<>();
        Document document = Jsoup.parse(pageSource);
        // Select all tr elements
        Elements trElements = document.select("tr[id^='job-']");
        // Loop through each tr element
        for (Element trElement : trElements) {

            Job job = new Job();

            // Extract the title from the h2 element inside the tr
            String title = trElement.select("h2[itemprop=title]").text();
            job.setJobTitle(title);

            //Extract Company name
            String companyName = trElement.select("h3[itemprop=name]").text();
            job.setCompanyName(companyName);

            //Job website name
            job.setJobWebsiteName(JOBWEBSITENAME);

            // Extract the href from the a element inside the td with class "source"
            String jobWebsiteLink = "https://remoteok.com/" + trElement.select("td.source a").attr("href");
            job.setJobWebsiteLink(jobWebsiteLink);

            // Extract the location and salary information from the second div with class "location"
            Elements locationElements = trElement.select("div.location");
            for(Element locationElement: locationElements){
                // Extract the salary text
                String extracText = locationElement.text();
                if(extracText.contains("\uD83D\uDCB0")) {
                    // Define a regex pattern to match salary values
                    //Pattern pattern = Pattern.compile("\\$([\\d,]+)\\s?-\\s?\\$([\\d,]+)");
                    Pattern pattern = Pattern.compile("\\$(\\d+k)\\s?-\\s?\\$(\\d+k)");

                    Matcher matcher = pattern.matcher(extracText);

                    // Check if the pattern matches
                    if (matcher.find()) {
                        // Extract min and max salary values
                        String minSalary = matcher.group(1);
                        String maxSalary = matcher.group(2);
                        // Remove commas and print the values
                        minSalary = minSalary.replaceAll("k", "000");
                        maxSalary = maxSalary.replaceAll("k", "000");

                        try{
                            job.setMinSalary(Integer.parseInt(minSalary));
                            job.setMaxSalary(Integer.parseInt(maxSalary));
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Unable to parse salary values as integers.");
                        }
                    }
                }else{
                    //Set the location
                    // Define a regex pattern to match alphabets and spaces
                    Pattern pattern = Pattern.compile("[A-Za-z ]+");
                    // Create a matcher object
                    Matcher matcher = pattern.matcher(extracText);
                    // Check if the pattern matches
                    if (matcher.find()) {
                        // Extract the location value
                        String location = matcher.group(0);
                        location = location.replaceAll("^\\s+", "");
                        // Print the extracted location
                        if(Objects.isNull(job.getLocation()) || job.getLocation().isBlank())
                            job.setLocation(location);
                        else
                            job.setLocation(job.getLocation()+","+location);
                    }
                }

            }


            // Extract and print the data-id attribute value
            String dataId = trElement.attr("data-id");
            // Select the div.markdown element inside the tr element with data-id="504129"
            Element targetElement = document.select("tr.expand[data-id='"+dataId+"']").first();
            Element markdownElement = targetElement.select("div.markdown").first();
            // Check if the div.markdown element is found
            if (markdownElement != null) {
                // Extract and print the text content
                String markdownText = markdownElement.text();
                job.setJobDescription(markdownText);
            }

            job.setId(dataId);

            if(DataValidation.validateDataForOneObject(job)){
                jobList.add(job);
            }
        }

        return jobList;
    }
}
