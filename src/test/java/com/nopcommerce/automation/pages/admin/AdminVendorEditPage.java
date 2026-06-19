package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminVendorEditPage extends BasePage {

    private final By activeCheckbox = By.id("Active");
    private final By seoNameInput = By.id("SeName");
    private final By descriptionInput = By.id("Description");
    private final By saveButton = By.cssSelector(".content-header button[name='save']");

    public void activateVendor() {
        enableAdvancedSettingsIfPresent();
        expandAdminCardByName("vendor-info");
        setCheckbox(activeCheckbox, true);
    }

    public void setVendorSeoName(String seoName) {
        enableAdvancedSettingsIfPresent();
        expandAdminCardByName("vendor-seo");
        type(seoNameInput, seoName);
    }

    public void setVendorDescription(String description) {
        expandAdminCardByName("vendor-info");
        type(descriptionInput, description);
    }

    public void clickSave() {
        click(saveButton);
        waitForAjaxComplete();
        try {
            WaitUtils.waitForUrlContains(Constants.ADMIN_VENDOR_LIST_PATH);
        } catch (Exception exception) {
            WaitUtils.waitForUrlContains("/vendor/edit");
            WaitUtils.waitForElement(activeCheckbox, WaitType.VISIBLE);
        }
    }

    public void waitForEditPageLoad() {
        WaitUtils.waitForUrlContains("/vendor/edit");
        WaitUtils.waitForElement(activeCheckbox, WaitType.VISIBLE);
    }
}
