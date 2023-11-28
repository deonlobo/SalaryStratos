package com.analysis.SalaryStratos.features.scraper;

import com.analysis.SalaryStratos.dataStructures.trie.TrieDS;
import com.analysis.SalaryStratos.models.Job;
import com.analysis.SalaryStratos.models.Jobs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class ScraperBot {
    private static ScraperBot bot = null;
    private WebDriver driver = null;
    private Queue<String> jobLinksQueue;



    private ScraperBot() {
            initializeScraperBot();
    }


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
        this.driver = new ChromeDriver(options);
//        this.jobsDataTrie = new TrieDS();
        this.jobLinksQueue = new LinkedList<>();

    }

    public static ScraperBot getScraperBot() {
        if (bot == null) {
            bot = new ScraperBot();
        }
        return bot;
    }

    public  WebDriver getDriver() {
        return  this.driver;
    }

    public  Queue<String> getJobLinksQueue() {
        return  this.jobLinksQueue;
    }

    public void saveToJson(Collection<Job> jobs) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Jobs jobsList = new Jobs();
        jobsList.setJobs(jobs);
        // Convert the userData object to a JSON string
        String json = gson.toJson(jobsList);

        String path = "src/database.json";

        try (FileWriter writer = new FileWriter(path)) {
            // Attempt to write the JSON string to the specified file
            try (PrintWriter printWriter = new PrintWriter(writer)) {
                printWriter.println(json);
            }
        } catch (IOException e) {
            System.out.println("Error While Saving.");
        }
    }

    public WebDriverWait getScraperBotWithWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }
}
