package com.nopcommerce.automation.pages.vendor;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class VendorApplyPage extends BasePage {

    private final By applyPageMarker = By.cssSelector(".html-apply-vendor-page");
    private final By nameInput = By.cssSelector("input.name, input#Name");
    private final By emailInput = By.cssSelector(".apply-vendor-page input.email, input#Email");
    private final By descriptionInput = By.cssSelector("textarea.description, textarea#Description");
    private final By applyButton = By.id("apply-vendor");
    private final By applicationResult = By.cssSelector(".apply-vendor-page .result");
    private final By applyForm = By.cssSelector(".apply-vendor-page form");
    private final By termsOfServiceCheckbox = By.id("termsofservice");

    public void navigateToApplyPage() {
        navigateTo(Constants.STORE_VENDOR_APPLY_PATH);
    }

    public void openApplyPage() {
        navigateToApplyPage();
        WaitUtils.waitForElement(applyPageMarker, WaitType.VISIBLE);
    }

    public boolean isApplyPageDisplayed() {
        return isDisplayed(applyPageMarker);
    }

    public boolean isApplyFormDisplayed() {
        return isDisplayed(applyForm);
    }

    public boolean isApplicationResultDisplayed() {
        return isDisplayed(applicationResult);
    }

    public String getApplicationResultText() {
        return getText(applicationResult);
    }

    public void submitApplication(String name, String email, String description) {
        type(nameInput, name);
        type(emailInput, email);
        type(descriptionInput, description);
        acceptTermsOfServiceIfPresent();
        click(applyButton);
        waitForAjaxComplete();
    }

    private void acceptTermsOfServiceIfPresent() {
        if (isDisplayedQuick(termsOfServiceCheckbox)) {
            setCheckbox(termsOfServiceCheckbox, true);
        }
    }
}
