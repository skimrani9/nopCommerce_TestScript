package com.nopcommerce.automation.utils;

import com.nopcommerce.automation.config.ConfigReader;
import com.nopcommerce.automation.config.DriverManager;
import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.pages.admin.AdminDashboardPage;
import com.nopcommerce.automation.pages.admin.AdminVendorEditPage;
import com.nopcommerce.automation.pages.admin.AdminVendorListPage;
import com.nopcommerce.automation.pages.admin.AdminVendorSettingsPage;
import com.nopcommerce.automation.pages.customer.LoginPage;
import com.nopcommerce.automation.pages.customer.RegisterPage;
import com.nopcommerce.automation.pages.vendor.VendorApplyPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class VendorStorefrontTestDataHelper {

    public static final String E2E_VENDOR_SHOP_NAME = "E2E Vendor Shop";
    public static final String E2E_VENDOR_SHOP_EMAIL = "e2e.vendor.shop@example.com";
    public static final String E2E_VENDOR_SEO = "e2e-vendor-shop";
    public static final String E2E_VENDOR_DESCRIPTION = "Electronics reseller for E2E storefront tests";
    public static final String E2E_VENDOR_UPDATED_DESCRIPTION = "Updated E2E vendor storefront description";
    public static final String E2E_VENDOR_OWNER_EMAIL = "e2e.vendor.owner@example.com";
    public static final String E2E_VENDOR_OWNER_PASSWORD = "TestPass123!";
    public static final String E2E_VENDOR_APPLICANT_EMAIL = "e2e.vendor.applicant@example.com";
    public static final String E2E_VENDOR_APPLICANT_PASSWORD = "TestPass123!";
    public static final String E2E_VENDOR_PASSWORD = "TestPass123!";

    private static final Logger logger = LogManager.getLogger(VendorStorefrontTestDataHelper.class);

    private static volatile boolean activeVendorPrepared;

    private VendorStorefrontTestDataHelper() {
    }

    public static void ensureVendorStorefrontSettings() {
        synchronized (VendorStorefrontTestDataHelper.class) {
            loginAsAdmin();
            new AdminVendorSettingsPage().ensureStorefrontVendorSettingsEnabled();
            logoutFromStorefront();
            logger.info("Vendor storefront settings are ready");
        }
    }

    public static void ensureAppliedVendorApplicant() {
        synchronized (VendorStorefrontTestDataHelper.class) {
            ensureVendorStorefrontSettings();
            ensureRegisteredCustomer(E2E_VENDOR_APPLICANT_EMAIL, E2E_VENDOR_APPLICANT_PASSWORD);
            loginAsCustomer(E2E_VENDOR_APPLICANT_EMAIL, E2E_VENDOR_APPLICANT_PASSWORD);

            VendorApplyPage vendorApplyPage = new VendorApplyPage();
            vendorApplyPage.openApplyPage();
            if (vendorApplyPage.isApplyFormDisplayed()) {
                vendorApplyPage.submitApplication(
                        E2E_VENDOR_SHOP_NAME + " Applicant",
                        E2E_VENDOR_SHOP_EMAIL,
                        E2E_VENDOR_DESCRIPTION);
            }

            logoutFromStorefront();
            logger.info("Applied vendor applicant is ready");
        }
    }

    public static void ensureActiveVendorStorefront() {
        synchronized (VendorStorefrontTestDataHelper.class) {
            if (activeVendorPrepared && isActiveVendorStorefrontAccessible()) {
                logger.info("Active vendor storefront data already prepared");
                return;
            }
            ensureVendorStorefrontSettings();
            ensureRegisteredCustomer(E2E_VENDOR_OWNER_EMAIL, E2E_VENDOR_OWNER_PASSWORD);
            loginAsCustomer(E2E_VENDOR_OWNER_EMAIL, E2E_VENDOR_OWNER_PASSWORD);

            VendorApplyPage vendorApplyPage = new VendorApplyPage();
            vendorApplyPage.openApplyPage();
            if (vendorApplyPage.isApplyFormDisplayed()) {
                vendorApplyPage.submitApplication(
                        E2E_VENDOR_SHOP_NAME,
                        E2E_VENDOR_SHOP_EMAIL,
                        E2E_VENDOR_DESCRIPTION);
            }

            logoutFromStorefront();
            activateVendorInAdmin();
            activeVendorPrepared = true;
            logger.info("Active vendor storefront data is ready");
        }
    }

    private static boolean isActiveVendorStorefrontAccessible() {
        try {
            com.nopcommerce.automation.pages.storefront.StorefrontVendorPage vendorPage =
                    new com.nopcommerce.automation.pages.storefront.StorefrontVendorPage();
            vendorPage.openVendorBySeoName(E2E_VENDOR_SEO);
            return vendorPage.isVendorPageDisplayed()
                    && vendorPage.isVendorNameDisplayed(E2E_VENDOR_SHOP_NAME);
        } catch (Exception exception) {
            return false;
        }
    }

    public static String createFreshVendorApplicantCustomer() {
        String email = "e2e.vendor.apply." + System.currentTimeMillis() + "@example.com";
        ensureVendorStorefrontSettings();
        RegisterPage registerPage = new RegisterPage();
        registerPage.register(email, E2E_VENDOR_PASSWORD);
        logoutFromStorefront();
        return email;
    }

    private static void activateVendorInAdmin() {
        loginAsAdmin();
        AdminVendorListPage vendorListPage = new AdminVendorListPage();
        AdminVendorEditPage vendorEditPage = new AdminVendorEditPage();
        vendorListPage.openVendorEditFromList(E2E_VENDOR_SHOP_NAME);
        vendorEditPage.waitForEditPageLoad();
        vendorEditPage.activateVendor();
        vendorEditPage.setVendorSeoName(E2E_VENDOR_SEO);
        vendorEditPage.clickSave();
        logoutFromStorefront();
    }

    private static void ensureRegisteredCustomer(String email, String password) {
        LoginPage loginPage = new LoginPage();
        loginPage.openLoginPage();
        loginPage.login(email, password);
        if (!loginPage.isLoginErrorDisplayed()) {
            logoutFromStorefront();
            return;
        }

        RegisterPage registerPage = new RegisterPage();
        if (!registerPage.tryRegister(email, password, "E2E", "Vendor")) {
            loginPage.openLoginPage();
            loginPage.login(email, password);
            if (loginPage.isLoginErrorDisplayed()) {
                throw new IllegalStateException("Unable to create vendor test customer: " + email);
            }
        }
        logoutFromStorefront();
    }

    private static void loginAsCustomer(String email, String password) {
        LoginPage loginPage = new LoginPage();
        loginPage.openLoginPage();
        loginPage.login(email, password);
        if (loginPage.isLoginErrorDisplayed()) {
            throw new IllegalStateException("Vendor test customer login failed: " + email);
        }
    }

    private static void loginAsAdmin() {
        AdminEnvironmentHelper.ensureAdminLoginReady();
        AdminDashboardPage adminDashboardPage = new AdminDashboardPage();
        LoginPage loginPage = new LoginPage();
        adminDashboardPage.openDashboard();
        if (loginPage.isLoginPageDisplayed()) {
            loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());
        }
        adminDashboardPage.waitForDashboardLoad();
    }

    private static void logoutFromStorefront() {
        DriverManager.getDriver().get(ConfigReader.getBaseUrl() + Constants.STORE_LOGOUT_PATH);
        DriverManager.getDriver().manage().deleteAllCookies();
    }
}
