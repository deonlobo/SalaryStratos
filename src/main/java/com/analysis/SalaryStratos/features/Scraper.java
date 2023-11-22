package com.analysis.SalaryStratos.features;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

public class Scraper {


    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverManager.chromedriver().browserVersion("77.0.3865.40").setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("enable-automation");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        driver = new ChromeDriver(options);

        try {
            String url = "https://ca.indeed.com/m/jobs?q=data+analyst&l=Canada";
            driver.get(url);

            JSONArray data = new JSONArray();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Increase the wait time if needed
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            // Set the desired scroll height
            int scrollHeight = 500;

            // Scroll down and extract data one by one until the scroll height is reached
            int currentScrollHeight = 0;
            int counter = 0;
            while (currentScrollHeight < scrollHeight) {
                jsExecutor.executeScript("window.scrollTo(0, arguments[0]);", currentScrollHeight);

                // Wait for the page to load
//                wait.until(ExpectedConditions.visibilityOf(getJobElement(1, driver)));
                By title = By.xpath("//div[@id='mosaic-jobResults']//div/ul/descendant::li/descendant::table/tbody/tr/td/div/h2/a/span");
                // Extract data from the job element
                wait.until(ExpectedConditions.presenceOfElementLocated(title));
                WebElement jobElement = driver.findElement(title);
                String text = jobElement.getText();

                // Process the scraped data as needed
                JSONObject jobData = new JSONObject();
                jobData.put("job_info", text);
                data.put(jobData);

                // Update the current scroll height
                currentScrollHeight += 50; // You can adjust this value based on your needs
                counter++;
            }

            // Save data to a JSON file
            saveToJsonFile(data, "src/main/resources/output.json");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            driver.quit();
        }
    }

//    public static WebElement getJobElement(int i, WebDriver driver, WebDriverWait wait) {
//        // Use this method to get the job element by index
//    }

    private static void saveToJsonFile(JSONArray data, String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            JSONObject result = new JSONObject();
            result.put("jobs", data);

            // Write the JSON object to a file
            fileWriter.write(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
