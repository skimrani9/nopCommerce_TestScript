package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AdminShipmentAddPage extends BasePage {

    private final By trackingNumberInput = By.id("TrackingNumber");
    private final By saveButton = By.id("shipment-save");

    public void waitForAddShipmentPage() {
        WaitUtils.waitForUrlContains("/addshipment");
    }

    public void createShipmentForAllItems() {
        waitForAddShipmentPage();
        By qtyInputs = By.cssSelector("input[id^='qtyToAdd']");
        for (WebElement qtyInput : driver.findElements(qtyInputs)) {
            String maxQty = qtyInput.getAttribute("max");
            if (maxQty != null && !maxQty.isBlank()) {
                type(By.id(qtyInput.getAttribute("id")), maxQty);
            } else {
                type(By.id(qtyInput.getAttribute("id")), "1");
            }
        }
        click(saveButton);
        WaitUtils.waitForUrlContains("/shipmentdetails");
    }
}
