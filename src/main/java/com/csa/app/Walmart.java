package com.csa.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.time.Duration;
import java.util.ArrayList;

public class Walmart {
    private final static String URL = "https://www.walmart.com/reviews/product/";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Rohan\\chromedriver.exe");

        String itemId = "117263923";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(URL + itemId + "?sort=relevancy");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

            System.out.println("URL: " + driver.getCurrentUrl());

            int maxPages = Integer.parseInt(driver.findElement(By.xpath("/html/body/div/div[1]/div/div/div/div/main/nav/ul/li[7]/a")).getText());

            for (int i = 0; i < maxPages; i++) {
                System.out.println("Page " + (i + 1) + " of " + maxPages);
                System.out.println("URL: " + driver.getCurrentUrl());
                String source = driver.getPageSource();
                ArrayList<Review> reviews = getReviewsFromSource(source);

                for (Review review : reviews) {
                    System.out.println(review);
                }


                // replaced nextPage with this to make sure that passing in the driver isn't interfering with anything - but it still doesn't work
                WebElement nextButton = driver.findElement(By.cssSelector("a[aria-label='Next Page']"));
                nextButton.click();

                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
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

    private static void nextPage(WebDriver driver) {
        WebElement nextButton = driver.findElement(By.cssSelector("a[aria-label='Next Page']"));
        nextButton.click();
    }
}
