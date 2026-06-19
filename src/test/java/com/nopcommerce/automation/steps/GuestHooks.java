package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.utils.GuestTestDataHelper;
import io.cucumber.java.Before;

public class GuestHooks {

    @Before(order = 1, value = "@Guest and @requires-data")
    public void prepareGuestCatalogData() {
        GuestTestDataHelper.ensureGuestCatalogData();
    }
}
