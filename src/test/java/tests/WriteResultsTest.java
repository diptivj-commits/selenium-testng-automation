package tests;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.selenium.base.BaseTest;
import com.automation.selenium.pages.LoginPage;

public class WriteResultsTest extends BaseTest {

    @Test
    public void loginAndWriteResults() throws IOException {

        String testName = "Valid Login";
        String expected = "You logged into a secure area!";
        String actual = "";
        String status = "FAIL";
        Throwable testFailure = null;

        try {
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.open(BASE_URL);

            loginPage.login("tomsmith", "SuperSecretPassword!");

            actual = loginPage.getFlashMessage();

            Assert.assertTrue(
                    actual.contains(expected),
                    "Unexpected login message: " + actual
            );

            status = "PASS";

        } catch (RuntimeException | AssertionError e) {
            testFailure = e;
            actual = actual.isEmpty() ? e.toString() : actual;
            throw e;

        } finally {
            try {
                writeResults(testName, expected, actual, status);
            } catch (IOException | RuntimeException e) {
                if (testFailure != null) {
                    // Keep the original test failure
                    testFailure.addSuppressed(e);
                } else {
                    throw e;
                }
            }
        }
    }

    private void writeResults(
            String testName,
            String expected,
            String actual,
            String status) throws IOException {

        Path outputDirectory = Paths.get(
                "target", "test-results"
        ).toAbsolutePath();

        Files.createDirectories(outputDirectory);

        String[] headers = {
                "Test Name", "Expected", "Actual", "Status"
        };

        String[] result = {
                testName, expected, actual, status
        };

        // Write Excel
        Path excelFile = outputDirectory.resolve("LoginResults.xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Results");

            writeExcelRow(sheet.createRow(0), headers);
            writeExcelRow(sheet.createRow(1), result);

            for (int column = 0; column < headers.length; column++) {
                sheet.autoSizeColumn(column);
            }

            try (OutputStream output =
                         Files.newOutputStream(excelFile)) {
                workbook.write(output);
            }
        }

        // Write CSV using Java's built-in file APIs
        Path csvFile = outputDirectory.resolve("LoginResults.csv");

        String csv = toCsvRow(headers)
                + "\r\n"
                + toCsvRow(result)
                + "\r\n";

        Files.writeString(
                csvFile, csv, StandardCharsets.UTF_8
        );

        System.out.println("Excel saved: " + excelFile);
        System.out.println("CSV saved: " + csvFile);
    }

    private void writeExcelRow(Row row, String[] values) {
        for (int column = 0; column < values.length; column++) {
            row.createCell(column).setCellValue(values[column]);
        }
    }

    private String toCsvRow(String[] values) {
        String[] escaped = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            // Quote fields and escape embedded quotation marks.
            // This also handles commas and line breaks in messages.
            escaped[i] = "\""
                    + values[i].replace("\"", "\"\"")
                    + "\"";
        }

        return String.join(",", escaped);
    }
}