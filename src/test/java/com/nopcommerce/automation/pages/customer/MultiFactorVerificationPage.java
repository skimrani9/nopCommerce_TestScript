package com.nopcommerce.automation.pages.customer;



import com.nopcommerce.automation.constants.Constants;

import com.nopcommerce.automation.constants.WaitType;

import com.nopcommerce.automation.pages.BasePage;

import com.nopcommerce.automation.utils.WaitUtils;

import org.openqa.selenium.By;



public class MultiFactorVerificationPage extends BasePage {



    private final By tokenInput = By.cssSelector("input#Token, input[name='Token']");

    private final By submitButton = By.cssSelector("#googleAuthSendCode_submit, button[type='submit']");

    private final By validationError = By.cssSelector(".field-validation-error, .message-error, .validation-summary-errors");



    public boolean isMultiFactorVerificationPageDisplayed() {

        return getCurrentUrl().toLowerCase().contains(Constants.MULTI_FACTOR_VERIFICATION_PATH)

                && isDisplayed(tokenInput);

    }



    public void submitVerificationCode(String code) {

        WaitUtils.waitForElement(tokenInput, WaitType.VISIBLE);

        type(tokenInput, code);

        click(submitButton);

    }



    public boolean isVerificationErrorDisplayed() {

        WaitUtils.waitForUrlContains(Constants.MULTI_FACTOR_VERIFICATION_PATH);

        return isMultiFactorVerificationPageDisplayed()

                && (isDisplayed(validationError) || isDisplayed(By.cssSelector(".message-error")));

    }

}

