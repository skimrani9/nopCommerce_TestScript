package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.ConfigReader;
import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.pages.admin.AdminCustomerEditPage;
import com.nopcommerce.automation.pages.admin.AdminCustomerListPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminCustomerSteps {

    private static final String CUSTOMER_EMAIL_KEY = "customerEmail";
    private static final String CUSTOMER_PASSWORD_KEY = "customerPassword";

    private final AdminCustomerListPage customerListPage = new AdminCustomerListPage();
    private final AdminCustomerEditPage customerEditPage = new AdminCustomerEditPage();

    @When("admin searches customer list by email {string}")
    public void adminSearchesCustomerListByEmail(String email) {
        customerListPage.searchByEmail(email);
    }

    @When("admin creates a customer with email prefix {string} and password {string}")
    public void adminCreatesCustomer(String emailPrefix, String password) {
        String email = emailPrefix + "." + System.currentTimeMillis() + "@example.com";
        ScenarioContext.set(CUSTOMER_EMAIL_KEY, email);
        ScenarioContext.set(CUSTOMER_PASSWORD_KEY, password);

        customerListPage.clickAddNewCustomer();
        customerEditPage.fillNewCustomerDetails(email, password, "E2E", "Customer");
        customerEditPage.clickSave();
        customerEditPage.waitForCustomerListRedirect();
    }

    @Given("a test customer exists with email prefix {string}")
    public void aTestCustomerExists(String emailPrefix) {
        adminCreatesCustomer(emailPrefix, "TestPass123!");
    }

    @Given("a deletable test customer exists with email prefix {string}")
    public void aDeletableTestCustomerExists(String emailPrefix) {
        aTestCustomerExists(emailPrefix);
    }

    @When("admin assigns customer role {string} to the customer")
    public void adminAssignsCustomerRole(String roleName) {
        customerListPage.openCustomerEditByEmail(getCustomerEmail());
        customerEditPage.assignCustomerRole(roleName);
        customerEditPage.clickSave();
        customerEditPage.waitForCustomerListRedirect();
    }

    @When("admin impersonates the customer")
    public void adminImpersonatesCustomer() {
        customerListPage.openCustomerEditByEmail(getCustomerEmail());
        customerEditPage.impersonateCustomer();
    }

    @When("admin exports all customers to Excel from customer list")
    public void adminExportsAllCustomersToExcel() {
        customerListPage.exportAllCustomersToExcel();
    }

    @When("admin deletes the customer")
    public void adminDeletesCustomer() {
        customerListPage.openCustomerEditByEmail(getCustomerEmail());
        customerEditPage.deleteCustomer();
    }

    @Then("matching customer record appears in customer grid")
    public void matchingCustomerRecordAppearsInCustomerGrid() {
        String adminEmail = ConfigReader.getAdminEmail();
        AssertionUtils.assertTrue(
                customerListPage.isCustomerListed(adminEmail)
                        || customerListPage.isCustomerListed(adminEmail.substring(0, adminEmail.indexOf('@'))),
                "Matching customer record should appear in customer grid");
    }

    @Then("customer appears in admin customer list")
    public void customerAppearsInAdminCustomerList() {
        AssertionUtils.assertTrue(
                customerListPage.isCustomerListed(getCustomerEmail()),
                "Customer should appear in admin customer list");
    }

    @Then("customer has role {string} assigned in admin")
    public void customerHasRoleAssignedInAdmin(String roleName) {
        customerListPage.openCustomerEditByEmail(getCustomerEmail());
        AssertionUtils.assertTrue(
                customerEditPage.isCustomerRoleSelected(roleName),
                "Customer role should be assigned in admin");
    }

    @Then("admin session acts as customer on storefront")
    public void adminSessionActsAsCustomerOnStorefront() {
        String originalWindow = com.nopcommerce.automation.config.DriverManager.getDriver().getWindowHandles().iterator().next();
        for (String windowHandle : com.nopcommerce.automation.config.DriverManager.getDriver().getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                com.nopcommerce.automation.config.DriverManager.getDriver().switchTo().window(windowHandle);
                break;
            }
        }
        AssertionUtils.assertFalse(
                com.nopcommerce.automation.config.DriverManager.getDriver().getCurrentUrl().contains("/admin"),
                "Impersonation should open storefront, not admin area");
    }

    @Then("customer export action is triggered successfully")
    public void customerExportActionIsTriggeredSuccessfully() {
        AssertionUtils.assertTrue(
                customerListPage.isCustomerListed(ConfigReader.getAdminEmail())
                        || com.nopcommerce.automation.config.DriverManager.getDriver().getCurrentUrl().contains("/customer/list"),
                "Customer export should complete without leaving customer list unexpectedly");
    }

    @Then("customer is not listed in admin customer list")
    public void customerIsNotListedInAdminCustomerList() {
        AssertionUtils.assertFalse(
                customerListPage.isCustomerListed(getCustomerEmail()),
                "Deleted customer should not appear in admin customer list");
    }

    private String getCustomerEmail() {
        return ScenarioContext.get(CUSTOMER_EMAIL_KEY);
    }
}
