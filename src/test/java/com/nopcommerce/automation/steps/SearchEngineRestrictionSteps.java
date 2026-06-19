package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.storefront.StorefrontCartPage;
import com.nopcommerce.automation.pages.storefront.StorefrontProductPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchEngineRestrictionSteps {

    private final StorefrontProductPage storefrontProductPage = new StorefrontProductPage();
    private final StorefrontCartPage storefrontCartPage = new StorefrontCartPage();

    @And("search engine crawler attempts to add current product to cart")
    public void searchEngineCrawlerAttemptsToAddCurrentProductToCart() {
        storefrontProductPage.attemptAddCurrentProductToCart();
    }

    @And("search engine crawler opens the shopping cart page")
    public void searchEngineCrawlerOpensTheShoppingCartPage() {
        storefrontCartPage.openCart();
    }

    @Then("search engine add to cart is blocked")
    public void searchEngineAddToCartIsBlocked() {
        AssertionUtils.assertTrue(
                storefrontProductPage.isAddToCartBlockedForSearchEngine(),
                "Search engine should be blocked from adding products to cart");
    }

    @Then("search engine shopping cart is empty")
    public void searchEngineShoppingCartIsEmpty() {
        AssertionUtils.assertTrue(
                storefrontCartPage.isCartEmpty(),
                "Search engine shopping cart should remain empty");
    }
}
