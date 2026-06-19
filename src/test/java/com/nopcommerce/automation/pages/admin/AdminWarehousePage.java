package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminWarehousePage extends BasePage {

    private final By warehouseGrid = By.id("warehouse-grid");
    private final By addNewButton = By.cssSelector("a[href*='createwarehouse']");

    public void openWarehouses() {
        navigateTo(Constants.ADMIN_SHIPPING_WAREHOUSES_PATH);
        WaitUtils.waitForElement(warehouseGrid, WaitType.VISIBLE);
    }

    public void clickAddNewWarehouse() {
        navigateTo(Constants.ADMIN_SHIPPING_WAREHOUSE_CREATE_PATH);
        WaitUtils.waitForElement(By.id("Name"), WaitType.VISIBLE);
    }

    public void fillAndSaveWarehouse(String name, String city) {
        type(By.id("Name"), name);
        type(By.id("Address_City"), city);
        click(By.cssSelector("button[name='save']"));
        WaitUtils.waitForUrlContains(Constants.ADMIN_SHIPPING_WAREHOUSES_PATH);
    }

    public boolean isWarehouseListed(String name) {
        openWarehouses();
        By warehouseCell = By.xpath(
                "//table[@id='warehouse-grid']//td[contains(normalize-space(.), '" + name + "')]");
        return isDisplayed(warehouseCell);
    }
}
