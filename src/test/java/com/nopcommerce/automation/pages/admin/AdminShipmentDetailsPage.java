package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminShipmentDetailsPage extends BasePage {

    private final By trackingNumberInput = By.id("TrackingNumber");
    private final By setTrackingNumberButton = By.cssSelector("button[name='settrackingnumber']");
    private final By setAsShippedButton = By.id("setasshipped");
    private final By setAsDeliveredButton = By.id("setasdelivered");
    private final By pdfPackagingSlipLink = By.xpath("//a[contains(@href,'PdfPackagingSlip')]");

    public void openShipmentDetails(String shipmentId) {
        navigateTo("/admin/order/shipmentdetails/" + shipmentId);
        WaitUtils.waitForUrlContains("/shipmentdetails");
    }

    public void setTrackingNumber(String trackingNumber) {
        type(trackingNumberInput, trackingNumber);
        click(setTrackingNumberButton);
        waitForAjaxComplete();
    }

    public String getTrackingNumber() {
        return getInputValue(trackingNumberInput);
    }

    public void markAsShipped() {
        clickActionConfirmation("setasshipped");
        waitForAjaxComplete();
    }

    public void markAsDelivered() {
        clickActionConfirmation("setasdelivered");
        waitForAjaxComplete();
    }

    public boolean isPdfPackagingSlipAvailable() {
        return isDisplayed(pdfPackagingSlipLink);
    }

    public String getShipmentStatusText() {
        return driver.getPageSource();
    }
}
