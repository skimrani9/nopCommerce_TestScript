package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.pages.BasePage;
import org.openqa.selenium.By;

public class AdminAccessDeniedPage extends BasePage {

    private final By accessDeniedHeading = By.cssSelector(".content-header h1");
    private final By accessDeniedAlert = By.cssSelector(".alert-danger");

    public void openRestrictedAdminPath(String path) {
        navigateTo(path);
    }

    public boolean isAccessDeniedPageDisplayed() {
        return isDisplayed(accessDeniedAlert) && isDisplayed(accessDeniedHeading);
    }
}
