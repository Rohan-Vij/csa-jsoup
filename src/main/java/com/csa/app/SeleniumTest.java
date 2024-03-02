package com.csa.app;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumTest {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Rohan\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://example.com");

            System.out.println("Page title is: " + driver.getTitle());
        } finally {
            driver.quit();
        }
    }
}
