package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.pages.admin.AdminAccessDeniedPage;
import com.nopcommerce.automation.pages.admin.AdminLayoutPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class VendorAdminAccessSteps {

    private final AdminAccessDeniedPage adminAccessDeniedPage = new AdminAccessDeniedPage();
    private final AdminLayoutPage adminLayoutPage = new AdminLayoutPage();

    @When("vendor admin navigates to the customer list page")
    public void vendorAdminNavigatesToTheCustomerListPage() {
        adminAccessDeniedPage.openRestrictedAdminPath(Constants.ADMIN_CUSTOMER_LIST_PATH);
    }

    @When("vendor admin navigates to the general settings page")
    public void vendorAdminNavigatesToTheGeneralSettingsPage() {
        adminAccessDeniedPage.openRestrictedAdminPath(Constants.ADMIN_GENERAL_SETTINGS_PATH);
    }

    @Then("vendor admin access denied page is displayed")
    public void vendorAdminAccessDeniedPageIsDisplayed() {
        AssertionUtils.assertTrue(
                adminAccessDeniedPage.isAccessDeniedPageDisplayed(),
                "Vendor admin should see access denied for restricted admin areas");
    }

    @Then("vendor admin sidebar shows catalog menu")
    public void vendorAdminSidebarShowsCatalogMenu() {
        AssertionUtils.assertTrue(
                adminLayoutPage.isSidebarMenuVisible("Catalog"),
                "Vendor admin sidebar should include Catalog menu");
    }

    @And("vendor admin sidebar does not show configuration menu")
    public void vendorAdminSidebarDoesNotShowConfigurationMenu() {
        AssertionUtils.assertFalse(
                adminLayoutPage.isSidebarMenuVisible("Configuration"),
                "Vendor admin sidebar should not include Configuration menu");
    }
}
