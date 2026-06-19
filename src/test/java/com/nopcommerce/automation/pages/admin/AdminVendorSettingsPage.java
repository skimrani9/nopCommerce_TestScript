package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminVendorSettingsPage extends BasePage {

    private final By allowApplyCheckbox = By.id("AllowCustomersToApplyForVendorAccount");
    private final By allowEditInfoCheckbox = By.id("AllowVendorsToEditInfo");
    private final By allowContactVendorsCheckbox = By.id("AllowCustomersToContactVendors");
    private final By saveButton = By.cssSelector(".content-header button[name='save']");

    public void openVendorSettings() {
        navigateTo(Constants.ADMIN_VENDOR_SETTINGS_PATH);
        WaitUtils.waitForElement(allowApplyCheckbox, WaitType.VISIBLE);
        enableAdvancedSettingsIfPresent();
    }

    public void ensureStorefrontVendorSettingsEnabled() {
        openVendorSettings();
        expandAdminCardByName("vendorsettings-common");
        setCheckbox(allowApplyCheckbox, true);
        setCheckbox(allowEditInfoCheckbox, true);
        expandAdminCardByName("vendorsettings-catalog");
        setCheckbox(allowContactVendorsCheckbox, true);
        click(saveButton);
        waitForAjaxComplete();
    }
}
