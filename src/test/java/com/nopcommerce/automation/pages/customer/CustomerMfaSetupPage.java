package com.nopcommerce.automation.pages.customer;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CustomerMfaSetupPage extends BasePage {

    private static final String GOOGLE_AUTHENTICATOR_PROVIDER_ID = "provider_MultiFactorAuth.GoogleAuthenticator";

    private final By mfaEnabledCheckbox = By.cssSelector("input#IsEnabled, input[name='IsEnabled']");
    private final By saveMfaSettingsButton = By.id("save-mfa-settings-button");
    private final By googleAuthenticatorRadio = By.id(GOOGLE_AUTHENTICATOR_PROVIDER_ID);
    private final By secretKeyInput = By.cssSelector("input#SecretKey, input[name='SecretKey']");
    private final By registrationCodeInput = By.cssSelector("input#Code, input[name='Code']");
    private final By registerMfaButton = By.cssSelector(".google-authenticator-conteiner button[type='submit']");

    public void enableGoogleAuthenticatorForCurrentCustomer() {
        navigateTo("/");
        if (!tryOpenGoogleAuthenticatorConfiguration()) {
            openMultiFactorAuthenticationSettings();
            setCheckbox(mfaEnabledCheckbox, true);
            click(googleAuthenticatorRadio);
            click(saveMfaSettingsButton);
            if (!tryOpenGoogleAuthenticatorConfiguration()) {
                throw new IllegalStateException(
                        "Google Authenticator configuration page is unavailable at "
                                + getCurrentUrl());
            }
        }
    }

    public void openMultiFactorAuthenticationSettings() {
        navigateTo(Constants.CUSTOMER_MFA_PATH);
        WaitUtils.waitForElement(mfaEnabledCheckbox, WaitType.VISIBLE);
    }

    public String getSecretKey() {
        WebElement secretKey = WaitUtils.waitForElement(secretKeyInput, WaitType.PRESENCE);
        String value = secretKey.getAttribute("value");
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Google Authenticator secret key was not found on MFA setup page");
        }
        return value.trim();
    }

    public void completeGoogleAuthenticatorRegistration(String verificationCode) {
        type(registrationCodeInput, verificationCode);
        click(registerMfaButton);
        WaitUtils.waitForUrlContains(Constants.CUSTOMER_MFA_PATH);
    }

    public void disableMultiFactorAuthentication() {
        try {
            openMultiFactorAuthenticationSettings();
            setCheckbox(mfaEnabledCheckbox, false);
            click(saveMfaSettingsButton);
        } catch (Exception exception) {
            logger.warn("Unable to open MFA settings while disabling MFA", exception);
        }
    }

    private boolean tryOpenGoogleAuthenticatorConfiguration() {
        navigateTo(Constants.CUSTOMER_MFA_PROVIDER_CONFIG_PATH);
        try {
            WaitUtils.waitForElement(secretKeyInput, WaitType.PRESENCE);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
