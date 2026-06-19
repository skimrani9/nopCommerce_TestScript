package com.nopcommerce.automation.utils;

import com.nopcommerce.automation.config.DriverManager;
import com.nopcommerce.automation.constants.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
    }

    public static String captureScreenshot(String scenarioName) {
        WebDriver driver = DriverManager.getDriver();
        if (!(driver instanceof TakesScreenshot takesScreenshot)) {
            return null;
        }

        try {
            Files.createDirectories(new File(Constants.SCREENSHOT_DIR).toPath());
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS"));
            String safeName = scenarioName.replaceAll("[^a-zA-Z0-9-_]", "_");
            File destination = new File(Constants.SCREENSHOT_DIR + File.separator + safeName + "-" + timestamp + ".png");
            File source = takesScreenshot.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, destination);
            logger.info("Screenshot captured at {}", destination.getAbsolutePath());
            return destination.getAbsolutePath();
        } catch (IOException exception) {
            logger.error("Unable to capture screenshot for scenario {}", scenarioName, exception);
            return null;
        }
    }
}
