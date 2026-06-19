package com.nopcommerce.automation.pages.customer;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private final By emailInput = By.cssSelector("input.email");
    private final By passwordInput = By.cssSelector("input.password");
    private final By loginButton = By.cssSelector("button.login-button");
    private final By loginErrorSummary = By.cssSelector(".message-error");
    private final By loginPageMarker = By.cssSelector(".html-login-page");
    private final By captchaContainer = By.cssSelector(".captcha-box, .g-recaptcha, iframe[title*='reCAPTCHA']");

    public void openLoginPage() {
        navigateTo(Constants.LOGIN_PATH);
    }

    public void openLoginPageWithAdminReturnUrl() {
        navigateTo(Constants.LOGIN_PATH + "?ReturnUrl=" + Constants.ADMIN_AREA_PATH);
    }

    public void enterEmail(String email) {
        type(emailInput, email);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        completeCaptchaIfPresent();
        clickLoginButton();
    }

    public void completeCaptchaIfPresent() {
        if (!isDisplayedQuick(captchaContainer)) {
            return;
        }
        org.openqa.selenium.JavascriptExecutor executor = (org.openqa.selenium.JavascriptExecutor) driver;
        executor.executeScript(
                "var response=document.querySelector('textarea[name=\"g-recaptcha-response\"]');"
                        + "if(response){response.style.display='block';response.value='test-captcha-token';}");
    }

    public void loginWithoutCompletingCaptcha(String email, String password) {
        clearCaptchaResponseIfPresent();
        login(email, password);
    }

    private void clearCaptchaResponseIfPresent() {
        if (!isCaptchaDisplayed()) {
            return;
        }
        org.openqa.selenium.JavascriptExecutor executor = (org.openqa.selenium.JavascriptExecutor) driver;
        executor.executeScript(
                "var captchaResponse = document.querySelector('textarea[name=\"g-recaptcha-response\"]');"
                        + "if (captchaResponse) { captchaResponse.value=''; }");
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginPageMarker);
    }

    public boolean isLoginErrorDisplayed() {
        return isDisplayed(loginErrorSummary);
    }

    public String getLoginErrorText() {
        return getText(loginErrorSummary);
    }

    public boolean isCaptchaDisplayed() {
        return isDisplayed(captchaContainer);
    }

    public boolean waitForAdminRedirect() {
        return WaitUtils.waitForUrlContains(Constants.ADMIN_AREA_PATH);
    }
}
