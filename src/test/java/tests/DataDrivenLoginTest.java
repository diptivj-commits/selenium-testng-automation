package tests;



import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.automation.selenium.pages.LoginPage;

public class DataDrivenLoginTest extends com.automation.selenium.base.BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] provideLoginData() {

        return new Object[][] {
            {
                "tomsmith",
                "SuperSecretPassword!",
                "You logged into a secure area!"
            },
            {
                "wrongUser",
                "SuperSecretPassword!",
                "Your username is invalid!"
            },
            {
                "tomsmith",
                "wrongPassword",
                "Your password is invalid!"
            },
            {
                "",
                "",
                "Your username is invalid!"
            }
        };
    }

    @Test(dataProvider = "loginData")
    public void verifyLogin(
            String username,
            String password,
            String expectedMessage) {

        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open(BASE_URL);

        loginPage.login(username, password);

        String actualMessage = loginPage.getFlashMessage();

        Assert.assertTrue(
                actualMessage.contains(expectedMessage),
                "Expected: " + expectedMessage
                        + " | Actual: " + actualMessage
        );
    }
}