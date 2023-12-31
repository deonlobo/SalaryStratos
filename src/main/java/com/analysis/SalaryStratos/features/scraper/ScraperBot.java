package com.analysis.SalaryStratos.features.scraper;

import com.analysis.SalaryStratos.models.Job;
import com.analysis.SalaryStratos.models.Jobs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;
import java.util.*;

public class ScraperBot {
    private static ScraperBot bot = null;
    private WebDriver driver = null;
    private Queue<String> jobLinksQueue;



    private ScraperBot() {
            initializeScraperBot();
    }


    private void initializeScraperBot() {
        WebDriverManager.chromedriver().browserVersion("119.0.6045.200").setup();
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
            System.out.println("Bot loaded for first time");
            bot = new ScraperBot();
        }
        System.out.println("Bot loaded from already created instance");
        return bot;
    }

    public  WebDriver getDriver() {
        return  this.driver;
    }

    public  Queue<String> getJobLinksQueue() {
        return  this.jobLinksQueue;
    }

    public WebDriverWait getScraperBotWithWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void saveAndAppendToJson(Collection<Job> jobs) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Jobs jobsList = new Jobs();

        String path = "src/main/resources/database.json";

        try {
            // Attempt to read existing data from the file
            Collection<Job> existingJobs = readJsonFile(path);
            // Append new data to existing data
            existingJobs.addAll(jobs);
            jobsList.setJobs(existingJobs);
            // Convert the combined list to JSON
            String updatedJson = gson.toJson(jobsList);
            System.out.println("Size after inserting jobs :"+existingJobs.size());


            // Write the updated JSON string to the file
            try (FileWriter writer = new FileWriter(path)) {
                try (PrintWriter printWriter = new PrintWriter(writer)) {
                    printWriter.println(updatedJson);
                }
            }

        } catch (IOException e) {
            System.out.println("Error While Saving.");
        }
    }

    private Collection<Job> readJsonFile(String path) throws IOException {
        Gson gson = new Gson();
        Collection<Job> existingJobs;

        try (Reader reader = new FileReader(path)) {
            // Read the existing data from the file
            existingJobs = gson.fromJson(new FileReader("src/main/resources/database.json"), Jobs.class).getJobs();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            // File doesn't exist, initialize an empty list
            existingJobs = new ArrayList<>();
        }

        return existingJobs;
    }
}
