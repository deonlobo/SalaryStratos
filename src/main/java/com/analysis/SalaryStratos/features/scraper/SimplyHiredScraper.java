package com.analysis.SalaryStratos.features.scraper;

import org.openqa.selenium.WebDriver;

public class SimplyHiredScraper {

    String websiteUrl = "www.simplyhired.com";

    public void scrapeWebPage(String searchTerm) {
        ScaperBot bot = new ScaperBot();
        WebDriver scraperBot = bot.getScraperBot();


        scraperBot.get(websiteUrl);

        scraperBot.findElement()

    }
}
