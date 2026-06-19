package com.nopcommerce.automation.pages.customer;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class CustomerInfoPage extends BasePage {

    private final By customerInfoPageMarker = By.cssSelector(".html-customer-info-page");
    private final By firstNameInput = By.cssSelector("input#FirstName");
    private final By lastNameInput = By.cssSelector("input#LastName");
    private final By emailInput = By.cssSelector("input#Email");
    private final By saveButton = By.id("save-info-button");
    private final By successNotification = By.cssSelector("#bar-notification.success, .bar-notification.success");

    public void openCustomerInfoPage() {
        navigateTo(Constants.STORE_CUSTOMER_INFO_PATH);
        WaitUtils.waitForElement(customerInfoPageMarker, WaitType.VISIBLE);
    }

    public void navigateToCustomerInfoPage() {
        navigateTo(Constants.STORE_CUSTOMER_INFO_PATH);
    }

    public boolean isCustomerInfoPageDisplayed() {
        return isDisplayed(customerInfoPageMarker)
                && isDisplayed(firstNameInput)
                && isDisplayed(lastNameInput)
                && isDisplayed(emailInput);
    }

    public String getFirstNameValue() {
        return getInputValue(firstNameInput);
    }

    public String getLastNameValue() {
        return getInputValue(lastNameInput);
    }

    public String getEmailValue() {
        return getInputValue(emailInput);
    }

    public void updateFirstNameAndLastName(String firstName, String lastName) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        click(saveButton);
        waitForAjaxComplete();
        WaitUtils.waitForElement(customerInfoPageMarker, WaitType.VISIBLE);
    }

    public boolean isUpdateSuccessDisplayed() {
        return isDisplayed(successNotification);
    }
}
