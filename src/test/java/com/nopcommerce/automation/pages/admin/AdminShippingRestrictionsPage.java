package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminShippingRestrictionsPage extends BasePage {

    private final By saveButton = By.cssSelector("button[name='save']");

    public void openShippingRestrictions() {
        navigateTo(Constants.ADMIN_SHIPPING_RESTRICTIONS_PATH);
        WaitUtils.waitForElement(saveButton, WaitType.VISIBLE);
    }

    public void saveRestrictions() {
        click(saveButton);
        waitForAjaxComplete();
    }

    public boolean isRestrictionsPageDisplayed() {
        return isDisplayed(saveButton);
    }
}
