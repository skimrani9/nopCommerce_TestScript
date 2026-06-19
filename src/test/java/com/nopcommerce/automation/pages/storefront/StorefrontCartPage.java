package com.nopcommerce.automation.pages.storefront;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class StorefrontCartPage extends BasePage {

    private final By checkoutButton = By.id("checkout");
    private final By discountCouponCodeInput = By.id("discountcouponcode");
    private final By applyDiscountButton = By.name("applydiscountcouponcode");
    private final By orderTotal = By.cssSelector(".order-total .value-summary, .order-total .product-price");
    private final By cartForm = By.cssSelector("#shopping-cart-form, .shopping-cart-page");
    private final By emptyCartMessage = By.cssSelector(".order-summary-content");

    public void openCart() {
        navigateTo(Constants.STORE_CART_PATH);
        WaitUtils.waitForElement(By.cssSelector(".shopping-cart-page, .cart"), WaitType.VISIBLE);
    }

    public void proceedToCheckout() {
        acceptTermsOfServiceIfPresent();
        click(checkoutButton);
        try {
            WaitUtils.waitForUrlContains("checkout");
        } catch (Exception exception) {
            WaitUtils.waitForElement(By.cssSelector("#checkout-steps, .checkout-page, .opc"), WaitType.VISIBLE);
        }
    }

    private void acceptTermsOfServiceIfPresent() {
        By termsCheckbox = By.id("termsofservice");
        if (isDisplayedQuick(termsCheckbox)) {
            setCheckbox(termsCheckbox, true);
        }
    }

    public void applyDiscountCoupon(String couponCode) {
        openCart();
        type(discountCouponCodeInput, couponCode);
        click(applyDiscountButton);
        waitForAjaxComplete();
    }

    public String getOrderTotalText() {
        return getText(orderTotal);
    }

    public boolean isDiscountApplied() {
        return isDisplayed(By.cssSelector(".cart-discount, .discount-box .message-success"));
    }

    public boolean isDiscountRejected() {
        return isDisplayed(By.cssSelector(".coupon-box .message-failure, .discount-box .message-failure, .message-error"));
    }

    public void applyDiscountCouponOnCurrentCartPage(String couponCode) {
        type(discountCouponCodeInput, couponCode);
        click(applyDiscountButton);
        waitForAjaxComplete();
        WaitUtils.waitForElement(By.cssSelector(".coupon-box, .shopping-cart-page"), WaitType.VISIBLE);
    }

    public boolean isCartPageWithItemsDisplayed() {
        openCart();
        return isDisplayed(By.cssSelector("table.cart .product-name"))
                && isDisplayed(By.cssSelector(".order-summary-content, #shopping-cart-form"));
    }

    public boolean isProductInCart(String productName) {
        By productNameCell = By.xpath("//table[contains(@class,'cart')]//a[contains(@class,'product-name')"
                + " and contains(normalize-space(.), '" + productName + "')]");
        return isDisplayed(productNameCell);
    }

    public boolean isCheckoutButtonVisible() {
        return isDisplayed(checkoutButton);
    }

    public boolean isCartEmpty() {
        openCart();
        if (isDisplayedQuick(By.cssSelector("table.cart .product-name"))) {
            return false;
        }
        return isDisplayedQuick(emptyCartMessage)
                || isDisplayedQuick(By.cssSelector(".no-data, .cart-empty, .order-summary-content"))
                || !isDisplayedQuick(By.cssSelector("#shopping-cart-form table.cart tbody tr"));
    }

    public void addProductToCartFromDetails(String productId) {
        navigateTo("/");
        By addToCartButton = By.id("add-to-cart-button-" + productId);
        if (!isDisplayed(addToCartButton)) {
            throw new IllegalStateException("Add to cart button not found for product id: " + productId);
        }
        click(addToCartButton);
        waitForAjaxComplete();
    }
}
