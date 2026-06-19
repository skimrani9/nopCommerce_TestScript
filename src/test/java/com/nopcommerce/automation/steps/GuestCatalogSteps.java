package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.storefront.StorefrontCategoryPage;
import com.nopcommerce.automation.pages.storefront.StorefrontProductPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class GuestCatalogSteps {

    private final StorefrontProductPage storefrontProductPage = new StorefrontProductPage();
    private final StorefrontCategoryPage storefrontCategoryPage = new StorefrontCategoryPage();

    @And("guest searches storefront for {string}")
    public void guestSearchesStorefrontFor(String searchTerm) {
        storefrontProductPage.searchFromHeader(searchTerm);
    }

    @Then("search results list matching products")
    public void searchResultsListMatchingProducts() {
        AssertionUtils.assertTrue(
                storefrontProductPage.hasSearchResults(),
                "Search results should list matching products");
    }

    @Then("search results show no products found message")
    public void searchResultsShowNoProductsFoundMessage() {
        AssertionUtils.assertTrue(
                storefrontProductPage.hasNoSearchResults(),
                "Search should display a no-results message");
    }

    @And("guest opens category {string}")
    public void guestOpensCategory(String seoName) {
        storefrontCategoryPage.openCategoryBySeoName(seoName);
    }

    @Then("category page displays {string} with product grid")
    public void categoryPageDisplaysWithProductGrid(String categoryName) {
        AssertionUtils.assertTrue(
                storefrontCategoryPage.isCategoryPageDisplayed(categoryName),
                "Category page should display category heading: " + categoryName);
        AssertionUtils.assertTrue(
                storefrontCategoryPage.hasProductGrid(),
                "Category page should display a product grid");
    }
}
