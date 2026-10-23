package tests;


import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.automation.selenium.base.BaseTest;
import com.automation.selenium.pages.LoginPage;
import com.automation.selenium.utils.ExcelUtil;

public class ExcelLoginTest extends BaseTest {

    @DataProvider(name = "excelLoginData")
    public Object[][] getLoginData() {

        return ExcelUtil.readLoginData(
                "testdata/LoginData.xlsx"
        );
    }

    @Test(dataProvider = "excelLoginData")
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