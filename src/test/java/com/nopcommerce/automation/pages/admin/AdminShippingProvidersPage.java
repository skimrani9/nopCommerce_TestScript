package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminShippingProvidersPage extends BasePage {

    public void openShippingProviders() {
        navigateTo(Constants.ADMIN_SHIPPING_PROVIDERS_PATH);
        WaitUtils.waitForElement(By.id("shippingproviders-grid"), WaitType.VISIBLE);
    }

    public boolean isShippingProvidersGridDisplayed() {
        return isDisplayed(By.id("shippingproviders-grid"));
    }
}
