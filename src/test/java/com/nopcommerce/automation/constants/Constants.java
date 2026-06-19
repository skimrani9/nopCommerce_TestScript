package com.nopcommerce.automation.constants;

public final class Constants {

    private Constants() {
    }

    public static final String ADMIN_AREA_PATH = "/admin";
    public static final String LOGIN_PATH = "/login";
    public static final String REGISTER_PATH = "/register";
    public static final String MULTI_FACTOR_VERIFICATION_PATH = "/multi-factor-verification";
    public static final String CUSTOMER_MFA_PATH = "/customer/multifactorauthentication";
    public static final String ADMIN_GENERAL_SETTINGS_PATH = "/admin/setting/generalcommon";
    public static final String ADMIN_SECURITY_PERMISSIONS_PATH = "/admin/security/permissions";
    public static final String ADMIN_PLUGIN_LIST_PATH = "/admin/plugin/list";
    public static final String ADMIN_PRODUCT_ATTRIBUTE_LIST_PATH = "/admin/productattribute/list";
    public static final String ADMIN_PRODUCT_ATTRIBUTE_CREATE_PATH = "/admin/productattribute/create";
    public static final String CUSTOMER_MFA_PROVIDER_CONFIG_PATH = "/customer/providerconfig?providerSysName=MultiFactorAuth.GoogleAuthenticator";
    public static final String ADMIN_PRODUCT_LIST_PATH = "/admin/product/list";
    public static final String ADMIN_PRODUCT_CREATE_PATH = "/admin/product/create";
    public static final String ADMIN_CATEGORY_LIST_PATH = "/admin/category/list";
    public static final String ADMIN_CATEGORY_CREATE_PATH = "/admin/category/create";
    public static final String ADMIN_ORDER_LIST_PATH = "/admin/order/list";
    public static final String ADMIN_SHIPMENT_LIST_PATH = "/admin/order/shipmentlist";
    public static final String ADMIN_CUSTOMER_LIST_PATH = "/admin/customer/list";
    public static final String ADMIN_CUSTOMER_CREATE_PATH = "/admin/customer/create";
    public static final String ADMIN_DISCOUNT_LIST_PATH = "/admin/discount/list";
    public static final String ADMIN_DISCOUNT_CREATE_PATH = "/admin/discount/create";
    public static final String ADMIN_TAX_PROVIDERS_PATH = "/admin/tax/providers";
    public static final String ADMIN_TAX_CATEGORIES_PATH = "/admin/tax/categories";
    public static final String ADMIN_TAX_SETTINGS_PATH = "/admin/setting/tax";
    public static final String ADMIN_SHIPPING_METHODS_PATH = "/admin/shipping/methods";
    public static final String ADMIN_SHIPPING_PROVIDERS_PATH = "/admin/shipping/providers";
    public static final String ADMIN_SHIPPING_WAREHOUSES_PATH = "/admin/shipping/warehouses";
    public static final String ADMIN_SHIPPING_WAREHOUSE_CREATE_PATH = "/admin/shipping/createwarehouse";
    public static final String ADMIN_SHIPPING_RESTRICTIONS_PATH = "/admin/shipping/restrictions";
    public static final String ADMIN_PAYMENT_METHODS_PATH = "/admin/payment/methods";
    public static final String ADMIN_PAYMENT_RESTRICTIONS_PATH = "/admin/payment/methodrestrictions";
    public static final String ADMIN_SCHEDULE_TASK_LIST_PATH = "/admin/scheduletask/list";
    public static final String SCHEDULE_TASK_RUN_PATH = "/scheduletask/runtask";
    public static final String KEEP_ALIVE_TASK_TYPE = "Nop.Services.Common.KeepAliveTask, Nop.Services";
    public static final String SEND_EMAILS_TASK_TYPE = "Nop.Services.Messages.QueuedMessagesSendTask, Nop.Services";
    public static final String INVALID_SCHEDULE_TASK_TYPE = "Invalid.Task.Type, Nop.Services";
    public static final String STORE_HOME_PATH = "/";
    public static final String STORE_SEARCH_PATH = "/search";
    public static final String STORE_CART_PATH = "/cart";
    public static final String STORE_CHECKOUT_PATH = "/checkout";
    public static final String STORE_SITEMAP_PATH = "/sitemap";
    public static final String STORE_SITEMAP_XML_PATH = "/sitemap.xml";
    public static final String STORE_ROBOTS_TXT_PATH = "/robots.txt";
    public static final String STORE_LOGIN_PATH = "/login";
    public static final String STORE_REGISTER_PATH = "/register";
    public static final String STORE_LOGOUT_PATH = "/logout";
    public static final String STORE_CUSTOMER_INFO_PATH = "/customer/info";
    public static final String STORE_WISHLIST_PATH = "/wishlist";
    public static final String STORE_VENDOR_APPLY_PATH = "/vendor/apply";
    public static final String STORE_VENDOR_INFO_PATH = "/vendor/info";
    public static final String STORE_REGISTER_RESULT_PATH = "/registerresult";
    public static final String ADMIN_VENDOR_LIST_PATH = "/admin/vendor/list";
    public static final String ADMIN_VENDOR_SETTINGS_PATH = "/admin/setting/vendor";
    public static final String CHECK_MONEY_ORDER_SYSTEM_NAME = "Payments.CheckMoneyOrder";
    public static final String MANUAL_PAYMENT_SYSTEM_NAME = "Payments.Manual";
    public static final String DEFAULT_BROWSER = "chrome";
    public static final String GOOGLEBOT_USER_AGENT =
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    public static final int DEFAULT_EXPLICIT_WAIT_SECONDS = 15;
    public static final String SCREENSHOT_DIR = "target/screenshots";
    public static final String EXTENT_REPORT_PATH = "target/extent-report.html";
}
