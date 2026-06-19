package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.storefront.StorefrontCartPage;
import com.nopcommerce.automation.pages.storefront.StorefrontProductPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import com.nopcommerce.automation.utils.GuestTestDataHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class GuestCartSteps {

    private final StorefrontProductPage storefrontProductPage = new StorefrontProductPage();
    private final StorefrontCartPage storefrontCartPage = new StorefrontCartPage();

    @And("guest adds product {string} to cart from search")
    public void guestAddsProductToCartFromSearch(String productName) {
        storefrontProductPage.openProductBySeoName(GuestTestDataHelper.GUEST_PRODUCT_SEO);
        AssertionUtils.assertTrue(
                storefrontProductPage.isProductDetailsPageDisplayed(productName),
                "Product details page should display: " + productName);
        storefrontProductPage.addCurrentProductToCart();
    }

    @And("guest opens the shopping cart page")
    public void guestOpensTheShoppingCartPage() {
        storefrontProductPage.waitForAddToCartSuccess();
        storefrontCartPage.openCart();
    }

    @Then("cart displays product {string} with totals")
    public void cartDisplaysProductWithTotals(String productName) {
        AssertionUtils.assertTrue(
                storefrontCartPage.isCartPageWithItemsDisplayed(),
                "Cart page should display line items and totals");
        AssertionUtils.assertTrue(
                storefrontCartPage.isProductInCart(productName),
                "Cart should contain product: " + productName);
    }

    @And("guest clicks checkout from cart")
    public void guestClicksCheckoutFromCart() {
        AssertionUtils.assertTrue(
                storefrontCartPage.isCheckoutButtonVisible(),
                "Checkout button should be visible on cart page");
        storefrontCartPage.proceedToCheckout();
    }

    @Then("guest is redirected to checkout page")
    public void guestIsRedirectedToCheckoutPage() {
        AssertionUtils.assertTrue(
                storefrontCartPage.currentUrl().contains("/checkout"),
                "Guest should be redirected to checkout page");
    }

    @And("guest applies discount coupon {string} on cart page")
    public void guestAppliesDiscountCouponOnCartPage(String couponCode) {
        storefrontCartPage.openCart();
        storefrontCartPage.applyDiscountCouponOnCurrentCartPage(couponCode);
    }

    @Then("invalid discount coupon error is displayed")
    public void invalidDiscountCouponErrorIsDisplayed() {
        AssertionUtils.assertTrue(
                storefrontCartPage.isDiscountRejected(),
                "Invalid discount coupon should display an error message");
    }
}
