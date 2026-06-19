package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.utils.VendorAdminTestDataHelper;
import io.cucumber.java.Before;

public class VendorAdminHooks {

    @Before(order = 1, value = "@VendorAdmin and @requires-data")
    public void prepareVendorAdminData() {
        VendorAdminTestDataHelper.ensureVendorAdminAccount();
    }
}
