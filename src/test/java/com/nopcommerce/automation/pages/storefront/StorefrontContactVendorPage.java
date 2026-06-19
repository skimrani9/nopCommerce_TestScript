package com.nopcommerce.automation.pages.storefront;

import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class StorefrontContactVendorPage extends BasePage {

    private final By contactPageMarker = By.cssSelector(".html-contact-page");
    private final By fullNameInput = By.cssSelector("input.fullname, input#FullName");
    private final By emailInput = By.cssSelector(".contact-page input.email, input#Email");
    private final By enquiryInput = By.cssSelector("textarea.enquiry, textarea#Enquiry");
    private final By sendButton = By.cssSelector("button.contact-us-button, button[name='send-email']");
    private final By successResult = By.cssSelector(".contact-page .result");

    public boolean isContactVendorPageDisplayed() {
        return isDisplayed(contactPageMarker);
    }

    public void submitContactForm(String fullName, String email, String enquiry) {
        type(fullNameInput, fullName);
        type(emailInput, email);
        type(enquiryInput, enquiry);
        click(sendButton);
        waitForAjaxComplete();
    }

    public boolean isContactSubmissionSuccessDisplayed() {
        return isDisplayed(successResult);
    }
}
