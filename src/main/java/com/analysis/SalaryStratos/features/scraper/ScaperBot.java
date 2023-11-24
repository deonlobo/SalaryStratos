package com.analysis.SalaryStratos.features.scraper;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ScaperBot {
    WebDriver bot = null;


    private void initializeScraperBot() {
        WebDriverManager.chromedriver().browserVersion("77.0.3865.40").setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("enable-automation");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        this.bot = new ChromeDriver(options);
    }

    public WebDriver getScraperBot() {
        if (bot == null) {
            initializeScraperBot();
        }
        return  bot;
    }

    public WebDriverWait getScraperBotWithWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }
}
