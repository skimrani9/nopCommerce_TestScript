package com.nopcommerce.automation.pages.storefront;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class StorefrontCheckoutPage extends BasePage {

    public void openCheckout() {
        navigateTo(Constants.STORE_CHECKOUT_PATH);
        WaitUtils.waitForElement(By.id("checkout-steps"), WaitType.VISIBLE);
    }

    public String placeGuestOrderWithCheckMoneyOrder() {
        fillBillingAddress("John", "Doe", "123 Test St", "New York", "10001", "United States");
        continueBilling();
        continueShippingIfRequired();
        selectShippingMethodIfRequired();
        selectPaymentMethod(Constants.CHECK_MONEY_ORDER_SYSTEM_NAME);
        continuePaymentMethod();
        continuePaymentInfoIfRequired();
        confirmOrder();
        return extractOrderIdFromCompletedPage();
    }

    public void fillBillingAddress(
            String firstName, String lastName, String address, String city, String zip, String country) {
        openCheckout();
        selectNewBillingAddressIfNeeded();
        type(By.id("BillingNewAddress_FirstName"), firstName);
        type(By.id("BillingNewAddress_LastName"), lastName);
        type(By.id("BillingNewAddress_Email"), "guest." + System.currentTimeMillis() + "@example.com");
        selectDropdownOptionByText(By.id("BillingNewAddress_CountryId"), country);
        waitForAjaxComplete();
        type(By.id("BillingNewAddress_City"), city);
        type(By.id("BillingNewAddress_Address1"), address);
        type(By.id("BillingNewAddress_ZipPostalCode"), zip);
    }

    private void selectNewBillingAddressIfNeeded() {
        By billingSelect = By.id("billing-address-select");
        if (isDisplayed(billingSelect)) {
            selectDropdownOptionByValue(billingSelect, "0");
        }
    }

    private void continueBilling() {
        WaitUtils.waitForElement(By.cssSelector("#opc-billing .step"), WaitType.VISIBLE);
        click(By.cssSelector("#billing-buttons-container .new-address-next-step-button"));
        waitForAjaxComplete();
    }

    private void continueShippingIfRequired() {
        if (!isDisplayedQuick(By.id("shipping-buttons-container"))) {
            return;
        }
        click(By.cssSelector("#shipping-buttons-container .new-address-next-step-button"));
        waitForAjaxComplete();
    }

    private void selectShippingMethodIfRequired() {
        if (!isDisplayedQuick(By.id("shipping-method-buttons-container"))) {
            return;
        }
        By firstShippingOption = By.cssSelector("input[name='shippingoption']");
        if (isDisplayedQuick(firstShippingOption)) {
            click(firstShippingOption);
        }
        click(By.cssSelector("#shipping-method-buttons-container .shipping-method-next-step-button"));
        waitForAjaxComplete();
    }

    private void selectPaymentMethod(String paymentSystemName) {
        By paymentRadio = By.cssSelector("input[name='paymentmethod'][value='" + paymentSystemName + "']");
        if (!isDisplayed(paymentRadio)) {
            paymentRadio = By.cssSelector("input[name='paymentmethod']");
        }
        click(paymentRadio);
    }

    private void continuePaymentMethod() {
        click(By.cssSelector("#payment-method-buttons-container .payment-method-next-step-button"));
        waitForAjaxComplete();
    }

    private void continuePaymentInfoIfRequired() {
        if (isDisplayed(By.cssSelector("#payment-info-buttons-container .payment-info-next-step-button"))) {
            click(By.cssSelector("#payment-info-buttons-container .payment-info-next-step-button"));
            waitForAjaxComplete();
        }
    }

    private void confirmOrder() {
        if (isDisplayed(By.id("termsofservice"))) {
            setCheckbox(By.id("termsofservice"), true);
        }
        click(By.cssSelector("#confirm-order-buttons-container .confirm-order-next-step-button"));
        WaitUtils.waitForUrlContains("checkout/completed");
    }

    private void waitForCheckoutStep(String stepId) {
        WaitUtils.waitForElement(By.id(stepId), WaitType.VISIBLE);
        waitForAjaxComplete();
    }

    private String extractOrderIdFromCompletedPage() {
        By orderDetailsLink = By.xpath("//a[contains(@href,'orderdetails')]");
        WebElement link = WaitUtils.waitForElement(orderDetailsLink, WaitType.VISIBLE);
        String href = link.getAttribute("href");
        String orderIdParam = "orderId=";
        int index = href.indexOf(orderIdParam);
        if (index >= 0) {
            String remainder = href.substring(index + orderIdParam.length());
            int ampIndex = remainder.indexOf('&');
            return ampIndex > 0 ? remainder.substring(0, ampIndex) : remainder;
        }
        return href.replaceAll("\\D+", "");
    }

    public boolean isPaymentMethodAvailable(String paymentSystemName) {
        openCheckout();
        fillBillingAddress("Test", "User", "1 Main St", "New York", "10001", "United States");
        continueBilling();
        continueShippingIfRequired();
        selectShippingMethodIfRequired();
        return isDisplayed(By.cssSelector("input[name='paymentmethod'][value='" + paymentSystemName + "']"));
    }
}
