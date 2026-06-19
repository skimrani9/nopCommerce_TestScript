package com.nopcommerce.automation.utils;

import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.pages.admin.AdminOrderEditPage;
import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.pages.admin.AdminPaymentMethodsPage;
import com.nopcommerce.automation.pages.admin.AdminProductEditPage;
import com.nopcommerce.automation.pages.admin.AdminProductListPage;
import com.nopcommerce.automation.pages.storefront.StorefrontCartPage;
import com.nopcommerce.automation.pages.storefront.StorefrontCheckoutPage;
import com.nopcommerce.automation.pages.storefront.StorefrontProductPage;

public final class CheckoutOrderHelper {

    private static final String ORDER_ID_KEY = "orderId";
    private static final String ORDER_PRODUCT_NAME_KEY = "orderProductName";
    private static final String ORDER_PRODUCT_SKU_KEY = "orderProductSku";

    private CheckoutOrderHelper() {
    }

    public static String createPendingOrderViaCheckout() {
        new AdminPaymentMethodsPage().setPaymentMethodActive(Constants.CHECK_MONEY_ORDER_SYSTEM_NAME, true);

        String sku = "E2E-ORD-" + (System.currentTimeMillis() % 100000);
        String productName = "E2E Order Product " + sku;

        AdminProductListPage productListPage = new AdminProductListPage();
        AdminProductEditPage productEditPage = new AdminProductEditPage();
        productListPage.openProductList();
        productListPage.clickAddNewProduct();
        productEditPage.fillProductDetails(productName, sku, "50.00", true);
        productEditPage.clickSave();
        productEditPage.waitForProductListRedirect();

        com.nopcommerce.automation.config.DriverManager.getDriver()
                .get(com.nopcommerce.automation.config.ConfigReader.getBaseUrl() + "/logout");
        StorefrontProductPage storefrontProductPage = new StorefrontProductPage();
        storefrontProductPage.openSearchResults(productName);
        storefrontProductPage.openProductDetailsFromSearch(productName);
        storefrontProductPage.addCurrentProductToCart();

        StorefrontCartPage cartPage = new StorefrontCartPage();
        cartPage.openCart();
        cartPage.proceedToCheckout();

        StorefrontCheckoutPage checkoutPage = new StorefrontCheckoutPage();
        String orderId = checkoutPage.placeGuestOrderWithCheckMoneyOrder();

        ScenarioContext.set(ORDER_ID_KEY, orderId);
        ScenarioContext.set(ORDER_PRODUCT_NAME_KEY, productName);
        ScenarioContext.set(ORDER_PRODUCT_SKU_KEY, sku);
        return orderId;
    }

    public static String createProcessingPaidOrderViaCheckout() {
        String orderId = createPendingOrderViaCheckout();
        AdminOrderEditPage orderEditPage = new AdminOrderEditPage();
        orderEditPage.openOrderEdit(orderId);
        orderEditPage.changeOrderStatus("Processing");
        orderEditPage.markOrderAsPaid();
        return orderId;
    }

    public static String getStoredOrderId() {
        return ScenarioContext.get(ORDER_ID_KEY);
    }

}
