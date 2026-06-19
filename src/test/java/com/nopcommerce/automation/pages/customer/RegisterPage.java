package com.nopcommerce.automation.pages.customer;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class RegisterPage extends BasePage {

    private final By firstNameInput = By.cssSelector("input#FirstName");
    private final By lastNameInput = By.cssSelector("input#LastName");
    private final By emailInput = By.cssSelector("input.email, input#Email");
    private final By passwordInput = By.cssSelector("input.password, input#Password");
    private final By confirmPasswordInput = By.cssSelector("input#ConfirmPassword");
    private final By acceptPrivacyPolicyCheckbox = By.id("accept-consent");
    private final By registerButton = By.id("register-button");
    private final By registrationPageMarker = By.cssSelector(".html-registration-page");
    private final By registrationResultMarker = By.cssSelector(".html-registration-result-page");
    private final By registrationErrorSummary = By.cssSelector(".message-error, .validation-summary-errors");

    public void openRegisterPage() {
        navigateTo(Constants.REGISTER_PATH);
        WaitUtils.waitForElement(registrationPageMarker, WaitType.VISIBLE);
    }

    public boolean tryRegister(String email, String password, String firstName, String lastName) {
        openRegisterPage();
        if (isDisplayed(firstNameInput)) {
            type(firstNameInput, firstName);
        }
        if (isDisplayed(lastNameInput)) {
            type(lastNameInput, lastName);
        }
        type(emailInput, email);
        type(passwordInput, password);
        if (isDisplayed(confirmPasswordInput)) {
            type(confirmPasswordInput, password);
        }
        if (isDisplayed(acceptPrivacyPolicyCheckbox)) {
            setCheckbox(acceptPrivacyPolicyCheckbox, true);
        }
        click(registerButton);
        try {
            WaitUtils.waitForUrlContains(Constants.STORE_REGISTER_RESULT_PATH);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean isRegistrationResultPageDisplayed() {
        return isDisplayed(registrationResultMarker);
    }

    public boolean isRegistrationPageDisplayed() {
        return isDisplayed(registrationPageMarker);
    }

    public void register(String email, String password) {
        openRegisterPage();
        if (isDisplayed(firstNameInput)) {
            type(firstNameInput, "E2E");
        }
        if (isDisplayed(lastNameInput)) {
            type(lastNameInput, "Customer");
        }
        type(emailInput, email);
        type(passwordInput, password);
        if (isDisplayed(confirmPasswordInput)) {
            type(confirmPasswordInput, password);
        }
        if (isDisplayed(acceptPrivacyPolicyCheckbox)) {
            setCheckbox(acceptPrivacyPolicyCheckbox, true);
        }
        click(registerButton);
        WaitUtils.waitForUrlContains("/registerresult");
    }
}
