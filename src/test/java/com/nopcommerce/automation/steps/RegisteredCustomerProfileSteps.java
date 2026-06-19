package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.customer.CustomerInfoPage;
import com.nopcommerce.automation.pages.customer.LoginPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import com.nopcommerce.automation.utils.RegisteredCustomerTestDataHelper;
import com.nopcommerce.automation.utils.WaitUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;

public class RegisteredCustomerProfileSteps {

    private final CustomerInfoPage customerInfoPage = new CustomerInfoPage();
    private final LoginPage loginPage = new LoginPage();

    @When("registered customer opens the customer info page")
    public void registeredCustomerOpensTheCustomerInfoPage() {
        customerInfoPage.openCustomerInfoPage();
    }

    @When("guest navigates to the customer info page directly")
    public void guestNavigatesToTheCustomerInfoPageDirectly() {
        customerInfoPage.navigateToCustomerInfoPage();
    }

    @And("registered customer updates first name to {string} and last name to {string}")
    public void registeredCustomerUpdatesFirstNameAndLastName(String firstName, String lastName) {
        customerInfoPage.updateFirstNameAndLastName(firstName, lastName);
    }

    @Then("customer info page displays profile fields with current values")
    public void customerInfoPageDisplaysProfileFieldsWithCurrentValues() {
        AssertionUtils.assertTrue(
                customerInfoPage.isCustomerInfoPageDisplayed(),
                "Customer info page should display profile fields");
        AssertionUtils.assertTrue(
                customerInfoPage.getEmailValue().equalsIgnoreCase(RegisteredCustomerTestDataHelper.E2E_REGISTERED_EMAIL),
                "Customer info page should show the registered customer email");
    }

    @Then("profile update success is displayed")
    public void profileUpdateSuccessIsDisplayed() {
        AssertionUtils.assertTrue(
                customerInfoPage.isUpdateSuccessDisplayed(),
                "Profile update should show a success notification");
    }

    @And("customer info shows first name {string} and last name {string}")
    public void customerInfoShowsFirstNameAndLastName(String firstName, String lastName) {
        AssertionUtils.assertTrue(
                firstName.equals(customerInfoPage.getFirstNameValue()),
                "First name should be updated to " + firstName);
        AssertionUtils.assertTrue(
                lastName.equals(customerInfoPage.getLastNameValue()),
                "Last name should be updated to " + lastName);
    }

    @Then("guest is redirected to the login page")
    public void guestIsRedirectedToTheLoginPage() {
        WaitUtils.waitForElement(By.cssSelector(".html-login-page"), WaitType.VISIBLE);
        AssertionUtils.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "Unauthenticated access to customer info should redirect to login");
    }
}
