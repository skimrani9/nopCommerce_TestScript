package com.nopcommerce.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features/vendor-admin",
        glue = "com.nopcommerce.automation",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/vendor-admin-cucumber.html",
                "json:target/cucumber-reports/vendor-admin-cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true
)
public class VendorAdminTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
