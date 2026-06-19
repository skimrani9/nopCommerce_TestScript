package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.DriverManager;
import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.utils.GuestTestDataHelper;
import io.cucumber.java.Before;

public class SearchEngineHooks {

    @Before(order = 1, value = "@SearchEngine and @requires-data")
    public void prepareSearchEngineCatalogData() {
        DriverManager.quitDriver();
        DriverManager.initDriver();
        try {
            GuestTestDataHelper.ensureGuestCatalogData();
        } finally {
            DriverManager.quitDriver();
            DriverManager.initDriver(Constants.GOOGLEBOT_USER_AGENT);
        }
    }
}
