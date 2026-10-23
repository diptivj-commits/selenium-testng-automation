package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.selenium.base.BaseTest;
import com.automation.selenium.pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginTest() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(BASE_URL);

        loginPage.login("tomsmith", "SuperSecretPassword!");

        Assert.assertTrue(
                loginPage.getFlashMessage()
                        .contains("You logged into a secure area!"),
                "Login success message should appear"
        );

        Assert.assertTrue(
                loginPage.isLogoutButtonDisplayed(),
                "Logout button should be visible after login"
        );
    }

    @Test
    public void invalidPasswordTest() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(BASE_URL);

        loginPage.login("tomsmith", "wrongPassword");

        Assert.assertTrue(
                loginPage.getFlashMessage()
                        .contains("Your password is invalid!"),
                "Invalid password message should appear"
        );
    }
}