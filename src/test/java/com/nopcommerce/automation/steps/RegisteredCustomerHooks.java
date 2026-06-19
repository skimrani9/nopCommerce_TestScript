package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.utils.RegisteredCustomerTestDataHelper;
import io.cucumber.java.Before;

public class RegisteredCustomerHooks {

    @Before(order = 1, value = "@Registered and @requires-data")
    public void prepareRegisteredCustomerData() {
        RegisteredCustomerTestDataHelper.ensureRegisteredCustomer();
    }
}
