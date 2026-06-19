package com.nopcommerce.automation.utils;

import com.nopcommerce.automation.config.ConfigReader;
import com.nopcommerce.automation.config.DriverManager;
import com.nopcommerce.automation.constants.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public final class AdminEnvironmentHelper {

    private static final Logger logger = LogManager.getLogger(AdminEnvironmentHelper.class);
    private static volatile boolean captchaPrepared;

    private AdminEnvironmentHelper() {
    }

    public static void resetCaptchaPrepared() {
        captchaPrepared = false;
    }

    public static void ensureAdminLoginReady() {
        clearStaleImpersonation();
        if (captchaPrepared) {
            return;
        }
        synchronized (AdminEnvironmentHelper.class) {
            if (captchaPrepared) {
                return;
            }
            prepareStoreForAutomation();
            captchaPrepared = true;
        }
    }

    public static void clearStaleImpersonation() {
        try {
            String sql = "DELETE FROM \\\"GenericAttribute\\\" "
                    + "WHERE \\\"KeyGroup\\\"='Customer' AND \\\"Key\\\"='ImpersonatedCustomerId';";
            String command = "docker exec nopcommerce_postgres_server "
                    + "psql -U postgres -d nopcommerce -c '" + sql + "'";
            ProcessBuilder processBuilder = new ProcessBuilder("wsl", "-e", "bash", "-lc", command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.debug("clear impersonation: {}", line);
                }
            }
            process.waitFor(30, TimeUnit.SECONDS);
        } catch (Exception exception) {
            logger.warn("Unable to clear stale impersonation state", exception);
        }
    }

    public static void ensureAnonymousStorefrontSession() {
        clearStaleImpersonation();
        try {
            WebDriver driver = DriverManager.getDriver();
            driver.get(ConfigReader.getBaseUrl() + Constants.STORE_LOGOUT_PATH);
            driver.manage().deleteAllCookies();
        } catch (Exception exception) {
            logger.warn("Unable to clear storefront authentication cookies", exception);
        }
    }

    private static void prepareStoreForAutomation() {
        disableCaptchaIfNeeded();
        waitForStoreReady();
    }

    private static void disableCaptchaIfNeeded() {
        Path scriptPath = Paths.get("scripts", "disable_captcha_wsl.py").toAbsolutePath();
        if (!scriptPath.toFile().exists()) {
            logger.warn("CAPTCHA disable script not found at {}", scriptPath);
            return;
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath.toString());
            processBuilder.environment().put("NOP_BASE_URL", ConfigReader.getBaseUrl());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("disable_captcha_wsl: {}", line);
                }
            }
            if (!process.waitFor(180, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                logger.warn("Timed out disabling CAPTCHA via WSL script");
                return;
            }
            if (process.exitValue() != 0) {
                logger.warn("CAPTCHA disable script exited with code {}", process.exitValue());
            } else {
                logger.info("CAPTCHA settings reset for automation and store is ready");
            }
        } catch (Exception exception) {
            logger.warn("Unable to disable CAPTCHA automatically", exception);
        }
    }

    public static void waitForStoreReady() {
        for (int attempt = 0; attempt < 30; attempt++) {
            try {
                java.net.HttpURLConnection connection =
                        (java.net.HttpURLConnection) new java.net.URL(ConfigReader.getBaseUrl() + "/").openConnection();
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
                connection.setRequestMethod("GET");
                if (connection.getResponseCode() == 200) {
                    return;
                }
            } catch (Exception ignored) {
                // retry until store responds
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
