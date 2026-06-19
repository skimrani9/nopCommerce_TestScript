package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminTaxSettingsPage extends BasePage {

    private final By saveButton = By.cssSelector("button[name='save']");

    public void openTaxSettings() {
        navigateTo(Constants.ADMIN_TAX_SETTINGS_PATH);
        WaitUtils.waitForElement(By.id("taxsettings-cards"), WaitType.VISIBLE);
    }

    public void setPricesIncludeTax(boolean includeTax) {
        expandAdminCardByName("taxsettings-common");
        setCheckbox(By.id("PricesIncludeTax"), includeTax);
        click(saveButton);
        waitForAjaxComplete();
    }

    public boolean isTaxSettingsPageDisplayed() {
        return isDisplayed(By.id("taxsettings-cards"));
    }
}
