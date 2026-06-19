package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminOrderEditPage extends BasePage {

    private final By orderStatusDropdown = By.id("OrderStatusId");
    private final By saveOrderStatusButton = By.id("btnSaveOrderStatus");
    private final By markOrderAsPaidButton = By.id("markorderaspaid");
    private final By refundOrderOfflineButton = By.id("refundorderoffline");
    private final By cancelOrderButton = By.id("cancelorder");
    private final By orderInfoCard = By.cssSelector("[data-card-name='order-info']");
    private final By productsCard = By.cssSelector("[data-card-name='order-products']");
    private final By addShipmentButton = By.id("btnAddNewShipment");

    public void waitForEditPageLoad() {
        WaitUtils.waitForUrlContains("/order/edit");
        WaitUtils.waitForElement(orderInfoCard, WaitType.VISIBLE);
    }

    public void openOrderEdit(String orderId) {
        navigateTo("/admin/order/edit/" + orderId);
        waitForEditPageLoad();
    }

    public boolean isOrderDetailDisplayed() {
        waitForEditPageLoad();
        expandAdminCardByName("order-info");
        expandAdminCardByName("order-products");
        return isDisplayed(orderInfoCard) && isDisplayed(productsCard);
    }

    public void changeOrderStatus(String statusLabel) {
        waitForEditPageLoad();
        expandAdminCardByName("order-info");
        selectDropdownOptionByText(orderStatusDropdown, statusLabel);
        clickActionConfirmation("btnSaveOrderStatus");
        waitForEditPageLoad();
    }

    public void markOrderAsPaid() {
        waitForEditPageLoad();
        expandAdminCardByName("order-info");
        clickActionConfirmation("markorderaspaid");
        waitForEditPageLoad();
    }

    public void refundOrderOffline() {
        waitForEditPageLoad();
        expandAdminCardByName("order-info");
        clickActionConfirmation("refundorderoffline");
        waitForEditPageLoad();
    }

    public void cancelOrder() {
        waitForEditPageLoad();
        expandAdminCardByName("order-info");
        clickActionConfirmation("cancelorder");
        waitForEditPageLoad();
    }

    public String getOrderStatusText() {
        expandAdminCardByName("order-info");
        return getText(By.xpath(
                "//div[@data-card-name='order-info']//label[contains(@for,'OrderStatus') or contains(normalize-space(.),'Order status')]"
                        + "/following::div[contains(@class,'font-weight-bold')][1]"));
    }

    public String getPaymentStatusText() {
        expandAdminCardByName("order-info");
        return getText(By.xpath(
                "//div[@data-card-name='order-info']//label[contains(@for,'PaymentStatus') or contains(normalize-space(.),'Payment status')]"
                        + "/following::div[contains(@class,'font-weight-bold')][1]"));
    }

    public void clickAddNewShipment() {
        expandAdminCardByName("order-billing-shipping");
        click(addShipmentButton);
        WaitUtils.waitForUrlContains("/addshipment");
    }
}
