package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.pages.customer.LoginPage;
import com.nopcommerce.automation.pages.vendor.VendorInfoPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import com.nopcommerce.automation.utils.VendorStorefrontTestDataHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class VendorStorefrontProfileSteps {

    private static final String CUSTOMER_EMAIL_KEY = "customerEmail";
    private static final String CUSTOMER_PASSWORD_KEY = "customerPassword";

    private final VendorInfoPage vendorInfoPage = new VendorInfoPage();
    private final LoginPage loginPage = new LoginPage();

    @Given("E2E vendor owner is logged into the storefront")
    public void e2EVendorOwnerIsLoggedIntoTheStorefront() {
        ScenarioContext.set(CUSTOMER_EMAIL_KEY, VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_EMAIL);
        ScenarioContext.set(CUSTOMER_PASSWORD_KEY, VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_PASSWORD);
        loginPage.openLoginPage();
        loginPage.login(
                VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_EMAIL,
                VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_PASSWORD);
        AssertionUtils.assertFalse(
                loginPage.isLoginErrorDisplayed(),
                "E2E vendor owner login should succeed");
    }

    @When("vendor opens the vendor info page")
    public void vendorOpensTheVendorInfoPage() {
        vendorInfoPage.openVendorInfoPage();
    }

    @When("guest navigates to the vendor info page directly")
    public void guestNavigatesToTheVendorInfoPageDirectly() {
        vendorInfoPage.navigateToVendorInfoPage();
    }

    @And("vendor updates description to the E2E updated storefront text")
    public void vendorUpdatesDescriptionToTheE2EUpdatedStorefrontText() {
        vendorInfoPage.updateDescription(VendorStorefrontTestDataHelper.E2E_VENDOR_UPDATED_DESCRIPTION);
    }

    @And("vendor profile description is set to the E2E updated storefront text")
    public void vendorProfileDescriptionIsSetToTheE2EUpdatedStorefrontText() {
        vendorInfoPage.openVendorInfoPage();
        vendorInfoPage.updateDescription(VendorStorefrontTestDataHelper.E2E_VENDOR_UPDATED_DESCRIPTION);
    }

    @Then("vendor profile update success is displayed")
    public void vendorProfileUpdateSuccessIsDisplayed() {
        AssertionUtils.assertTrue(
                vendorInfoPage.isVendorInfoPageDisplayed(),
                "Vendor info page should remain available after save");
        AssertionUtils.assertTrue(
                vendorInfoPage.getDescriptionValue()
                        .contains(VendorStorefrontTestDataHelper.E2E_VENDOR_UPDATED_DESCRIPTION),
                "Vendor info page should persist the updated description");
    }
}
