package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.automation.selenium.base.BaseTest;
import com.automation.selenium.pages.LoginPage;

public class CsvLoginTest extends BaseTest {

    @DataProvider(name = "csvLoginData")
    public Object[][] readCsvData() throws IOException {

        String resourcePath = "testdata/LoginData.csv";

        InputStream input = getClass().getClassLoader()
                .getResourceAsStream(resourcePath);

        if (input == null) {
            throw new IllegalArgumentException(
                    "CSV file not found: " + resourcePath
            );
        }

        List<Object[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        input, StandardCharsets.UTF_8))) {

            // Read and skip the header
            String header = reader.readLine();

            if (header == null) {
                throw new IllegalArgumentException(
                        "CSV file is empty"
                );
            }

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                // -1 preserves empty fields, including trailing ones
                String[] values = line.split(",", -1);

                if (values.length != 3) {
                    throw new IllegalArgumentException(
                            "Expected 3 columns at line "
                                    + lineNumber
                    );
                }

                if (values[2].isBlank()) {
                    throw new IllegalArgumentException(
                            "Expected message missing at line "
                                    + lineNumber
                    );
                }

                data.add(new Object[] {
                        values[0], values[1], values[2]
                });
            }
        }

        if (data.isEmpty()) {
            throw new IllegalArgumentException(
                    "CSV contains no test data"
            );
        }

        return data.toArray(new Object[0][]);
    }

    @Test(dataProvider = "csvLoginData")
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