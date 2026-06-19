package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.storefront.StorefrontHomePage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GuestHomeSteps {

    private final StorefrontHomePage storefrontHomePage = new StorefrontHomePage();

    @When("guest navigates to the storefront homepage")
    public void guestNavigatesToTheStorefrontHomepage() {
        storefrontHomePage.openHomePage();
    }

    @Then("homepage displays header footer and main content")
    public void homepageDisplaysHeaderFooterAndMainContent() {
        AssertionUtils.assertTrue(
                storefrontHomePage.isHomePageDisplayed(),
                "Homepage should display header, footer, and main content");
    }

    @And("guest login and register links are visible")
    public void guestLoginAndRegisterLinksAreVisible() {
        AssertionUtils.assertTrue(
                storefrontHomePage.areGuestHeaderLinksVisible(),
                "Guest login and register links should be visible in header");
    }

    @And("guest opens the HTML sitemap page")
    public void guestOpensTheHtmlSitemapPage() {
        storefrontHomePage.openSitemap();
    }

    @Then("sitemap page displays category and product links")
    public void sitemapPageDisplaysCategoryAndProductLinks() {
        AssertionUtils.assertTrue(
                storefrontHomePage.isSitemapPageDisplayed(),
                "Sitemap page should display grouped entity links");
    }

    @Then("guest is browsing as an anonymous visitor")
    public void guestIsBrowsingAsAnAnonymousVisitor() {
        AssertionUtils.assertTrue(
                storefrontHomePage.isGuestSession(),
                "Guest should see login link and not be logged in");
    }
}
