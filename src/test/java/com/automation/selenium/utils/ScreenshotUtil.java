package com.automation.selenium.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtil {

    private ScreenshotUtil() {
        // Utility class
    }

    public static Path captureScreenshot(
            WebDriver driver, String testName) {

        if (driver == null) {
            throw new IllegalArgumentException(
                    "Driver must not be null"
            );
        }

        if (!(driver instanceof TakesScreenshot)) {
            throw new IllegalArgumentException(
                    "Driver does not support screenshots"
            );
        }

        String safeName = testName == null || testName.isBlank()
                ? "test"
                : testName.replaceAll("[^a-zA-Z0-9_-]", "_");

        Path directory = Paths.get(
                "target", "screenshots"
        ).toAbsolutePath();

        try {
            Files.createDirectories(directory);

            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            // Creates a unique filename for each screenshot
            Path destination = Files.createTempFile(
                    directory, safeName + "_", ".png"
            );

            Files.write(destination, screenshot);

            System.out.println(
                    "Screenshot saved: " + destination
            );

            return destination;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to save screenshot for " + testName, e
            );
        }
    }
}
