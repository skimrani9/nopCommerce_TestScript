package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.storefront.StorefrontCategoryPage;
import com.nopcommerce.automation.pages.storefront.StorefrontHomePage;
import com.nopcommerce.automation.pages.storefront.StorefrontProductPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class SearchEngineBrowsingSteps {

    private final StorefrontHomePage storefrontHomePage = new StorefrontHomePage();
    private final StorefrontProductPage storefrontProductPage = new StorefrontProductPage();
    private final StorefrontCategoryPage storefrontCategoryPage = new StorefrontCategoryPage();

    @Given("search engine crawler session is active")
    public void searchEngineCrawlerSessionIsActive() {
        // Crawler user agent is applied in Hooks before WebDriver initialization.
    }

    @When("search engine crawler navigates to the storefront homepage")
    public void searchEngineCrawlerNavigatesToTheStorefrontHomepage() {
        storefrontHomePage.openHomePage();
    }

    @When("search engine crawler searches storefront for {string}")
    public void searchEngineCrawlerSearchesStorefrontFor(String searchTerm) {
        storefrontProductPage.openSearchResults(searchTerm);
    }

    @When("search engine crawler opens category {string}")
    public void searchEngineCrawlerOpensCategory(String seoName) {
        storefrontCategoryPage.openCategoryBySeoName(seoName);
    }

    @When("search engine crawler opens product {string}")
    public void searchEngineCrawlerOpensProduct(String seoName) {
        storefrontProductPage.openProductBySeoName(seoName);
    }
}
