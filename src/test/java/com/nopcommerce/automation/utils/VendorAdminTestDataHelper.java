package com.nopcommerce.automation.utils;

import com.nopcommerce.automation.config.ConfigReader;
import com.nopcommerce.automation.config.DriverManager;
import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.pages.admin.AdminCustomerEditPage;
import com.nopcommerce.automation.pages.admin.AdminCustomerListPage;
import com.nopcommerce.automation.pages.admin.AdminDashboardPage;
import com.nopcommerce.automation.pages.customer.LoginPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class VendorAdminTestDataHelper {

    private static final Logger logger = LogManager.getLogger(VendorAdminTestDataHelper.class);

    private static volatile boolean vendorAdminPrepared;

    private VendorAdminTestDataHelper() {
    }

    public static void ensureVendorAdminAccount() {
        synchronized (VendorAdminTestDataHelper.class) {
            if (vendorAdminPrepared && canVendorAdminLoginToAdminPanel()) {
                logger.info("Vendor admin account already prepared");
                return;
            }
            VendorStorefrontTestDataHelper.ensureActiveVendorStorefront();
            assignVendorsRoleToOwner();
            vendorAdminPrepared = true;
            logger.info("Vendor admin account is ready");
        }
    }

    private static void assignVendorsRoleToOwner() {
        AdminEnvironmentHelper.ensureAdminLoginReady();
        AdminDashboardPage adminDashboardPage = new AdminDashboardPage();
        LoginPage loginPage = new LoginPage();
        adminDashboardPage.openDashboard();
        if (loginPage.isLoginPageDisplayed()) {
            loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());
        }
        adminDashboardPage.waitForDashboardLoad();

        AdminCustomerListPage customerListPage = new AdminCustomerListPage();
        AdminCustomerEditPage customerEditPage = new AdminCustomerEditPage();
        customerListPage.openCustomerEditByEmail(VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_EMAIL);
        customerEditPage.waitForEditPageLoad();
        if (!customerEditPage.isCustomerRoleSelected("Vendors")) {
            customerEditPage.assignCustomerRole("Vendors");
            customerEditPage.clickSave();
            customerEditPage.waitForCustomerListRedirect();
        }
        logoutFromStorefront();
    }

    private static boolean canVendorAdminLoginToAdminPanel() {
        LoginPage loginPage = new LoginPage();
        AdminDashboardPage adminDashboardPage = new AdminDashboardPage();
        loginPage.openLoginPageWithAdminReturnUrl();
        loginPage.login(
                VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_EMAIL,
                VendorStorefrontTestDataHelper.E2E_VENDOR_OWNER_PASSWORD);
        if (loginPage.isLoginErrorDisplayed()) {
            return false;
        }
        boolean loggedIn = adminDashboardPage.isDashboardDisplayed();
        logoutFromStorefront();
        return loggedIn;
    }

    private static void logoutFromStorefront() {
        DriverManager.getDriver().get(ConfigReader.getBaseUrl() + Constants.STORE_LOGOUT_PATH);
        DriverManager.getDriver().manage().deleteAllCookies();
    }
}
