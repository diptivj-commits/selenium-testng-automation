package com.automation.selenium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public final class ExcelUtil {

    private ExcelUtil() {
    }

    public static Object[][] readLoginData(String resourcePath) {

        try (InputStream input = ExcelUtil.class.getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (input == null) {
                throw new IllegalArgumentException(
                        "Excel file not found: " + resourcePath
                );
            }

            try (Workbook workbook = WorkbookFactory.create(input)) {

                Sheet sheet = workbook.getSheet("Login");

                if (sheet == null) {
                    throw new IllegalArgumentException(
                            "Sheet 'Login' not found"
                    );
                }

                DataFormatter formatter = new DataFormatter();
                FormulaEvaluator evaluator =
                        workbook.getCreationHelper()
                                .createFormulaEvaluator();

                List<Object[]> testData = new ArrayList<>();

                // Start at 1 to skip the header row
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                    Row row = sheet.getRow(i);

                    if (row == null) {
                        continue;
                    }

                    String username = formatter.formatCellValue(
                            row.getCell(0), evaluator
                    );

                    String password = formatter.formatCellValue(
                            row.getCell(1), evaluator
                    );

                    String expectedMessage = formatter.formatCellValue(
                            row.getCell(2), evaluator
                    );

                    // Skip completely empty rows
                    if (username.isBlank()
                            && password.isBlank()
                            && expectedMessage.isBlank()) {
                        continue;
                    }

                    if (expectedMessage.isBlank()) {
                        throw new IllegalArgumentException(
                                "Missing expectedMessage in Excel row "
                                        + (i + 1)
                        );
                    }

                    testData.add(new Object[] {
                            username, password, expectedMessage
                    });
                }

                if (testData.isEmpty()) {
                    throw new IllegalArgumentException(
                            "No test data found in sheet 'Login'"
                    );
                }

                return testData.toArray(new Object[0][]);
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to read Excel file: " + resourcePath, e
            );
        }
    }
}