package com.analysis.SalaryStratos.features.scraper;

import com.analysis.SalaryStratos.features.DataValidation;
import com.analysis.SalaryStratos.models.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GlassDoorScraper {
    String websiteUrl = "https://www.glassdoor.ca/index.htm";
    Queue<String> jobLinksQueue = new LinkedList<>();

    public void crawlWebPage(String[] searchTerms) throws InterruptedException {
        ScraperBot bot = ScraperBot.getScraperBot();
        WebDriver scraperBot = bot.getDriver();
        WebDriverWait scraperBotWithWait = bot.getScraperBotWithWait(scraperBot);

        scraperBot.get(websiteUrl);
        loginToGlassDoor(scraperBotWithWait, scraperBot);


        int liCount = 0;
        for(String searchTerm: searchTerms) {
            scraperBotWithWait
                            .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[a/@href='/Job/index.htm']")));
            String searchUrl = "https://www.glassdoor.ca/Job/" + searchTerm + "-jobs-SRCH_KO0,"+ searchTerm.length() +".htm";
            scraperBot.get(searchUrl);

            try {
                scraperBotWithWait
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='JAModal']")));
                WebElement modelElement = scraperBot.findElement(By.xpath("//div[@id='JAModal']"));


                ((JavascriptExecutor) scraperBot).executeScript("arguments[0].style.display='none';", modelElement);
            } catch (Exception e) {
                System.out.println("Popup Not Found");
            }


            Thread.sleep(1000);

            String pageSource = scraperBot.getPageSource();
            liCount = scrapJobLinks(pageSource, liCount);
            int sameSize = 0;
            int jobSize = 0;
            while (jobSize < 50) {
                try {

                    try{
                        scraperBotWithWait
                                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[span/span[contains(text(), 'Show more jobs')]]")));
                        WebElement nextButton = scraperBot
                                .findElement(By.xpath("//button[span/span[contains(text(), 'Show more jobs')]]"));

                        nextButton.click();
                    } catch (ElementClickInterceptedException e) {
                        System.out.println("Popup Found");

                        scraperBotWithWait
                                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='JAModal']")));
                        WebElement modelElement = scraperBot.findElement(By.xpath("//div[@id='JAModal']"));
                        ((JavascriptExecutor) scraperBot).executeScript("arguments[0].style.display='none';", modelElement);

                    }


                    Thread.sleep(5000);

                    pageSource = scraperBot.getPageSource();
                    Document pageDoc = Jsoup.parse(pageSource);

                    Elements liElements = pageDoc.select("ul[class=JobsList_jobsList__Ey2Vo]>li");

                    if(liElements.size() == jobSize) {
                        sameSize++;
                    }
                    jobSize = liElements.size();

                    if (sameSize == 3) {
                        System.out.println("Same after show more");
                        jobSize = 100;
                    }

                } catch (Exception e) {
                    System.out.println("Error while getting links: " + e);
                    jobSize = 100;
                    continue;
                }
            }
            Set<String> uniqueJobs = new HashSet<>();
            Collection<Job> jobsCollection = new ArrayList<>();
            pageSource = scraperBot.getPageSource();
            Document pageDoc = Jsoup.parse(pageSource);
            int jobCounter = 0;
            try {

                Elements liElements = pageDoc.select("ul[class=JobsList_jobsList__Ey2Vo]>li");

                for(Element li: liElements) {

                    String jobId = "";
                    try {



                        List<WebElement> lst =  scraperBot
                                .findElements(By.xpath("//ul[@class='JobsList_jobsList__Ey2Vo']/li"));
                        Thread.sleep(500);

                        try {
                            scraperBotWithWait
                                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='JAModal']")));
                            WebElement modelElement = scraperBot.findElement(By.xpath("//div[@id='JAModal']"));


                            ((JavascriptExecutor) scraperBot).executeScript("arguments[0].style.display='none';", modelElement);
                        } catch (Exception e) {
                            System.out.println("Popup Not Found");
                        }
                        Thread.sleep(500);

                        lst.get(jobCounter).click();

                        Thread.sleep(500);

                        pageSource = scraperBot.getPageSource();
                        pageDoc = Jsoup.parse(pageSource);
                        Elements aLink = li.select("a[class=JobCard_seoLink__WdqHZ]");
                        String jobLink = !aLink.isEmpty() ? aLink.get(0).attr("href"): "";
                        String jobIdRegex = "jl=(\\d+)";
                        Pattern jobIdPattern = Pattern.compile(jobIdRegex);

                        Matcher jobIdMatcher = jobIdPattern.matcher(jobLink);
                        ;
                        if (jobIdMatcher.find()) {
                            jobId = jobIdMatcher.group(1);
                        } else {
                            continue;
                        }

                        String jobTitle = li.select("a[class=JobCard_seoLink__WdqHZ]").text();

                        String jobCompanyName = li.select("span[class=EmployerProfile_employerName__Xemli]").text();

                        String jobCompanyLocation = li.select("div[class=JobCard_location__N_iYE]").text();

                        String jobWebsiteName = "GlassDoor";

                        String jobSalary = li.select("div[class=JobCard_salaryEstimate___m9kY]").text();

                        int minSalary = 0;
                        int maxSalary = 0;
                        String regexYearlyWithK = "\\$([\\d.]+)K - \\$([\\d.]+)K \\(Employer Est.\\)";
                        String regexYearlyWithK2 = "\\$([\\d.]+)K - \\$([\\d.]+)K \\(Glassdoor Est.\\)";
                        String regexYearlyWithK3 = "\\$([\\d.]+)K \\(Glassdoor Est.\\)";
                        String regexYearlyWithK4 = "\\$([\\d.]+)K \\(Employer Est.\\)";
                        String regexPerhour =  "\\$([\\d.]+) - \\$([\\d.]+) Per hour \\(Employer Est.\\)";
                        String regexPerhour2 =  "\\$([\\d.]+) - \\$([\\d.]+) Per hour \\(Glassdoor Est.\\)";

                        if (jobSalary.matches(regexYearlyWithK) || jobSalary.matches(regexYearlyWithK2)) {
                            if(jobSalary.matches(regexYearlyWithK)) {
                                String lowerSalaryStr = jobSalary.replaceAll(regexYearlyWithK, "$1");
                                String upperSalaryStr = jobSalary.replaceAll(regexYearlyWithK, "$2");

                                minSalary = (int) (Double.parseDouble(lowerSalaryStr) * 1000);
                                maxSalary = (int) (Double.parseDouble(upperSalaryStr) * 1000);
                            } else {
                                String lowerSalaryStr = jobSalary.replaceAll(regexYearlyWithK2, "$1");
                                String upperSalaryStr = jobSalary.replaceAll(regexYearlyWithK2, "$2");

                                minSalary = (int) (Double.parseDouble(lowerSalaryStr) * 1000);
                                maxSalary = (int) (Double.parseDouble(upperSalaryStr) * 1000);
                            }

                        } else if (jobSalary.matches(regexYearlyWithK3) || jobSalary.matches(regexYearlyWithK4)) {
                            if(jobSalary.matches(regexYearlyWithK3)) {
                                String lowerSalaryStr = jobSalary.replaceAll(regexYearlyWithK3, "$1");

                                minSalary = (int) (Double.parseDouble(lowerSalaryStr) * 1000);
                                maxSalary = (int) (Double.parseDouble(lowerSalaryStr) * 1000);
                            } else {
                                String lowerSalaryStr = jobSalary.replaceAll(regexYearlyWithK4, "$1");

                                minSalary = (int) (Double.parseDouble(lowerSalaryStr) * 1000);
                                maxSalary = (int) (Double.parseDouble(lowerSalaryStr) * 1000);
                            }

                        } else if (jobSalary.matches(regexPerhour) || jobSalary.matches(regexPerhour2)) {
                            if (jobSalary.matches(regexPerhour)) {
                                String lowerSalaryStr = jobSalary.replaceAll(regexPerhour, "$1");
                                String upperSalaryStr = jobSalary.replaceAll(regexPerhour, "$2");

                                minSalary = (int) (Double.parseDouble(lowerSalaryStr) * 40 * 4 * 12);
                                maxSalary = (int) (Double.parseDouble(upperSalaryStr) * 40 * 4 * 12);
                            } else {
                                String lowerSalaryStr = jobSalary.replaceAll(regexPerhour2, "$1");
                                String upperSalaryStr = jobSalary.replaceAll(regexPerhour2, "$2");

                                minSalary = (int) (Double.parseDouble(lowerSalaryStr) * 40 * 4 * 12);
                                maxSalary = (int) (Double.parseDouble(upperSalaryStr) * 40 * 4 * 12);
                            }

                        }

                        String jobDescription = pageDoc.select("div[class=JobDetails_jobDescription__6VeBn JobDetails_blurDescription__fRQYh]").text();



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


                        if(DataValidation.validateDataForOneObject(job) && !uniqueJobs.contains(job.getId())){
//                            System.out.println("Is valid Job");
                            uniqueJobs.add(job.getId());
                            jobsCollection.add(job);
                            System.out.println("Scrapped jobId : "+job.getId()+" successfully");
                        }
                        jobCounter++;
                    } catch (Exception e) {
                        jobCounter++;
                        e.printStackTrace();
                        //System.out.println("Failed for Job: " + jobId);
                    }

                }
            } catch (Exception e) {
                jobCounter++;
                e.printStackTrace();
            }
            bot.saveAndAppendToJson(jobsCollection);
        }
    }

    public int scrapJobLinks(String pageSource, int liCount) {
        Document pageDoc = Jsoup.parse(pageSource);
        Elements liElements = pageDoc.select("ul[class=JobsList_jobsList__Ey2Vo]>li");
        List<Element> updatedLiList = liElements.subList(liCount, liElements.size());
        for(Element liElement: updatedLiList) {
            Elements aLink = liElement.select("a[class=JobCard_seoLink__WdqHZ]");
            String link = !aLink.isEmpty() ? aLink.get(0).attr("href"): "";
            jobLinksQueue.add(link);
            liCount++;
        }
        return liCount;
    }
    public void loginToGlassDoor(WebDriverWait scraperBotWithWait, WebDriver scraperBot) {
        scraperBotWithWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='inlineUserEmail']")));

        WebElement emailInput = scraperBot.findElement(By.xpath("//input[@id='inlineUserEmail']"));
        emailInput.sendKeys("boyahox285@cumzle.com");
        emailInput.sendKeys(Keys.ENTER);

        scraperBotWithWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='inlineUserPassword']")));

        WebElement passwordInput = scraperBot.findElement(By.xpath("//input[@id='inlineUserPassword']"));
        passwordInput.sendKeys("Test@123");
        passwordInput.sendKeys(Keys.ENTER);
    }

    public static void main(String[] args) throws InterruptedException {
        GlassDoorScraper scraper = new GlassDoorScraper();
        scraper.crawlWebPage(new String[]{"react"});
    }
}
