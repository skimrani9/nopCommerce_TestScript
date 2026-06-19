package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminPaymentRestrictionsPage extends BasePage {

    private final By saveButton = By.cssSelector("button[name='save']");

    public void openPaymentRestrictions() {
        navigateTo(Constants.ADMIN_PAYMENT_RESTRICTIONS_PATH);
        WaitUtils.waitForElement(saveButton, WaitType.VISIBLE);
    }

    public void restrictPaymentMethodToCountry(String systemName, String countryName) {
        openPaymentRestrictions();
        By countryCheckbox = By.xpath(
                "//tr[.//td[contains(normalize-space(.), '" + countryName + "')]]"
                        + "//input[contains(@name,'restrict_" + systemName + "')]");
        setCheckbox(countryCheckbox, true);
        click(saveButton);
        waitForAjaxComplete();
    }

    public boolean isRestrictionsPageDisplayed() {
        return isDisplayed(saveButton);
    }
}
