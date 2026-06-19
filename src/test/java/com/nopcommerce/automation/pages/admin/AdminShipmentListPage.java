package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminShipmentListPage extends BasePage {

    private final By shipmentsGrid = By.id("shipments-grid");
    private final By searchShipmentsButton = By.id("search-shipments");

    public void openShipmentList() {
        navigateTo(Constants.ADMIN_SHIPMENT_LIST_PATH);
        WaitUtils.waitForElement(shipmentsGrid, WaitType.VISIBLE);
    }

    public boolean isShipmentGridDisplayed() {
        return isDisplayed(shipmentsGrid)
                && isDisplayed(By.cssSelector("#shipments-grid tbody tr"));
    }

    public void openFirstShipmentDetails() {
        openShipmentList();
        By detailsLink = By.xpath("//table[@id='shipments-grid']//a[contains(@href,'ShipmentDetails')]");
        click(detailsLink);
        WaitUtils.waitForUrlContains("/shipmentdetails");
    }

    public String getFirstShipmentIdFromGrid() {
        openShipmentList();
        By detailsLink = By.xpath("//table[@id='shipments-grid']//a[contains(@href,'ShipmentDetails')]");
        String href = WaitUtils.waitForElement(detailsLink, WaitType.VISIBLE).getAttribute("href");
        return href.substring(href.lastIndexOf('/') + 1);
    }
}
