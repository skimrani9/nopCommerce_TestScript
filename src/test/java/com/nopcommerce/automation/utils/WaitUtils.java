package com.nopcommerce.automation.utils;

import com.nopcommerce.automation.config.ConfigReader;
import com.nopcommerce.automation.config.DriverManager;
import com.nopcommerce.automation.constants.WaitType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class WaitUtils {

    private WaitUtils() {
    }

    public static WebElement waitForElement(By locator, WaitType waitType) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));

        return switch (waitType) {
            case CLICKABLE -> wait.until(ExpectedConditions.elementToBeClickable(locator));
            case VISIBLE -> wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            case PRESENCE -> wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        };
    }

    public static boolean waitForUrlContains(String partialUrl) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
        String expected = partialUrl.toLowerCase();
        return wait.until(webDriver -> webDriver.getCurrentUrl().toLowerCase().contains(expected));
    }

    public static boolean waitForUrlNotContains(String partialUrl) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
        String expected = partialUrl.toLowerCase();
        return wait.until(webDriver -> !webDriver.getCurrentUrl().toLowerCase().contains(expected));
    }

    public static void waitForInvisibility(By locator) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForNumberOfWindowsToBe(int windowCount) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
        wait.until(ExpectedConditions.numberOfWindowsToBe(windowCount));
    }
}
