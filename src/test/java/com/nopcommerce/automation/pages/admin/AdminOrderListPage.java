package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AdminOrderListPage extends BasePage {

    private final By ordersGrid = By.id("orders-grid");
    private final By searchOrdersButton = By.id("search-orders");
    private final By orderStatusFilter = By.id("OrderStatusIds");
    private final By goToOrderInput = By.id("GoDirectlyToCustomOrderNumber");
    private final By goToOrderButton = By.id("go-to-order-by-number");

    public void openOrderList() {
        navigateTo(Constants.ADMIN_ORDER_LIST_PATH);
        WaitUtils.waitForElement(ordersGrid, WaitType.VISIBLE);
    }

    public void filterByOrderStatus(String statusLabel) {
        WebElement statusSelect = WaitUtils.waitForElement(orderStatusFilter, WaitType.VISIBLE);
        for (WebElement option : statusSelect.findElements(By.tagName("option"))) {
            if (option.getText().trim().equalsIgnoreCase(statusLabel)) {
                option.click();
                break;
            }
        }
        click(searchOrdersButton);
        WaitUtils.waitForElement(ordersGrid, WaitType.VISIBLE);
    }

    public boolean isOrderGridDisplayedWithResults() {
        return isDisplayed(ordersGrid)
                && isDisplayed(By.cssSelector("#orders-grid tbody tr"));
    }

    public void openOrderEditById(String orderId) {
        navigateTo(Constants.ADMIN_ORDER_LIST_PATH.replace("/list", "/edit/" + orderId));
        WaitUtils.waitForUrlContains("/order/edit");
    }

    public void openFirstOrderFromGrid() {
        openOrderList();
        By editLink = By.xpath("//table[@id='orders-grid']//a[contains(@href,'Order/Edit')]");
        click(editLink);
        WaitUtils.waitForUrlContains("/order/edit");
    }

    public String getFirstOrderIdFromGrid() {
        openOrderList();
        By editLink = By.xpath("//table[@id='orders-grid']//a[contains(@href,'Order/Edit')]");
        WebElement link = WaitUtils.waitForElement(editLink, WaitType.VISIBLE);
        String href = link.getAttribute("href");
        return href.substring(href.lastIndexOf('/') + 1);
    }
}
