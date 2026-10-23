package com.automation.selenium;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GoogleLocatorTest {

    public static void main(String[] args) {
    	
        WebDriver driver = new ChromeDriver();

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            driver.manage().window().maximize();
            driver.get("https://www.google.com/");

            // Wait for the search box to appear
            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.name("q")
                )
            );

            // 1. ID
			/*
			 * WebElement searchById = driver.findElement(By.id("snbc"));
			 * verifyNotNull(searchById);
			 */

            // 2. CSS selector
            WebElement searchByCss =
                    driver.findElement(
                        By.cssSelector("textarea[name='q']")
                    );
            verifyDisplayed("CSS selector", searchByCss);

            // 3. Full link text
            WebElement gmailByFullText =
                    driver.findElement(By.linkText("Gmail"));
            verifyDisplayed("Full link text", gmailByFullText);

            // 4. Partial link text
            WebElement gmailByPartialText =
                    driver.findElement(By.partialLinkText("Gma"));
            verifyDisplayed("Partial link text", gmailByPartialText);

            // 5. XPath
            WebElement searchByXpath =
                    driver.findElement(
                        By.xpath("//textarea[@name='q']")
                    );
            verifyDisplayed("XPath", searchByXpath);

            // 6. Name
            WebElement searchByName =
                    driver.findElement(By.name("q"));
            verifyDisplayed("Name", searchByName);

            // 7. Class name — provide a single class
            WebElement searchByClass =
                    driver.findElement(By.className("gLFyf"));
            verifyDisplayed("Class name", searchByClass);

            // 8. Tag name — finds the first matching textarea
            WebElement searchByTag =
                    driver.findElement(By.tagName("textarea"));
            verifyDisplayed("Tag name", searchByTag);

            // Interact with the search box
            String searchText = "Selenium Java tutorials";
            searchByName.sendKeys(searchText);

            if (!searchText.equals(
                    searchByName.getDomProperty("value"))) {
                throw new AssertionError(
                    "Search box does not contain the expected text"
                );
            }

            System.out.println(
                "PASS: Search text entered successfully"
            );
            System.out.println(
                "PASS: All eight locator checks completed"
            );

        } finally {
            // Close the browser even if a check fails
            driver.quit();
        }
    }

    private static void verifyDisplayed(
            String locatorName, WebElement element) {

        if (!element.isDisplayed()) {
            throw new AssertionError(
                locatorName + ": Element is not visible"
            );
        }

        System.out.println(
            "PASS: Element found using " + locatorName
        );
    }
    
    private static void verifyNotNull(WebElement element) {
    	if (element == null) {
    		 throw new AssertionError(
    	                element + ": Element not found"
    	            );
    	}
    	
    	 System.out.println(
    	            "PASS: Element found"
    	        );
    }
}