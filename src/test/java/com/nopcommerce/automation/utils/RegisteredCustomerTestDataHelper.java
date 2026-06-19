package com.nopcommerce.automation.utils;

import com.nopcommerce.automation.config.ConfigReader;
import com.nopcommerce.automation.config.DriverManager;
import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.pages.customer.LoginPage;
import com.nopcommerce.automation.pages.customer.RegisterPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class RegisteredCustomerTestDataHelper {

    public static final String E2E_REGISTERED_EMAIL = "e2e.registered.customer@example.com";
    public static final String E2E_REGISTERED_PASSWORD = "TestPass123!";
    public static final String E2E_REGISTERED_FIRST_NAME = "E2E";
    public static final String E2E_REGISTERED_LAST_NAME = "Registered";

    private static final Logger logger = LogManager.getLogger(RegisteredCustomerTestDataHelper.class);

    private RegisteredCustomerTestDataHelper() {
    }

    public static void ensureRegisteredCustomer() {
        synchronized (RegisteredCustomerTestDataHelper.class) {
            GuestTestDataHelper.ensureGuestCatalogData();
            if (canLoginAsRegisteredCustomer()) {
                logoutFromStorefront();
                logger.info("Registered customer account already exists");
                return;
            }

            RegisterPage registerPage = new RegisterPage();
            boolean registered = registerPage.tryRegister(
                    E2E_REGISTERED_EMAIL,
                    E2E_REGISTERED_PASSWORD,
                    E2E_REGISTERED_FIRST_NAME,
                    E2E_REGISTERED_LAST_NAME);

            if (registered || canLoginAsRegisteredCustomer()) {
                logoutFromStorefront();
                logger.info("Registered customer test account is ready");
                return;
            }

            throw new IllegalStateException(
                    "Unable to create or authenticate registered customer: " + E2E_REGISTERED_EMAIL);
        }
    }

    public static void ensureProductInWishlist() {
        LoginPage loginPage = new LoginPage();
        loginPage.openLoginPage();
        loginPage.login(E2E_REGISTERED_EMAIL, E2E_REGISTERED_PASSWORD);
        if (loginPage.isLoginErrorDisplayed()) {
            throw new IllegalStateException("Registered customer login failed while preparing wishlist data");
        }

        com.nopcommerce.automation.pages.storefront.StorefrontProductPage productPage =
                new com.nopcommerce.automation.pages.storefront.StorefrontProductPage();
        productPage.openProductBySeoName(GuestTestDataHelper.GUEST_PRODUCT_SEO);
        productPage.addCurrentProductToWishlist();

        com.nopcommerce.automation.pages.storefront.StorefrontWishlistPage wishlistPage =
                new com.nopcommerce.automation.pages.storefront.StorefrontWishlistPage();
        wishlistPage.openWishlist();
        if (!wishlistPage.isProductInWishlist(GuestTestDataHelper.GUEST_PRODUCT_NAME)) {
            throw new IllegalStateException("Failed to seed wishlist with product: "
                    + GuestTestDataHelper.GUEST_PRODUCT_NAME);
        }

        logoutFromStorefront();
    }

    private static boolean canLoginAsRegisteredCustomer() {
        LoginPage loginPage = new LoginPage();
        loginPage.openLoginPage();
        loginPage.login(E2E_REGISTERED_EMAIL, E2E_REGISTERED_PASSWORD);
        return !loginPage.isLoginErrorDisplayed();
    }

    private static void logoutFromStorefront() {
        DriverManager.getDriver().get(ConfigReader.getBaseUrl() + Constants.STORE_LOGOUT_PATH);
        DriverManager.getDriver().manage().deleteAllCookies();
    }
}
