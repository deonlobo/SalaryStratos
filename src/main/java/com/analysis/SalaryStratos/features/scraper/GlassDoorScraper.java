package com.analysis.SalaryStratos.features.scraper;

import com.analysis.SalaryStratos.models.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collection;

public class GlassDoorScraper {
    String websiteUrl = "https://www.glassdoor.com";

    public void crawlWebPage(String searchTerm) {
        ScaperBot bot = new ScaperBot();
        WebDriver scraperBot = bot.getScraperBot();
        WebDriverWait scraperBotWithWait = bot.getScraperBotWithWait(scraperBot);

        scraperBot.get(websiteUrl + "/search?q=" + searchTerm);

        String pageSource = scraperBot.getPageSource();
        scrapeWebPage(pageSource);

    }

    public void scrapeWebPage(String pageSource) {
        Collection<Job> job = new ArrayList<Job>();
        Document pageDoc = Jsoup.parse(pageSource);
        Elements liElements = pageDoc.select("[id=job-list]>li");
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

//    public static void main(String[] args) {
//        SimplyHiredScraper scraper = new SimplyHiredScraper();
//        scraper.crawlWebPage("react");
//    }
}
