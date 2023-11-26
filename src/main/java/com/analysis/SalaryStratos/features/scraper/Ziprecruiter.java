package com.analysis.SalaryStratos.features.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.LinkedList;
import java.util.Queue;

public class Ziprecruiter {
    String websiteUrl = "https://www.ziprecruiter.com";
    Queue<String> jobLinksQueue = new LinkedList<>();

    public void crawlWebPage(String[] searchTerms) throws InterruptedException {
        ScaperBot bot = new ScaperBot();
        WebDriver scraperBot = bot.getScraperBot();
        WebDriverWait scraperBotWithWait = bot.getScraperBotWithWait(scraperBot);

        loginToZiprecruiter(scraperBot);

        for(String searchTerm: searchTerms) {
            scraperBot.get(websiteUrl + "/jobs-search?search=" + searchTerm);
            String pageSource = scraperBot.getPageSource();
            scrapJobLinks(pageSource);
           /* while (jobLinksQueue.size() < 2) {
                System.out.println(jobLinksQueue.size());
                try {
                    String nextlink = scraperBot
                            .findElement(By.xpath("//nav[@data-testid='pageNumberContainer']//span[@aria-current='true']"))
                            .findElement(By.xpath("following-sibling::*"))
                            .getAttribute("href");
                    scraperBot.get(nextlink);
                    scraperBotWithWait
                            .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='job-list']")));
                    pageSource = scraperBot.getPageSource();
                    scrapJobLinks(pageSource);
                } catch (NoSuchElementException e) {
                    System.out.println("Error while getting links: " + e);
                    continue;
                }

            }*/

          /*  for (String jobLink: jobLinksQueue) {
                scraperBot.get(jobLink);
                String jobPageSource = scraperBot.getPageSource();
                Thread.sleep(3000);
                System.out.println(jobLink);
                //scrapeJobData(jobPageSource, jobLink);
            }*/
            System.out.println("\n\n");
        }


    }

    private void loginToZiprecruiter(WebDriver scraperBot) throws InterruptedException {
        scraperBot.get(websiteUrl);
        //Find the element by text content using XPath
        WebElement element = scraperBot.findElement(By.xpath("//*[text()='Log In']"));

        //Click on the element
        element.click();
        Thread.sleep(3000);

        // Find the email input and enter the email
        WebElement emailInput = scraperBot.findElement(By.id("email"));
        emailInput.sendKeys("testppxx99@gmail.com");

        // Find the password input and enter the password
        WebElement passwordInput = scraperBot.findElement(By.id("password"));
        passwordInput.sendKeys("Password123!@#");

        // Find the submit button and click it
        WebElement submitButton = scraperBot.findElement(By.id("submit_button"));
        submitButton.click();

        Thread.sleep(3000);
    }

    public void scrapJobLinks(String pageSource) {

        Document doc = Jsoup.parse(pageSource);

        Elements divs = doc.select("div.w-full");

        for (Element div : divs) {
            if (div.text().contains("Quick Apply")) {
                Element anchorTag = div.selectFirst("a[href^='https://www.ziprecruiter.com']");
                if (anchorTag != null) {
                    String hrefValue = anchorTag.attr("href");
                    jobLinksQueue.add(hrefValue);
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Ziprecruiter scraper = new Ziprecruiter();
        scraper.crawlWebPage(new String[]{"react","developer"});
    }
}
