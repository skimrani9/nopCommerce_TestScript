package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.admin.AdminPaymentMethodsPage;
import com.nopcommerce.automation.pages.admin.AdminPaymentRestrictionsPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminPaymentSteps {

    private final AdminPaymentMethodsPage paymentMethodsPage = new AdminPaymentMethodsPage();
    private final AdminPaymentRestrictionsPage paymentRestrictionsPage = new AdminPaymentRestrictionsPage();

    @Given("payment method {string} is active")
    public void paymentMethodIsActive(String systemName) {
        paymentMethodsPage.setPaymentMethodActive(systemName, true);
    }

    @When("admin activates payment method {string}")
    public void adminActivatesPaymentMethod(String systemName) {
        paymentMethodsPage.setPaymentMethodActive(systemName, true);
    }

    @When("admin deactivates payment method {string}")
    public void adminDeactivatesPaymentMethod(String systemName) {
        paymentMethodsPage.setPaymentMethodActive(systemName, false);
    }

    @When("admin opens payment restrictions page")
    public void adminOpensPaymentRestrictionsPage() {
        paymentRestrictionsPage.openPaymentRestrictions();
    }

    @When("admin opens payment methods page")
    public void adminOpensPaymentMethodsPage() {
        paymentMethodsPage.openPaymentMethods();
    }

    @Then("payment method {string} is active in admin")
    public void paymentMethodIsActiveInAdmin(String systemName) {
        AssertionUtils.assertTrue(
                paymentMethodsPage.isPaymentMethodActive(systemName),
                "Payment method should be active in admin");
    }

    @Then("payment method {string} is inactive in admin")
    public void paymentMethodIsInactiveInAdmin(String systemName) {
        AssertionUtils.assertFalse(
                paymentMethodsPage.isPaymentMethodActive(systemName),
                "Payment method should be inactive in admin");
    }

    @Then("payment restrictions page is displayed")
    public void paymentRestrictionsPageIsDisplayed() {
        AssertionUtils.assertTrue(
                paymentRestrictionsPage.isRestrictionsPageDisplayed(),
                "Payment restrictions page should be displayed");
    }

    @Then("payment method {string} is listed in payment methods grid")
    public void paymentMethodIsListedInGrid(String systemName) {
        AssertionUtils.assertTrue(
                paymentMethodsPage.isPaymentMethodListed(systemName),
                "Payment method should be listed in payment methods grid");
    }

    @Then("payment methods grid is displayed")
    public void paymentMethodsGridIsDisplayed() {
        AssertionUtils.assertTrue(
                paymentMethodsPage.isPaymentMethodsGridDisplayed(),
                "Payment methods grid should be displayed");
    }
}
