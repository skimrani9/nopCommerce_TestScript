package com.nopcommerce.automation.utils;

import com.nopcommerce.automation.config.ConfigReader;
import com.nopcommerce.automation.config.DriverManager;
import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.pages.admin.AdminCategoryEditPage;
import com.nopcommerce.automation.pages.admin.AdminCategoryListPage;
import com.nopcommerce.automation.pages.admin.AdminDashboardPage;
import com.nopcommerce.automation.pages.admin.AdminProductEditPage;
import com.nopcommerce.automation.pages.admin.AdminProductListPage;
import com.nopcommerce.automation.pages.customer.LoginPage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class GuestTestDataHelper {

    public static final String GUEST_PRODUCT_NAME = "E2E Guest Product";
    public static final String GUEST_CATEGORY_NAME = "E2E Guest Catalog";
    public static final String GUEST_CATEGORY_SEO = "e2e-guest-catalog";
    public static final String GUEST_PRODUCT_SEO = "e2e-guest-product";
    public static final String GUEST_PRODUCT_SKU = "E2E-GUEST-PRODUCT";
    public static final String GUEST_SEARCH_TERM = "E2E Guest";

    private static final Logger logger = LogManager.getLogger(GuestTestDataHelper.class);

    private GuestTestDataHelper() {
    }

    public static void ensureGuestCatalogData() {
        synchronized (GuestTestDataHelper.class) {
            AdminEnvironmentHelper.ensureAdminLoginReady();
            loginAsAdmin();
            ensureCategoryExists();
            ensureProductExists();
            mapProductToCategory();
            logoutFromAdmin();
            logger.info("Guest catalog test data is ready");
        }
    }

    private static void loginAsAdmin() {
        AdminDashboardPage adminDashboardPage = new AdminDashboardPage();
        LoginPage loginPage = new LoginPage();
        adminDashboardPage.openDashboard();
        if (loginPage.isLoginPageDisplayed()) {
            loginPage.login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());
        }
        adminDashboardPage.waitForDashboardLoad();
    }

    private static void ensureCategoryExists() {
        AdminCategoryListPage categoryListPage = new AdminCategoryListPage();
        AdminCategoryEditPage categoryEditPage = new AdminCategoryEditPage();
        if (categoryListPage.isCategoryListed(GUEST_CATEGORY_NAME)) {
            return;
        }
        categoryListPage.clickAddNewCategory();
        categoryEditPage.fillCategoryDetails(GUEST_CATEGORY_NAME, true);
        categoryEditPage.clickSave();
        waitForCategorySave();
    }

    private static void waitForCategorySave() {
        try {
            WaitUtils.waitForUrlContains(Constants.ADMIN_CATEGORY_LIST_PATH);
        } catch (Exception exception) {
            WaitUtils.waitForUrlContains("/admin/category/edit");
        }
    }

    private static void ensureProductExists() {
        AdminProductListPage productListPage = new AdminProductListPage();
        AdminProductEditPage productEditPage = new AdminProductEditPage();
        productListPage.openProductList();
        productListPage.searchBySkuPrefix(GUEST_PRODUCT_SKU);
        if (productListPage.isProductListed(GUEST_PRODUCT_NAME)) {
            productListPage.openProductEditBySku(GUEST_PRODUCT_SKU);
            productEditPage.setProductSeoName(GUEST_PRODUCT_SEO);
            productEditPage.configureSimpleProductInventory(100);
            productEditPage.clickSave();
            waitForProductSave();
            return;
        }
        productEditPage.openCreateProductPage();
        productEditPage.fillProductDetails(GUEST_PRODUCT_NAME, GUEST_PRODUCT_SKU, "49.99", true);
        productEditPage.clickSave();
        waitForProductSave();
        productListPage.openProductEditBySku(GUEST_PRODUCT_SKU);
        productEditPage.setProductSeoName(GUEST_PRODUCT_SEO);
        productEditPage.configureSimpleProductInventory(100);
        productEditPage.clickSave();
        waitForProductSave();
    }

    private static void waitForProductSave() {
        try {
            WaitUtils.waitForUrlContains(Constants.ADMIN_PRODUCT_LIST_PATH);
        } catch (Exception exception) {
            WaitUtils.waitForUrlContains("/admin/product/edit");
        }
    }

    private static void mapProductToCategory() {
        AdminCategoryListPage categoryListPage = new AdminCategoryListPage();
        AdminCategoryEditPage categoryEditPage = new AdminCategoryEditPage();
        categoryListPage.openCategoryEditFromList(GUEST_CATEGORY_NAME);
        if (categoryEditPage.isProductMappedToCategory(GUEST_PRODUCT_NAME)) {
            return;
        }
        categoryEditPage.addProductToCategory(GUEST_PRODUCT_NAME);
        categoryEditPage.clickSave();
        categoryEditPage.waitForSaveComplete();
    }

    private static void logoutFromAdmin() {
        DriverManager.getDriver().get(ConfigReader.getBaseUrl() + Constants.STORE_LOGOUT_PATH);
        DriverManager.getDriver().manage().deleteAllCookies();
    }
}
