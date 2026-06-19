package com.nopcommerce.automation.pages.storefront;

import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class StorefrontVendorPage extends BasePage {

    private final By vendorPageMarker = By.cssSelector(".html-vendor-page");
    private final By vendorDescription = By.cssSelector(".vendor-page .vendor-description");
    private final By contactVendorButton = By.cssSelector(".contact-vendor-button");

    public void openVendorBySeoName(String seoName) {
        navigateTo("/" + seoName);
        WaitUtils.waitForElement(vendorPageMarker, WaitType.VISIBLE);
    }

    public boolean isVendorPageDisplayed() {
        return isDisplayed(vendorPageMarker);
    }

    public boolean isVendorNameDisplayed(String vendorName) {
        By vendorHeading = By.xpath("//div[contains(@class,'vendor-page')]//h1[contains(normalize-space(.), '"
                + vendorName + "')]");
        return isDisplayed(vendorHeading);
    }

    public boolean isVendorDescriptionVisible() {
        return isDisplayed(vendorDescription) && !getText(vendorDescription).isBlank();
    }

    public boolean vendorDescriptionContains(String text) {
        return getText(vendorDescription).contains(text);
    }

    public boolean isContactVendorButtonVisible() {
        return isDisplayed(contactVendorButton);
    }

    public void openContactVendorPage() {
        click(contactVendorButton);
        WaitUtils.waitForElement(By.cssSelector(".html-contact-page"), WaitType.VISIBLE);
    }
}
