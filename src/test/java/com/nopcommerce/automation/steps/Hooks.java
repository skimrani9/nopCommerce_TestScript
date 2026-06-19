package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.DriverManager;
import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.pages.admin.AdminDashboardPage;
import com.nopcommerce.automation.pages.admin.AdminGeneralSettingsPage;
import com.nopcommerce.automation.pages.customer.CustomerMfaSetupPage;
import com.nopcommerce.automation.pages.customer.LoginPage;
import com.nopcommerce.automation.utils.AdminEnvironmentHelper;
import com.nopcommerce.automation.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks {

    private static final Logger logger = LogManager.getLogger(Hooks.class);

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        ScenarioContext.clear();
        String userAgent = scenario.getSourceTagNames().contains("@SearchEngine")
                ? com.nopcommerce.automation.constants.Constants.GOOGLEBOT_USER_AGENT
                : null;
        DriverManager.initDriver(userAgent);
        logger.info("Starting scenario: {}", scenario.getName());
    }

    @After(value = "@admin-mfa", order = 1)
    public void cleanupMfaConfiguration() {
        try {
            new CustomerMfaSetupPage().disableMultiFactorAuthentication();
            logger.info("Disabled MFA configuration after MFA scenario");
        } catch (Exception exception) {
            logger.warn("Unable to disable MFA configuration during cleanup", exception);
        }
    }

    @After(value = "@admin-captcha", order = 1)
    public void cleanupCaptchaConfiguration() {
        try {
            AdminDashboardPage adminDashboardPage = new AdminDashboardPage();
            LoginPage loginPage = new LoginPage();
            adminDashboardPage.openDashboard();
            if (loginPage.isLoginPageDisplayed()) {
                loginPage.login(
                        com.nopcommerce.automation.config.ConfigReader.getAdminEmail(),
                        com.nopcommerce.automation.config.ConfigReader.getAdminPassword());
            }
            new AdminGeneralSettingsPage().disableCaptchaOnLoginPage();
            logger.info("Disabled CAPTCHA configuration after CAPTCHA scenario");
        } catch (Exception exception) {
            logger.warn("Unable to disable CAPTCHA configuration during cleanup", exception);
        }
    }

    @After(order = 1)
    public void clearAdminImpersonationState() {
        AdminEnvironmentHelper.clearStaleImpersonation();
    }

    @After(value = "@TC-NC-T36004", order = 0)
    public void clearImpersonationBrowserSession() {
        try {
            AdminEnvironmentHelper.ensureAnonymousStorefrontSession();
        } catch (Exception exception) {
            logger.warn("Unable to clear impersonation browser session", exception);
        }
    }

    @After(order = -1)
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            ScreenshotUtils.captureScreenshot(scenario.getName());
        }

        if (scenario.getStatus() == Status.FAILED) {
            logger.error("Scenario failed: {}", scenario.getName());
        } else {
            logger.info("Scenario finished: {} with status {}", scenario.getName(), scenario.getStatus());
        }

        DriverManager.quitDriver();
        ScenarioContext.clear();
    }
}
