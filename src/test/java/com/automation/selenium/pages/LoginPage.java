package com.automation.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton =
            By.cssSelector("button[type='submit']");
    private final By flashMessage = By.id("flash");
    private final By logoutButton =
            By.cssSelector("a[href='/logout']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/login");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        usernameInput
                )
        );
    }

    public void enterUsername(String username) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        usernameInput
                )
        );

        input.clear();
        input.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        passwordInput
                )
        );

        input.clear();
        input.sendKeys(password);
    }

    public void clickLogin() {
        wait.until(
                ExpectedConditions.elementToBeClickable(loginButton)
        ).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getFlashMessage() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        flashMessage
                )
        ).getText();
    }

    public boolean isLogoutButtonDisplayed() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        logoutButton
                )
        ).isDisplayed();
    }

    public void logout() {
        wait.until(
                ExpectedConditions.elementToBeClickable(logoutButton)
        ).click();
    }
}
