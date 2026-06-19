package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.pages.vendor.VendorApplyPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import com.nopcommerce.automation.utils.VendorStorefrontTestDataHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class VendorStorefrontApplicationSteps {

    private static final String CUSTOMER_EMAIL_KEY = "customerEmail";
    private static final String CUSTOMER_PASSWORD_KEY = "customerPassword";

    private final VendorApplyPage vendorApplyPage = new VendorApplyPage();

    @Given("a registered customer without a vendor account exists")
    public void aRegisteredCustomerWithoutAVendorAccountExists() {
        String email = VendorStorefrontTestDataHelper.createFreshVendorApplicantCustomer();
        ScenarioContext.set(CUSTOMER_EMAIL_KEY, email);
        ScenarioContext.set(CUSTOMER_PASSWORD_KEY, VendorStorefrontTestDataHelper.E2E_VENDOR_PASSWORD);
    }

    @Given("an E2E vendor applicant with submitted application exists")
    public void anE2EVendorApplicantWithSubmittedApplicationExists() {
        VendorStorefrontTestDataHelper.ensureAppliedVendorApplicant();
        ScenarioContext.set(CUSTOMER_EMAIL_KEY, VendorStorefrontTestDataHelper.E2E_VENDOR_APPLICANT_EMAIL);
        ScenarioContext.set(CUSTOMER_PASSWORD_KEY, VendorStorefrontTestDataHelper.E2E_VENDOR_APPLICANT_PASSWORD);
    }

    @When("customer opens the vendor application page")
    public void customerOpensTheVendorApplicationPage() {
        vendorApplyPage.openApplyPage();
    }

    @When("guest navigates to the vendor application page directly")
    public void guestNavigatesToTheVendorApplicationPageDirectly() {
        vendorApplyPage.navigateToApplyPage();
    }

    @When("customer submits vendor application with shop details")
    public void customerSubmitsVendorApplicationWithShopDetails() {
        vendorApplyPage.submitApplication(
                VendorStorefrontTestDataHelper.E2E_VENDOR_SHOP_NAME + " Apply",
                VendorStorefrontTestDataHelper.E2E_VENDOR_SHOP_EMAIL,
                VendorStorefrontTestDataHelper.E2E_VENDOR_DESCRIPTION);
    }

    @Then("vendor application success message is displayed")
    public void vendorApplicationSuccessMessageIsDisplayed() {
        AssertionUtils.assertTrue(
                vendorApplyPage.isApplicationResultDisplayed(),
                "Vendor application should show a submitted success message");
    }

    @Then("vendor application pending message is displayed")
    public void vendorApplicationPendingMessageIsDisplayed() {
        AssertionUtils.assertTrue(
                vendorApplyPage.isApplicationResultDisplayed(),
                "Vendor application page should show pending/already applied message");
        AssertionUtils.assertFalse(
                vendorApplyPage.isApplyFormDisplayed(),
                "Vendor application form should be hidden after application exists");
    }
}
