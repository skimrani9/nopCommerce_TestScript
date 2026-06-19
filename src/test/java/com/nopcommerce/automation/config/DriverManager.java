package com.nopcommerce.automation.config;

import com.nopcommerce.automation.constants.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void initDriver() {
        initDriver(null);
    }

    public static void initDriver(String userAgent) {
        if (driver.get() != null) {
            return;
        }

        String browser = ConfigReader.getBrowser();
        if (browser == null || browser.isBlank()) {
            browser = Constants.DEFAULT_BROWSER;
        }

        WebDriver webDriver = switch (browser.toLowerCase()) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                if (userAgent != null && !userAgent.isBlank()) {
                    org.openqa.selenium.firefox.FirefoxOptions firefoxOptions =
                            new org.openqa.selenium.firefox.FirefoxOptions();
                    firefoxOptions.addPreference("general.useragent.override", userAgent);
                    yield new FirefoxDriver(firefoxOptions);
                }
                yield new FirefoxDriver();
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                if (userAgent != null && !userAgent.isBlank()) {
                    org.openqa.selenium.edge.EdgeOptions edgeOptions = new org.openqa.selenium.edge.EdgeOptions();
                    edgeOptions.addArguments("--user-agent=" + userAgent);
                    yield new EdgeDriver(edgeOptions);
                }
                yield new EdgeDriver();
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--start-maximized");
                if (userAgent != null && !userAgent.isBlank()) {
                    options.addArguments("--user-agent=" + userAgent);
                }
                yield new ChromeDriver(options);
            }
        };

        webDriver.manage().timeouts().implicitlyWait(java.time.Duration.ZERO);
        driver.set(webDriver);
    }

    public static WebDriver getDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Call initDriver() first.");
        }
        return webDriver;
    }

    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            webDriver.quit();
            driver.remove();
        }
    }
}
