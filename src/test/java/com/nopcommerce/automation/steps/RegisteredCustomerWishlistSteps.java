package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.storefront.StorefrontCartPage;
import com.nopcommerce.automation.pages.storefront.StorefrontProductPage;
import com.nopcommerce.automation.pages.storefront.StorefrontWishlistPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import com.nopcommerce.automation.utils.GuestTestDataHelper;
import com.nopcommerce.automation.utils.RegisteredCustomerTestDataHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RegisteredCustomerWishlistSteps {

    private final StorefrontProductPage storefrontProductPage = new StorefrontProductPage();
    private final StorefrontWishlistPage storefrontWishlistPage = new StorefrontWishlistPage();
    private final StorefrontCartPage storefrontCartPage = new StorefrontCartPage();

    @Given("registered customer has E2E guest product in wishlist")
    public void registeredCustomerHasE2EGuestProductInWishlist() {
        RegisteredCustomerTestDataHelper.ensureProductInWishlist();
    }

    @When("registered customer opens the E2E guest product detail page")
    public void registeredCustomerOpensTheE2EGuestProductDetailPage() {
        storefrontProductPage.openProductBySeoName(GuestTestDataHelper.GUEST_PRODUCT_SEO);
    }

    @And("registered customer adds the product to wishlist")
    public void registeredCustomerAddsTheProductToWishlist() {
        storefrontProductPage.addCurrentProductToWishlist();
    }

    @When("registered customer opens the wishlist page")
    public void registeredCustomerOpensTheWishlistPage() {
        storefrontWishlistPage.openWishlist();
    }

    @When("registered customer moves wishlist item to cart")
    public void registeredCustomerMovesWishlistItemToCart() {
        storefrontWishlistPage.openWishlist();
        storefrontWishlistPage.moveFirstWishlistItemToCart();
    }

    @Then("wishlist confirmation is shown")
    public void wishlistConfirmationIsShown() {
        AssertionUtils.assertTrue(
                storefrontProductPage.isWishlistAddConfirmationShown()
                        || storefrontWishlistPage.isProductInWishlist(GuestTestDataHelper.GUEST_PRODUCT_NAME),
                "Adding to wishlist should show confirmation or persist the item");
    }

    @Then("wishlist displays the E2E guest product with price")
    public void wishlistDisplaysTheE2EGuestProductWithPrice() {
        AssertionUtils.assertTrue(
                storefrontWishlistPage.isWishlistPageDisplayed(),
                "Wishlist page should be displayed for authenticated customer");
        AssertionUtils.assertTrue(
                storefrontWishlistPage.isProductInWishlist(GuestTestDataHelper.GUEST_PRODUCT_NAME),
                "Wishlist should contain the E2E guest product");
        AssertionUtils.assertTrue(
                storefrontWishlistPage.isWishlistItemPriceDisplayed(GuestTestDataHelper.GUEST_PRODUCT_NAME),
                "Wishlist item should display a unit price");
    }

    @Then("shopping cart contains the E2E guest product")
    public void shoppingCartContainsTheE2EGuestProduct() {
        storefrontCartPage.openCart();
        AssertionUtils.assertTrue(
                storefrontCartPage.isProductInCart(GuestTestDataHelper.GUEST_PRODUCT_NAME),
                "Shopping cart should contain the product moved from wishlist");
    }
}
