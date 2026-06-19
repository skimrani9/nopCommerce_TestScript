package com.nopcommerce.automation.steps;



import com.nopcommerce.automation.config.ConfigReader;

import com.nopcommerce.automation.config.ScenarioContext;

import com.nopcommerce.automation.constants.Constants;

import com.nopcommerce.automation.pages.admin.AdminDashboardPage;

import com.nopcommerce.automation.pages.admin.AdminGeneralSettingsPage;

import com.nopcommerce.automation.pages.admin.AdminLayoutPage;

import com.nopcommerce.automation.pages.admin.AdminPluginListPage;

import com.nopcommerce.automation.pages.admin.AdminSecurityPermissionsPage;

import com.nopcommerce.automation.pages.customer.CustomerMfaSetupPage;

import com.nopcommerce.automation.pages.customer.LoginPage;

import com.nopcommerce.automation.pages.customer.MultiFactorVerificationPage;

import com.nopcommerce.automation.pages.customer.RegisterPage;

import com.nopcommerce.automation.utils.AssertionUtils;

import com.nopcommerce.automation.utils.AdminEnvironmentHelper;
import com.nopcommerce.automation.utils.TotpUtils;

import io.cucumber.java.en.Given;

import io.cucumber.java.en.Then;

import io.cucumber.java.en.When;



public class AdminLoginSteps {



    private static final String CUSTOMER_EMAIL_KEY = "customerEmail";

    private static final String CUSTOMER_PASSWORD_KEY = "customerPassword";

    private static final String MFA_SECRET_KEY = "mfaSecretKey";



    private final LoginPage loginPage = new LoginPage();

    private final RegisterPage registerPage = new RegisterPage();

    private final AdminDashboardPage adminDashboardPage = new AdminDashboardPage();

    private final AdminLayoutPage adminLayoutPage = new AdminLayoutPage();

    private final AdminGeneralSettingsPage adminGeneralSettingsPage = new AdminGeneralSettingsPage();

    private final CustomerMfaSetupPage customerMfaSetupPage = new CustomerMfaSetupPage();

    private final MultiFactorVerificationPage multiFactorVerificationPage = new MultiFactorVerificationPage();



    @Given("an admin user exists with valid credentials")

    public void anAdminUserExistsWithValidCredentials() {

        AssertionUtils.assertTrue(

                ConfigReader.getAdminEmail() != null && !ConfigReader.getAdminEmail().isBlank(),

                "Admin email must be configured via admin.email or ADMIN_EMAIL environment variable");

        AssertionUtils.assertTrue(

                ConfigReader.getAdminPassword() != null && !ConfigReader.getAdminPassword().isBlank(),

                "Admin password must be configured via admin.password or ADMIN_PASSWORD environment variable");

    }



    @Given("admin is logged into the admin panel")

    public void adminIsLoggedIntoTheAdminPanel() {

        AdminEnvironmentHelper.ensureAdminLoginReady();
        AdminEnvironmentHelper.ensureAnonymousStorefrontSession();

        adminDashboardPage.openDashboard();

        if (!adminDashboardPage.isDashboardDisplayed()) {
            loginPage.openLoginPageWithAdminReturnUrl();
            loginToAdminPanel();
        }

        adminDashboardPage.waitForDashboardLoad();

    }



    @Given("a registered customer without admin permissions exists")

    public void aRegisteredCustomerWithoutAdminPermissionsExists() {

        String customerEmail = ConfigReader.getCustomerEmail();

        String customerPassword = ConfigReader.getCustomerPassword();



        if (customerEmail == null || customerEmail.isBlank()

                || customerPassword == null || customerPassword.isBlank()) {

            customerEmail = "e2e.customer." + System.currentTimeMillis() + "@example.com";

            customerPassword = "TestPass123!";

            registerPage.register(customerEmail, customerPassword);

        }



        ScenarioContext.set(CUSTOMER_EMAIL_KEY, customerEmail);

        ScenarioContext.set(CUSTOMER_PASSWORD_KEY, customerPassword);

    }



    @Given("customer is logged into the storefront")

    public void customerIsLoggedIntoTheStorefront() {

        loginPage.openLoginPage();

        loginPage.login(getCustomerEmail(), getCustomerPassword());

        AssertionUtils.assertFalse(

                loginPage.isLoginErrorDisplayed(),

                "Customer login failed. Verify customer credentials and account state.");

    }



    @Given("admin has Google Authenticator MFA enabled")

    public void adminHasGoogleAuthenticatorMfaEnabled() {

        new AdminPluginListPage().ensureGoogleAuthenticatorPluginInstalled();

        new AdminSecurityPermissionsPage().enableMultiFactorAuthenticationForAdministrators();

        customerMfaSetupPage.enableGoogleAuthenticatorForCurrentCustomer();

        String secretKey = customerMfaSetupPage.getSecretKey();

        String verificationCode = TotpUtils.generateCode(secretKey);

        customerMfaSetupPage.completeGoogleAuthenticatorRegistration(verificationCode);

        ScenarioContext.set(MFA_SECRET_KEY, secretKey);

    }



    @Given("CAPTCHA is enabled on the login page")

    public void captchaIsEnabledOnTheLoginPage() {

        adminGeneralSettingsPage.enableCaptchaOnLoginPage();

    }



    @When("user navigates to the admin area")

    public void userNavigatesToTheAdminArea() {

        adminDashboardPage.openDashboard();

    }



    @When("user opens storefront login page with admin return url")

    public void userOpensStorefrontLoginPageWithAdminReturnUrl() {

        loginPage.openLoginPageWithAdminReturnUrl();

    }



    @When("user submits valid admin email and password on login page without completing CAPTCHA")

    public void userSubmitsValidAdminEmailAndPasswordOnLoginPageWithoutCompletingCaptcha() {

        if (!loginPage.isLoginPageDisplayed()) {

            loginPage.openLoginPageWithAdminReturnUrl();

        }

        loginPage.loginWithoutCompletingCaptcha(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

    }



    @When("user submits valid admin email and password on login page")

    public void userSubmitsValidAdminEmailAndPasswordOnLoginPage() {

        if (!loginPage.isLoginPageDisplayed()) {

            loginPage.openLoginPageWithAdminReturnUrl();

        }

        loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

    }



    @When("user submits correct admin email with wrong password {string}")

    public void userSubmitsCorrectAdminEmailWithWrongPassword(String wrongPassword) {

        if (!loginPage.isLoginPageDisplayed()) {

            loginPage.openLoginPageWithAdminReturnUrl();

        }

        loginPage.login(ConfigReader.getAdminEmail(), wrongPassword);

    }



    @When("user submits invalid MFA verification code {string}")

    public void userSubmitsInvalidMfaVerificationCode(String invalidCode) {

        multiFactorVerificationPage.submitVerificationCode(invalidCode);

    }



    @When("user submits valid MFA verification code")

    public void userSubmitsValidMfaVerificationCode() {

        String secretKey = ScenarioContext.get(MFA_SECRET_KEY);

        AssertionUtils.assertTrue(

                secretKey != null && !secretKey.isBlank(),

                "MFA secret key must be stored in scenario context before submitting a valid code");

        multiFactorVerificationPage.submitVerificationCode(TotpUtils.generateCode(secretKey));

    }



    @When("admin logs out from the admin area")

    public void adminLogsOutFromTheAdminArea() {

        adminLayoutPage.clickLogout();

    }



    @Then("user is redirected to the admin dashboard")

    public void userIsRedirectedToTheAdminDashboard() {

        adminDashboardPage.waitForDashboardLoad();

        AssertionUtils.assertTrue(

                adminDashboardPage.currentUrl().contains(Constants.ADMIN_AREA_PATH),

                "Expected admin dashboard URL");

        AssertionUtils.assertTrue(

                adminDashboardPage.isDashboardDisplayed(),

                "Expected admin dashboard heading to be visible");

    }



    @Then("user is redirected to multi-factor verification page")

    public void userIsRedirectedToMultiFactorVerificationPage() {

        AssertionUtils.assertTrue(

                multiFactorVerificationPage.isMultiFactorVerificationPageDisplayed(),

                "Expected multi-factor verification page after login");

    }



    @Then("login error is displayed and admin area remains inaccessible")

    public void loginErrorIsDisplayedAndAdminAreaRemainsInaccessible() {

        boolean loginBlocked = loginPage.isLoginErrorDisplayed() || loginPage.isLoginPageDisplayed();

        AssertionUtils.assertTrue(

                loginBlocked,

                "Expected login error message or login page after failed login");

        adminDashboardPage.openDashboard();

        AssertionUtils.assertTrue(

                loginPage.isLoginPageDisplayed() || !adminDashboardPage.isDashboardDisplayed(),

                "Admin area should remain inaccessible after failed login");

    }



    @Then("MFA verification error is displayed")

    public void mfaVerificationErrorIsDisplayed() {

        AssertionUtils.assertTrue(

                multiFactorVerificationPage.isVerificationErrorDisplayed(),

                "Expected MFA verification to fail for invalid code");

    }



    @Then("CAPTCHA is displayed on login page")

    public void captchaIsDisplayedOnLoginPage() {

        AssertionUtils.assertTrue(

                loginPage.isCaptchaDisplayed(),

                "Expected CAPTCHA widget on login page when CAPTCHA is enabled");

    }



    @Then("customer is denied access to the admin area")

    public void customerIsDeniedAccessToTheAdminArea() {

        boolean denied = loginPage.isLoginPageDisplayed() || !adminDashboardPage.isDashboardDisplayed();

        AssertionUtils.assertTrue(

                denied,

                "Non-admin customer should not access admin dashboard");

        AssertionUtils.assertFalse(

                adminDashboardPage.currentUrl().contains(Constants.ADMIN_AREA_PATH + "/Home")

                        && adminDashboardPage.isDashboardDisplayed(),

                "Admin dashboard must not be accessible for non-admin customer");

    }



    @Then("admin session ends and admin area requires re-authentication")

    public void adminSessionEndsAndAdminAreaRequiresReAuthentication() {

        adminDashboardPage.openDashboard();

        AssertionUtils.assertTrue(

                loginPage.isLoginPageDisplayed(),

                "Admin area should require login after logout");

    }



    private void completeMfaWithStoredOrConfiguredSecret() {

        String secretKey = ScenarioContext.get(MFA_SECRET_KEY);

        if (secretKey != null && !secretKey.isBlank()) {

            multiFactorVerificationPage.submitVerificationCode(TotpUtils.generateCode(secretKey));

        }

    }

    private void loginToAdminPanel() {

        loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

        if (multiFactorVerificationPage.isMultiFactorVerificationPageDisplayed()) {

            completeMfaWithStoredOrConfiguredSecret();

        }

    }



    private String getCustomerEmail() {

        String contextEmail = ScenarioContext.get(CUSTOMER_EMAIL_KEY);

        if (contextEmail != null && !contextEmail.isBlank()) {

            return contextEmail;

        }

        return ConfigReader.getCustomerEmail();

    }



    private String getCustomerPassword() {

        String contextPassword = ScenarioContext.get(CUSTOMER_PASSWORD_KEY);

        if (contextPassword != null && !contextPassword.isBlank()) {

            return contextPassword;

        }

        return ConfigReader.getCustomerPassword();

    }

}

