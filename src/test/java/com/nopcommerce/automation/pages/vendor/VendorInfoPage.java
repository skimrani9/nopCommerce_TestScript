package com.nopcommerce.automation.pages.vendor;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class VendorInfoPage extends BasePage {

    private final By vendorInfoPageMarker = By.cssSelector(".html-vendorinfo-page");
    private final By descriptionInput = By.cssSelector(".vendorinfo-page textarea#Description, .vendorinfo-page textarea");
    private final By saveButton = By.cssSelector(".save-vendorinfo-button");
    private final By successNotification = By.cssSelector("#bar-notification.success, .bar-notification.success");

    public void navigateToVendorInfoPage() {
        navigateTo(Constants.STORE_VENDOR_INFO_PATH);
    }

    public void openVendorInfoPage() {
        navigateToVendorInfoPage();
        WaitUtils.waitForElement(vendorInfoPageMarker, WaitType.VISIBLE);
    }

    public boolean isVendorInfoPageDisplayed() {
        return isDisplayed(vendorInfoPageMarker);
    }

    public void updateDescription(String description) {
        type(descriptionInput, description);
        click(saveButton);
        waitForAjaxComplete();
        WaitUtils.waitForElement(vendorInfoPageMarker, WaitType.VISIBLE);
    }

    public String getDescriptionValue() {
        return getInputValue(descriptionInput);
    }

    public boolean isUpdateSuccessDisplayed() {
        return isDisplayed(successNotification);
    }
}
