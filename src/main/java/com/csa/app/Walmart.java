package com.csa.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class Walmart {
    private final static String URL = "https://www.walmart.com/reviews/product/";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Rohan\\chromedriver.exe");

        String itemId = "344772415";

        WebDriver driver = new ChromeDriver();

        try {
            driver.get(URL + itemId + "?sort=relevancy");

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

            System.out.println("URL: " + driver.getCurrentUrl());

            String nextXPath = "/html/body/div/div[1]/div/div/div/div/main/nav/ul/li[8]/a";
            WebElement nextButton = driver.findElement(By.xpath(nextXPath));
            nextButton.click();

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

            System.out.println("URL: " + driver.getCurrentUrl());

            String source = driver.getPageSource();
            Document doc = Jsoup.parse(source);

            Element reviewsList = doc.select("ul.cc-3").first();
            Elements reviews = reviewsList.select("li.dib");

            for (Element review : reviews) {
                char rating = review.select("span.w_iUH7").text().charAt(0);

                boolean verified = false;

                String date = review.select("div.f7").first().text();
                if (date.equals("Verified Purchase")) {
                    date = review.select("div.f7").get(1).text();
                    verified = true;
                }

                Elements contentAndAuthor = review.select("div.f6");
                String content = contentAndAuthor.first().text();
                String author = contentAndAuthor.last().text();

                System.out.println("----------");
                System.out.println("Rating: " + rating);
                System.out.println("Date: " + date);
                System.out.println("Content: " + content);
                System.out.println("Author: " + author);
                System.out.println("Verified: " + verified);
            }

        } finally {
            driver.quit();
        }
    }
}
