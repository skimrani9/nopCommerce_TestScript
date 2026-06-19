package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.utils.VendorStorefrontTestDataHelper;
import io.cucumber.java.Before;

public class VendorStorefrontHooks {

    @Before(order = 1, value = "@VendorStorefront and @requires-data")
    public void prepareVendorStorefrontData() {
        VendorStorefrontTestDataHelper.ensureVendorStorefrontSettings();
    }
}
