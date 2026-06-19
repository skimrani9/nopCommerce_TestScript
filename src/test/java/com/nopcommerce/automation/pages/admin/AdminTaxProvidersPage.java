package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminTaxProvidersPage extends BasePage {

    public void openTaxProviders() {
        navigateTo(Constants.ADMIN_TAX_PROVIDERS_PATH);
        WaitUtils.waitForElement(By.id("tax-providers-grid"), WaitType.VISIBLE);
    }

    public boolean isTaxProvidersGridDisplayed() {
        return isDisplayed(By.id("tax-providers-grid"));
    }
}
