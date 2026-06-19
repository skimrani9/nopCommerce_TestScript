package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.storefront.StorefrontContactVendorPage;
import com.nopcommerce.automation.pages.storefront.StorefrontVendorPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import com.nopcommerce.automation.utils.VendorStorefrontTestDataHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class VendorStorefrontPublicPageSteps {

    private final StorefrontVendorPage storefrontVendorPage = new StorefrontVendorPage();
    private final StorefrontContactVendorPage storefrontContactVendorPage = new StorefrontContactVendorPage();

    @Given("an active E2E vendor storefront exists")
    public void anActiveE2EVendorStorefrontExists() {
        VendorStorefrontTestDataHelper.ensureActiveVendorStorefront();
    }

    @When("guest opens the E2E vendor public page")
    public void guestOpensTheE2EVendorPublicPage() {
        storefrontVendorPage.openVendorBySeoName(VendorStorefrontTestDataHelper.E2E_VENDOR_SEO);
    }

    @And("guest submits contact vendor enquiry")
    public void guestSubmitsContactVendorEnquiry() {
        AssertionUtils.assertTrue(
                storefrontVendorPage.isContactVendorButtonVisible(),
                "Contact vendor button should be visible on vendor page");
        storefrontVendorPage.openContactVendorPage();
        storefrontContactVendorPage.submitContactForm(
                "E2E Guest",
                "guest.contact@example.com",
                "Wholesale pricing question for E2E vendor");
    }

    @Then("vendor public page displays the E2E vendor shop name")
    public void vendorPublicPageDisplaysTheE2EVendorShopName() {
        AssertionUtils.assertTrue(
                storefrontVendorPage.isVendorPageDisplayed(),
                "Vendor public page should be displayed");
        AssertionUtils.assertTrue(
                storefrontVendorPage.isVendorNameDisplayed(VendorStorefrontTestDataHelper.E2E_VENDOR_SHOP_NAME),
                "Vendor page should display the E2E vendor shop name");
    }

    @Then("vendor public page displays the E2E vendor description")
    public void vendorPublicPageDisplaysTheE2EVendorDescription() {
        AssertionUtils.assertTrue(
                storefrontVendorPage.isVendorDescriptionVisible(),
                "Vendor page should display a shop description");
    }

    @Then("contact vendor success message is displayed")
    public void contactVendorSuccessMessageIsDisplayed() {
        AssertionUtils.assertTrue(
                storefrontContactVendorPage.isContactVendorPageDisplayed(),
                "Contact vendor page should be displayed");
        AssertionUtils.assertTrue(
                storefrontContactVendorPage.isContactSubmissionSuccessDisplayed(),
                "Contact vendor form should show success message after submission");
    }

    @Then("vendor public page shows the E2E updated storefront description")
    public void vendorPublicPageShowsTheE2EUpdatedStorefrontDescription() {
        AssertionUtils.assertTrue(
                storefrontVendorPage.vendorDescriptionContains(
                        VendorStorefrontTestDataHelper.E2E_VENDOR_UPDATED_DESCRIPTION),
                "Vendor public page should show the updated storefront description");
    }
}
