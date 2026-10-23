package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.selenium.base.BaseTest;

public class KeywordDrivenLoginTest extends BaseTest {

    @Test
    public void validLoginUsingKeywords() {

        // Keyword, locator type, locator value, test data
        String[][] steps = {
            {"OPEN",        "",    "",               "/login"},
            {"TYPE",        "id",  "username",       "tomsmith"},
            {"TYPE",        "id",  "password",       "SuperSecretPassword!"},
            {"CLICK",       "css", "button[type='submit']", ""},
            {"VERIFY_TEXT", "id",  "flash",          "You logged into a secure area!"},
            {"CLICK",       "css", "a[href='/logout']", ""},
            {"VERIFY_URL",  "",    "", "/login"},
            {"VERIFY_TEXT", "id",  "flash",          "You logged out of the secure area!"}
        };

        for (int i = 0; i < steps.length; i++) {
            String[] step = steps[i];

            // Log actions without printing passwords or other test data
            System.out.println(
                    "Step " + (i + 1) + ": " + step[0]
            );

            executeStep(step[0], step[1], step[2], step[3]);
        }
    }

    private void executeStep(
            String keyword,
            String locatorType,
            String locatorValue,
            String data) {

        switch (keyword) {

            case "OPEN":
                driver.get(BASE_URL + data);
                break;

            case "TYPE":
                WebElement input = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                getLocator(locatorType, locatorValue)
                        )
                );

                input.clear();
                input.sendKeys(data);
                break;

            case "CLICK": {
                WebElement element = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                getLocator(locatorType, locatorValue)
                        )
                );

                System.out.println("Clicking: " + element.getText());
                System.out.println("Link target: " + element.getAttribute("href"));

                element.click();

                System.out.println("URL after click: " + driver.getCurrentUrl());
                break;
            }

            case "VERIFY_TEXT":
                By locator = getLocator(locatorType, locatorValue);

                // Wait for the expected text, not just the element
                wait.until(
                        ExpectedConditions.textToBePresentInElementLocated(
                                locator, data
                        )
                );

                String actualText = driver.findElement(locator).getText();

                Assert.assertTrue(
                        actualText.contains(data),
                        "Expected text: " + data
                                + " | Actual text: " + actualText
                );
                break;
            case "VERIFY_URL":
                wait.until(
                        ExpectedConditions.urlToBe(BASE_URL + data)
                );
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported keyword: " + keyword
                );
        }
    }

    private By getLocator(String type, String value) {

        switch (type.toLowerCase(java.util.Locale.ROOT)) {
            case "id":
                return By.id(value);

            case "name":
                return By.name(value);

            case "css":
                return By.cssSelector(value);

            case "xpath":
                return By.xpath(value);

            case "linktext":
                return By.linkText(value);

            case "partiallinktext":
                return By.partialLinkText(value);

            case "classname":
                return By.className(value);

            case "tagname":
                return By.tagName(value);

            default:
                throw new IllegalArgumentException(
                        "Unsupported locator type: " + type
                );
        }
    }
}