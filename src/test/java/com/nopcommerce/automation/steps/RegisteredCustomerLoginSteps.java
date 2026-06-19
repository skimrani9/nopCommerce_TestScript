package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.pages.customer.LoginPage;
import com.nopcommerce.automation.pages.storefront.StorefrontHomePage;
import com.nopcommerce.automation.utils.AssertionUtils;
import com.nopcommerce.automation.utils.RegisteredCustomerTestDataHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RegisteredCustomerLoginSteps {

    private static final String CUSTOMER_EMAIL_KEY = "customerEmail";
    private static final String CUSTOMER_PASSWORD_KEY = "customerPassword";

    private final LoginPage loginPage = new LoginPage();
    private final StorefrontHomePage storefrontHomePage = new StorefrontHomePage();

    @Given("a registered E2E customer account exists")
    public void aRegisteredE2ECustomerAccountExists() {
        RegisteredCustomerTestDataHelper.ensureRegisteredCustomer();
        ScenarioContext.set(CUSTOMER_EMAIL_KEY, RegisteredCustomerTestDataHelper.E2E_REGISTERED_EMAIL);
        ScenarioContext.set(CUSTOMER_PASSWORD_KEY, RegisteredCustomerTestDataHelper.E2E_REGISTERED_PASSWORD);
    }

    @When("registered customer opens the login page")
    public void registeredCustomerOpensTheLoginPage() {
        loginPage.openLoginPage();
    }

    @And("registered customer submits valid credentials")
    public void registeredCustomerSubmitsValidCredentials() {
        loginPage.login(
                RegisteredCustomerTestDataHelper.E2E_REGISTERED_EMAIL,
                RegisteredCustomerTestDataHelper.E2E_REGISTERED_PASSWORD);
    }

    @And("registered customer submits correct email with wrong password {string}")
    public void registeredCustomerSubmitsCorrectEmailWithWrongPassword(String wrongPassword) {
        loginPage.login(RegisteredCustomerTestDataHelper.E2E_REGISTERED_EMAIL, wrongPassword);
    }

    @When("registered customer logs out from the storefront")
    public void registeredCustomerLogsOutFromTheStorefront() {
        storefrontHomePage.logout();
    }

    @Then("registered customer is authenticated on the storefront")
    public void registeredCustomerIsAuthenticatedOnTheStorefront() {
        AssertionUtils.assertFalse(
                loginPage.isLoginErrorDisplayed(),
                "Registered customer login should succeed without errors");
        AssertionUtils.assertTrue(
                storefrontHomePage.isCustomerLoggedIn(),
                "Authenticated customer should see account and logout links");
    }

    @Then("login error is displayed and customer remains unauthenticated")
    public void loginErrorIsDisplayedAndCustomerRemainsUnauthenticated() {
        AssertionUtils.assertTrue(
                loginPage.isLoginErrorDisplayed(),
                "Login error should be displayed for incorrect password");
        AssertionUtils.assertFalse(
                storefrontHomePage.isCustomerLoggedIn(),
                "Customer should remain unauthenticated after failed login");
    }

    @Then("registered customer session is cleared and guest links are visible")
    public void registeredCustomerSessionIsClearedAndGuestLinksAreVisible() {
        AssertionUtils.assertTrue(
                storefrontHomePage.isGuestSession(),
                "Logout should restore guest session with login link visible");
    }
}
