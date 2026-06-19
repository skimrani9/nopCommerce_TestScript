package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AdminGeneralSettingsPage extends BasePage {

    private static final String CAPTCHA_CARD_ID = "generalcommon-captcha";
    private static final String RECAPTCHA_TEST_PUBLIC_KEY = "6LeIxAcTAAAAAJcZVRqyHhHrmPgi4y0B6S_EPNdZ6";
    private static final String RECAPTCHA_TEST_PRIVATE_KEY = "6LeIxAcTAAAAAGG-vFI1TnRWxMZNQuJWeWf9A";

    private final By advancedSettingsToggle = By.id("advanced-settings-mode");
    private final By captchaEnabledCheckbox = By.id("CaptchaSettings_Enabled");
    private final By captchaShowOnLoginCheckbox = By.id("CaptchaSettings_ShowOnLoginPage");
    private final By captchaPublicKeyInput = By.id("CaptchaSettings_ReCaptchaPublicKey");
    private final By captchaPrivateKeyInput = By.id("CaptchaSettings_ReCaptchaPrivateKey");
    private final By saveButton = By.cssSelector("form[action*='GeneralCommon'] button[name='save']");

    public void enableCaptchaOnLoginPage() {
        navigateTo(Constants.ADMIN_GENERAL_SETTINGS_PATH);
        showAdvancedSettings();
        expandAdminCard(CAPTCHA_CARD_ID);
        setCheckbox(captchaEnabledCheckbox, true);
        WaitUtils.waitForElement(captchaShowOnLoginCheckbox, WaitType.VISIBLE);
        setCheckbox(captchaShowOnLoginCheckbox, true);
        WaitUtils.waitForElement(captchaPublicKeyInput, WaitType.VISIBLE);
        type(captchaPublicKeyInput, RECAPTCHA_TEST_PUBLIC_KEY);
        type(captchaPrivateKeyInput, RECAPTCHA_TEST_PRIVATE_KEY);
        click(saveButton);
        WaitUtils.waitForElement(By.cssSelector("div.wrapper"), WaitType.VISIBLE);
    }

    public void disableCaptchaOnLoginPage() {
        navigateTo(Constants.ADMIN_GENERAL_SETTINGS_PATH);
        showAdvancedSettings();
        expandAdminCard(CAPTCHA_CARD_ID);
        setCheckbox(captchaShowOnLoginCheckbox, false);
        setCheckbox(captchaEnabledCheckbox, false);
        click(saveButton);
        WaitUtils.waitForElement(By.cssSelector("div.wrapper"), WaitType.VISIBLE);
    }

    private void showAdvancedSettings() {
        WebElement toggle = WaitUtils.waitForElement(advancedSettingsToggle, WaitType.PRESENCE);
        if (!toggle.isSelected()) {
            click(By.cssSelector("label[for='advanced-settings-mode']"));
        }
    }
}
