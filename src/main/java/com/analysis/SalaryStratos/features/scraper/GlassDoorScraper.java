package com.analysis.SalaryStratos.features.scraper;

import com.analysis.SalaryStratos.models.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

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

            scraperBotWithWait
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='JAModal']")));
            WebElement modelElement = scraperBot.findElement(By.xpath("//div[@id='JAModal']"));


            ((JavascriptExecutor) scraperBot).executeScript("arguments[0].style.display='none';", modelElement);

            Thread.sleep(1000);

            String pageSource = scraperBot.getPageSource();
            liCount = scrapJobLinks(pageSource, liCount);

            System.out.println("LIcount:" + liCount);
            while (jobLinksQueue.size() < 100) {
                System.out.println(jobLinksQueue.size());
                try {

                    scraperBot
                            .findElement(By.xpath("//button[span/span[contains(text(), 'Show more jobs')]]"))
                            .click();
                    scraperBotWithWait
                            .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@class='JobsList_jobsList__Ey2Vo']>li[" + (liCount) + "]")));
                    pageSource = scraperBot.getPageSource();
                    liCount = scrapJobLinks(pageSource, liCount);
                } catch (NoSuchElementException e) {
                    System.out.println("Error while getting links: " + e);
                    continue;
                }
            }

            for(String link: jobLinksQueue) {
                System.out.println(link);
            }


        }


//        String pageSource = scraperBot.getPageSource();
//        scrapeWebPage(pageSource);

    }

    public int scrapJobLinks(String pageSource, int liCount) {
        Document pageDoc = Jsoup.parse(pageSource);
        Elements liElements = pageDoc.select("ul[class=JobsList_jobsList__Ey2Vo]>li");
        List<Element> updatedLiList = liElements.subList(liCount, liElements.size());
        for(Element liElement: updatedLiList) {
            String jobWebsiteId = liElement.attr("data-jobid");
            String jobWebsiteLink = "https://www.glassdoor.ca/job-listing?jl=" + jobWebsiteId;
            jobLinksQueue.add(jobWebsiteLink);
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

    public void scrapeWebPage(String pageSource) {
        Collection<Job> job = new ArrayList<Job>();
        Document pageDoc = Jsoup.parse(pageSource);
        Elements liElements = pageDoc.select("[class=]>li");
        int liCount = liElements.size();
        System.out.println("liCOunt: " + liCount);
        for (int liIterator = 0; liIterator < liCount; liIterator++) {
            Element liElement = liElements.get(liIterator);
            String jobTitle = liElement.select("h2[data-testid=searchSerpJobTitle]").text();
            String jobCompanyName = liElement.select("[data-testid=searchSerpJobLocation]").text();
            String jobWebsiteName = "SimplyHired";
            String jobWebsiteLink = liElement.select("h2[data-testid=searchSerpJobTitle] a").attr("href");

            String jobSalary = liElement.select("p[data-testid=searchSerpJobSalaryEst]").text();
            if(jobSalary == null) {
                jobSalary = liElement.select("p[data-testid=searchSerpJobSalaryConfirmed]").text();
            }
            int minSalary = 0;
            int maxSalary = 0;
            String regexYearly = "\\$([\\d.]+)K - \\$([\\d.]+)K a year";
            String regexHourly = "\\$([\\d.]+) - \\$([\\d.]+) an hour";
            String regexHourlyFrom = "From \\\\$([\\\\d.]+) an hour";
            String regexYearFrom = "\\$([\\d,]+) a year";
            if (jobSalary.matches(regexYearly)) {
                String lowerSalaryStr = jobSalary.replaceAll(regexYearly, "$1");
                String upperSalaryStr = jobSalary.replaceAll(regexYearly, "$2");

                minSalary = (int) (Double.parseDouble(lowerSalaryStr) * 1000);
                maxSalary = (int) (Double.parseDouble(upperSalaryStr) * 1000);
            } else if (jobSalary.matches(regexHourly)) {
                String lowerSalaryStr = jobSalary.replaceAll(regexHourly, "$1");
                String upperSalaryStr = jobSalary.replaceAll(regexHourly, "$2");

                minSalary = (int) (Double.parseDouble(lowerSalaryStr)*40*20*12);
                maxSalary = (int) (Double.parseDouble(upperSalaryStr)*40*20*12);
            } else if (jobSalary.matches(regexHourlyFrom)) {
                String lowerSalaryStr = jobSalary.replaceAll(regexHourlyFrom, "$1");

                // Convert to an integer
                maxSalary = (int) (Double.parseDouble(lowerSalaryStr)*40*20*12);
            } else if (jobSalary.matches(regexYearFrom)) {
                String salaryStr = jobSalary.replaceAll(regexYearFrom, "$1").replace(",", "");

                // Convert to an integer
                maxSalary = Integer.parseInt(salaryStr);
            } else {
                minSalary = 0;
                maxSalary = 0;
            }

            String jobDescription = liElement.select("p[data-testid=searchSerpJobSnippet]").text();

            System.out.println(jobTitle + ", " + jobCompanyName + ", " + jobWebsiteLink + ", " + minSalary + ", " + maxSalary  + ", " + jobDescription);


        }
    }

    public static void main(String[] args) throws InterruptedException {
        GlassDoorScraper scraper = new GlassDoorScraper();
        scraper.crawlWebPage(new String[]{"react"});
    }
}
