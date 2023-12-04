package com.analysis.SalaryStratos.features.scraper;

import com.analysis.SalaryStratos.features.DataValidation;
import com.analysis.SalaryStratos.models.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimplyHiredScraper {

    String websiteUrl = "https://www.simplyhired.com";

    public void crawlWebPage(String[] searchTerms) {
        ScraperBot bot = ScraperBot.getScraperBot();
        WebDriver scraperBot = bot.getDriver();
        Queue<String> jobLinksQueue = bot.getJobLinksQueue();
        WebDriverWait scraperBotWithWait = bot.getScraperBotWithWait(scraperBot);
        Set<String> uniqueJobs = new HashSet<>();

        for(String searchTerm: searchTerms) {
            scraperBot.get(websiteUrl + "/search?q=" + searchTerm);
            String pageSource = scraperBot.getPageSource();
            scrapJobLinks(pageSource, jobLinksQueue);
            while (jobLinksQueue.size() < 100) {
                try {
                    String nextLink = scraperBot
                            .findElement(By.xpath("//nav[@data-testid='pageNumberContainer']//span[@aria-current='true']"))
                            .findElement(By.xpath("following-sibling::*"))
                            .getAttribute("href");
                    scraperBot.get(nextLink);
                    scraperBotWithWait
                            .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='job-list']")));
                    pageSource = scraperBot.getPageSource();
                    scrapJobLinks(pageSource, jobLinksQueue);
                } catch (NoSuchElementException e) {
                    System.out.println("Error while getting links: " + e);
                    continue;
                }

            }
            Collection<Job> jobsCollection = new ArrayList<>();

            for (String jobLink: jobLinksQueue) {
                scraperBot.get(jobLink);
                String jobPageSource = scraperBot.getPageSource();

                Job job = scrapeJobData(jobPageSource, jobLink);


                //Add Only the validated data to the jobList
                if(DataValidation.validateDataForOneObject(job) && !uniqueJobs.contains(job.getId())){
                    uniqueJobs.add(job.getId());
                    jobsCollection.add(job);
                }
            }

            bot.saveAndAppendToJson(jobsCollection);
        }


        scraperBot.close();


    }

    public void scrapJobLinks(String pageSource, Queue<String> jobLinksQueue) {
        Document pageDoc = Jsoup.parse(pageSource);
        Elements liElements = pageDoc.select("[id=job-list]>li");
        for(Element liElement: liElements) {
            String jobWebsiteLink = liElement.select("h2[data-testid=searchSerpJobTitle] a").attr("href");
            jobLinksQueue.add(websiteUrl + jobWebsiteLink);
        }
    }

    public Job scrapeJobData(String pageSource, String jobLink) {
        Document pageDoc = Jsoup.parse(pageSource);
        String jobId = jobLink.replace("https://www.simplyhired.com/job/", "");
        String jobTitle = pageDoc.select("h1[data-testid=viewJobTitle]").text();
        String jobCompanyName = pageDoc.select("span[data-testid=viewJobCompanyName]>span>span:nth-child(1)").text();
        String jobCompanyLocation = pageDoc.select("span[data-testid=viewJobCompanyLocation]>span>span:nth-child(1)").text();
        String jobWebsiteName = "SimplyHired";

        String jobSalary = pageDoc.select("span[data-testid=viewJobBodyJobCompensation]>span>span:nth-child(1)").text();
        jobSalary = jobSalary.replace("Estimated: ", "");
        int minSalary = 0;
        int maxSalary = 0;
        int indexOfDollar = jobSalary.indexOf("$");
        if(indexOfDollar > -1) {
            jobSalary = jobSalary.substring(indexOfDollar);
        }
        String regexYearlyWithK = "\\$([\\d.]+)K - \\$([\\d.]+)K a year";
        String regexYearlyFrom = "\\$([\\d,]+) a year";
        String regexWeeklyWithoutK = "\\$([\\d,]+) - \\$([\\d,]+) a week";
        String regexMonthlyWithoutK = "\\$([\\d,]+) - \\$([\\d,]+) a month";
        String regexYearlyWithoutK = "\\$([\\d,]+) - \\$([\\d,]+) a year";
        String regexHourly = "\\$([\\d.]+) - \\$([\\d.]+) an hour";
        String regexHourlyNonDecimal = "\\$([\\d]+) - \\$([\\d]+) an hour";
        String regexHourlyFrom = "\\$([\\d.]+) an hour";
        String regexDaily = "\\$([\\d.]+) a day";
        String regexWeekMax = "\\$([\\d,]+) a week";
        String regexYearFrom = "\\$([\\d,]+) a year";

        if (jobSalary.matches(regexDaily)) {
            String upperSalaryStr = jobSalary.replaceAll(regexDaily, "$1");

            maxSalary = (int) (Double.parseDouble(upperSalaryStr) * 20 * 12);
        }
        else if (jobSalary.matches(regexWeekMax)) {
            String upperSalaryStr = jobSalary.replaceAll(regexWeekMax, "$1");
            upperSalaryStr = upperSalaryStr.replace(",", "");

            maxSalary = (int) (Double.parseDouble(upperSalaryStr) * 3 * 12);
        }
        else if (jobSalary.matches(regexYearlyWithK)) {
            String lowerSalaryStr = jobSalary.replaceAll(regexYearlyWithK, "$1");
            String upperSalaryStr = jobSalary.replaceAll(regexYearlyWithK, "$2");

            minSalary = (int) (Double.parseDouble(lowerSalaryStr) * 1000);
            maxSalary = (int) (Double.parseDouble(upperSalaryStr) * 1000);
        } else if (jobSalary.matches(regexYearlyFrom)) {
            String upperSalaryStr = jobSalary.replaceAll(regexYearlyFrom, "$1");
            upperSalaryStr = upperSalaryStr.replace(",", "");

            maxSalary = (int) (Double.parseDouble(upperSalaryStr));
        } else if (jobSalary.matches(regexYearlyWithoutK)) {
                String lowerSalaryStr = jobSalary.replaceAll(regexYearlyWithoutK, "$1");
                lowerSalaryStr = lowerSalaryStr.replace(",", "");

                String upperSalaryStr = jobSalary.replaceAll(regexYearlyWithoutK, "$2");
                upperSalaryStr = upperSalaryStr.replace(",", "");


                minSalary = (int) (Double.parseDouble(lowerSalaryStr));
                maxSalary = (int) (Double.parseDouble(upperSalaryStr));
        } else if (jobSalary.matches(regexMonthlyWithoutK)) {
            String lowerSalaryStr = jobSalary.replaceAll(regexMonthlyWithoutK, "$1");
            lowerSalaryStr = lowerSalaryStr.replace(",", "");

            String upperSalaryStr = jobSalary.replaceAll(regexMonthlyWithoutK, "$2");
            upperSalaryStr = upperSalaryStr.replace(",", "");
            minSalary = (int) (Double.parseDouble(lowerSalaryStr) * 12);
            maxSalary = (int) (Double.parseDouble(upperSalaryStr) * 12);
        } else if (jobSalary.matches(regexWeeklyWithoutK)) {
            String lowerSalaryStr = jobSalary.replaceAll(regexWeeklyWithoutK, "$1");
            lowerSalaryStr = lowerSalaryStr.replace(",", "");

            String upperSalaryStr = jobSalary.replaceAll(regexWeeklyWithoutK, "$2");
            upperSalaryStr = upperSalaryStr.replace(",", "");
            minSalary = (int) (Double.parseDouble(lowerSalaryStr) * 3 * 12);
            maxSalary = (int) (Double.parseDouble(upperSalaryStr) * 3 * 12);
        }
         else if (jobSalary.matches(regexHourly)) {
            String lowerSalaryStr = jobSalary.replaceAll(regexHourly, "$1");
            String upperSalaryStr = jobSalary.replaceAll(regexHourly, "$2");

            minSalary = (int) (Double.parseDouble(lowerSalaryStr)*40*4*12);
            maxSalary = (int) (Double.parseDouble(upperSalaryStr)*40*4*12);
        } else if (jobSalary.matches(regexHourlyNonDecimal)) {
            String lowerSalaryStr = jobSalary.replaceAll(regexHourlyNonDecimal, "$1");
            String upperSalaryStr = jobSalary.replaceAll(regexHourlyNonDecimal, "$2");

            minSalary = (int) (Double.parseDouble(lowerSalaryStr)*40*4*12);
            maxSalary = (int) (Double.parseDouble(upperSalaryStr)*40*4*12);
        } else if (jobSalary.matches(regexHourlyFrom)) {
            String lowerSalaryStr = jobSalary.replaceAll(regexHourlyFrom, "$1");
            // Convert to an integer
            maxSalary = (int) (Double.parseDouble(lowerSalaryStr)*40*4*12);


        } else if (jobSalary.matches(regexYearFrom)) {
            String salaryStr = jobSalary.replaceAll(regexYearFrom, "$1").replace(",", "");

            // Convert to an integer
            maxSalary = Integer.parseInt(salaryStr);
        } else {
            System.out.println("Failed For:" + jobSalary);
        }

        String jobDescription = pageDoc.select("div[data-testid=viewJobBodyJobFullDescriptionContent]").text();

        Job job = new Job();
        job.setId(jobId);
        job.setJobTitle(jobTitle);
        job.setJobWebsiteName(jobWebsiteName);
        job.setCompanyName(jobCompanyName);
        job.setLocation(jobCompanyLocation);
        job.setJobDescription(jobDescription);
        job.setJobWebsiteLink(jobLink);
        job.setMinSalary(minSalary);
        job.setMaxSalary(maxSalary);

        return  job;
    }

    public static void main(String[] args) {
        SimplyHiredScraper scraper = new SimplyHiredScraper();
        scraper.crawlWebPage(new String[]{"react"});
    }
}
