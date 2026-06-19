package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.admin.AdminShippingMethodsPage;
import com.nopcommerce.automation.pages.admin.AdminShippingProvidersPage;
import com.nopcommerce.automation.pages.admin.AdminShippingRestrictionsPage;
import com.nopcommerce.automation.pages.admin.AdminWarehousePage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminShippingSteps {

    private final AdminShippingMethodsPage shippingMethodsPage = new AdminShippingMethodsPage();
    private final AdminShippingRestrictionsPage shippingRestrictionsPage = new AdminShippingRestrictionsPage();
    private final AdminWarehousePage warehousePage = new AdminWarehousePage();
    private final AdminShippingProvidersPage shippingProvidersPage = new AdminShippingProvidersPage();

    @When("admin opens shipping methods page")
    public void adminOpensShippingMethodsPage() {
        shippingMethodsPage.openShippingMethods();
    }

    @When("admin opens shipping restrictions page")
    public void adminOpensShippingRestrictionsPage() {
        shippingRestrictionsPage.openShippingRestrictions();
    }

    @When("admin creates warehouse {string} with city {string}")
    public void adminCreatesWarehouse(String warehouseName, String city) {
        warehousePage.clickAddNewWarehouse();
        warehousePage.fillAndSaveWarehouse(warehouseName, city);
    }

    @When("admin creates shipping method {string}")
    public void adminCreatesShippingMethod(String methodName) {
        shippingMethodsPage.openShippingMethods();
        shippingMethodsPage.clickAddNewMethod();
        shippingMethodsPage.fillAndSaveShippingMethod(methodName, "E2E shipping method");
    }

    @When("admin opens shipping providers page")
    public void adminOpensShippingProvidersPage() {
        shippingProvidersPage.openShippingProviders();
    }

    @Then("shipping methods grid is displayed")
    public void shippingMethodsGridIsDisplayed() {
        AssertionUtils.assertTrue(
                shippingMethodsPage.isShippingMethodListed("Ground")
                        || shippingMethodsPage.isShippingMethodListed("E2E"),
                "Shipping methods grid should be displayed");
    }

    @Then("shipping restrictions page is displayed")
    public void shippingRestrictionsPageIsDisplayed() {
        AssertionUtils.assertTrue(
                shippingRestrictionsPage.isRestrictionsPageDisplayed(),
                "Shipping restrictions page should be displayed");
    }

    @Then("warehouse {string} appears in warehouse list")
    public void warehouseAppearsInWarehouseList(String warehouseName) {
        AssertionUtils.assertTrue(
                warehousePage.isWarehouseListed(warehouseName),
                "Warehouse should appear in warehouse list");
    }

    @Then("shipping method {string} appears in shipping methods list")
    public void shippingMethodAppearsInList(String methodName) {
        AssertionUtils.assertTrue(
                shippingMethodsPage.isShippingMethodListed(methodName),
                "Shipping method should appear in shipping methods list");
    }

    @Then("shipping providers grid is displayed")
    public void shippingProvidersGridIsDisplayed() {
        AssertionUtils.assertTrue(
                shippingProvidersPage.isShippingProvidersGridDisplayed(),
                "Shipping providers grid should be displayed");
    }
}
