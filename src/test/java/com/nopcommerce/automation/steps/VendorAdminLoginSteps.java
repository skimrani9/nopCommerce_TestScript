package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.pages.admin.AdminDashboardPage;
import com.nopcommerce.automation.pages.customer.LoginPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import com.nopcommerce.automation.utils.VendorAdminTestDataHelper;
import com.nopcommerce.automation.utils.VendorStorefrontTestDataHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class VendorAdminLoginSteps {

    private static final String VENDOR_ADMIN_EMAIL_KEY = "vendorAdminEmail";
    private static final String VENDOR_ADMIN_PASSWORD_KEY = "vendorAdminPassword";

    private final LoginPage loginPage = new LoginPage();
    private final AdminDashboardPage adminDashboardPage = new AdminDashboardPage();

    @Given("a vendor admin account exists")
    public void aVendorAdminAccountExists() {
        VendorAdminTestDataHelper.ensureVendorAdminAccount();
        storeVendorAdminCredentials();
    }

    @Given("vendor admin is logged into the admin panel")
    public void vendorAdminIsLoggedIntoTheAdminPanel() {
        VendorAdminTestDataHelper.ensureVendorAdminAccount();
        storeVendorAdminCredentials();
        loginPage.openLoginPageWithAdminReturnUrl();
        loginPage.login(
                VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_EMAIL,
                VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_PASSWORD);
        AssertionUtils.assertFalse(
                loginPage.isLoginErrorDisplayed(),
                "Vendor admin login should succeed");
        adminDashboardPage.waitForDashboardLoad();
    }

    @When("vendor admin opens the admin login page")
    public void vendorAdminOpensTheAdminLoginPage() {
        loginPage.openLoginPageWithAdminReturnUrl();
    }

    @And("vendor admin submits valid credentials")
    public void vendorAdminSubmitsValidCredentials() {
        loginPage.login(
                VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_EMAIL,
                VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_PASSWORD);
    }

    @When("vendor admin navigates to the dashboard home page")
    public void vendorAdminNavigatesToTheDashboardHomePage() {
        adminDashboardPage.openDashboard();
        adminDashboardPage.waitForDashboardLoad();
    }

    @Then("vendor admin is redirected to the admin dashboard")
    public void vendorAdminIsRedirectedToTheAdminDashboard() {
        AssertionUtils.assertFalse(
                loginPage.isLoginErrorDisplayed(),
                "Vendor admin login should not show an error");
        AssertionUtils.assertTrue(
                adminDashboardPage.isDashboardDisplayed(),
                "Vendor admin should reach the admin dashboard");
    }

    @Then("vendor admin dashboard page is displayed")
    public void vendorAdminDashboardPageIsDisplayed() {
        AssertionUtils.assertTrue(
                adminDashboardPage.isDashboardDisplayed(),
                "Vendor admin dashboard heading should be visible");
        AssertionUtils.assertTrue(
                adminDashboardPage.currentUrl().contains(Constants.ADMIN_AREA_PATH),
                "Vendor admin should remain in the admin area");
    }

    private void storeVendorAdminCredentials() {
        ScenarioContext.set(VENDOR_ADMIN_EMAIL_KEY, VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_EMAIL);
        ScenarioContext.set(VENDOR_ADMIN_PASSWORD_KEY, VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_PASSWORD);
    }
}
