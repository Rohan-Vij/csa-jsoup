package com.csa.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVWriter;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

public class Walmart {
    private final static String URL = "https://www.walmart.com/reviews/product/";
    private final static String itemId = "117263923";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Rohan\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless=new");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
        WebDriver driver = new ChromeDriver(options);

        ReviewList reviewList = new ReviewList(itemId);

        try {
            driver.get(URL + itemId + "?sort=relevancy");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

            System.out.println("URL: " + driver.getCurrentUrl());

            int maxPages = Integer.parseInt(driver.findElement(By.xpath("/html/body/div/div[1]/div/div/div/div/main/nav/ul/li[7]/a")).getText());

            for (int i = 1; i <= maxPages; i++) {
                System.out.println("Page " + i + " of " + maxPages);
                System.out.println("URL: " + driver.getCurrentUrl());
                String source = driver.getPageSource();
                ArrayList<Review> reviews = getReviewsFromSource(source);

                for (Review review : reviews) {
                    System.out.println(review);
                    reviewList.addReview(review);
                }

                // nextPageButton(driver); // does not pass Walmart bot detection
                nextPageURL(driver, i);

                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
            }

            // Write to CSV
            String csvFilename = "./src/main/resources/" + itemId + ".csv";
            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilename))) {
                String[] header = { "Rating", "Comment", "Author", "Date", "Verified?" };
                writer.writeNext(header);

                for (Review review : reviewList.getReviews()) {
                    String[] entries = {
                        String.valueOf(review.getRating()),
                        review.getContent(),
                        review.getAuthor(),
                        review.getDate().toString(),
                        review.isVerified() ? "Verified" : "Unverified"
                    };
                    writer.writeNext(entries);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } finally {
            driver.quit();
        }
    }

    private static ArrayList<Review> getReviewsFromSource(String source) {
        ArrayList<Review> reviewsObjectList = new ArrayList<>();
        Document doc = Jsoup.parse(source);

        Element reviewsList = doc.select("ul.cc-3").first();
        Elements reviews = reviewsList.select("li.dib");

        for (Element review : reviews) {
            int rating = Character.getNumericValue(review.select("span.w_iUH7").text().charAt(0));

            boolean verified = false;

            String date = review.select("div.f7").first().text();
            if (date.equals("Verified Purchase")) {
                date = review.select("div.f7").get(1).text();
                verified = true;
            }

            Elements contentAndAuthor = review.select("div.f6");
            String content = contentAndAuthor.first().text();
            String author = contentAndAuthor.last().text();

            Review reviewObject = new Review(rating, verified, date, content, author);

            reviewsObjectList.add(reviewObject);
        }

        return reviewsObjectList;
    }

    private static void nextPageButton(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[aria-label='Next Page']")));
        nextButton.click();
    }

    private static void nextPageURL(WebDriver driver, int page) {
        String newURL = URL + itemId + "?sort=relevancy " + "&page=" + page;
        driver.get(newURL);
    }
}
