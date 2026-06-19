package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.pages.admin.AdminOrderEditPage;
import com.nopcommerce.automation.pages.admin.AdminShipmentAddPage;
import com.nopcommerce.automation.pages.admin.AdminShipmentDetailsPage;
import com.nopcommerce.automation.pages.admin.AdminShipmentListPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import com.nopcommerce.automation.utils.CheckoutOrderHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminShipmentSteps {

    private static final String ORDER_ID_KEY = "orderId";
    private static final String SHIPMENT_ID_KEY = "shipmentId";

    private final AdminShipmentListPage shipmentListPage = new AdminShipmentListPage();
    private final AdminShipmentAddPage shipmentAddPage = new AdminShipmentAddPage();
    private final AdminShipmentDetailsPage shipmentDetailsPage = new AdminShipmentDetailsPage();
    private final AdminOrderEditPage orderEditPage = new AdminOrderEditPage();

    @Given("at least one shipment exists in the system")
    public void atLeastOneShipmentExists() {
        ensureShipmentExists();
    }

    @Given("a processing paid order exists for automation")
    public void aProcessingPaidOrderExistsForAutomation() {
        String orderId = CheckoutOrderHelper.createProcessingPaidOrderViaCheckout();
        ScenarioContext.set(ORDER_ID_KEY, orderId);
    }

    @Given("a shipment exists for automation")
    public void aShipmentExistsForAutomation() {
        ensureShipmentExists();
    }

    @Given("a shipped shipment exists for automation")
    public void aShippedShipmentExistsForAutomation() {
        ensureShipmentExists();
        shipmentDetailsPage.openShipmentDetails(getShipmentId());
        if (!shipmentDetailsPage.getShipmentStatusText().toLowerCase().contains("shipped")) {
            shipmentDetailsPage.markAsShipped();
        }
    }

    @When("admin navigates to the shipment list")
    public void adminNavigatesToShipmentList() {
        shipmentListPage.openShipmentList();
    }

    @When("admin creates a shipment for the order")
    public void adminCreatesShipmentForOrder() {
        orderEditPage.openOrderEdit(getOrderId());
        orderEditPage.clickAddNewShipment();
        shipmentAddPage.createShipmentForAllItems();
        ScenarioContext.set(SHIPMENT_ID_KEY, extractShipmentIdFromUrl());
    }

    @When("admin sets shipment tracking number to {string}")
    public void adminSetsShipmentTrackingNumber(String trackingNumber) {
        shipmentDetailsPage.openShipmentDetails(getShipmentId());
        shipmentDetailsPage.setTrackingNumber(trackingNumber);
    }

    @When("admin marks the shipment as shipped")
    public void adminMarksShipmentAsShipped() {
        shipmentDetailsPage.openShipmentDetails(getShipmentId());
        shipmentDetailsPage.markAsShipped();
    }

    @When("admin marks the shipment as delivered")
    public void adminMarksShipmentAsDelivered() {
        shipmentDetailsPage.openShipmentDetails(getShipmentId());
        shipmentDetailsPage.markAsDelivered();
    }

    @When("admin views shipment details")
    public void adminViewsShipmentDetails() {
        shipmentDetailsPage.openShipmentDetails(getShipmentId());
    }

    @Then("shipment grid is displayed with shipment records")
    public void shipmentGridIsDisplayedWithRecords() {
        AssertionUtils.assertTrue(
                shipmentListPage.isShipmentGridDisplayed(),
                "Shipment grid should display shipment records");
    }

    @Then("shipment is created and linked to the order")
    public void shipmentIsCreatedAndLinkedToOrder() {
        AssertionUtils.assertTrue(
                getShipmentId() != null && !getShipmentId().isBlank(),
                "Shipment should be created and linked to order");
        shipmentDetailsPage.openShipmentDetails(getShipmentId());
        AssertionUtils.assertTrue(
                shipmentDetailsPage.getTrackingNumber() != null,
                "Shipment details page should be accessible after creation");
    }

    @Then("shipment tracking number is saved as {string}")
    public void shipmentTrackingNumberIsSavedAs(String expectedTracking) {
        AssertionUtils.assertContains(
                shipmentDetailsPage.getTrackingNumber(),
                expectedTracking,
                "Shipment tracking number should be saved");
    }

    @Then("shipment status reflects shipped state")
    public void shipmentStatusReflectsShippedState() {
        AssertionUtils.assertTrue(
                shipmentDetailsPage.getShipmentStatusText().toLowerCase().contains("shipped"),
                "Shipment status should reflect shipped state");
    }

    @Then("shipment status reflects delivered state")
    public void shipmentStatusReflectsDeliveredState() {
        AssertionUtils.assertTrue(
                shipmentDetailsPage.getShipmentStatusText().toLowerCase().contains("delivered"),
                "Shipment status should reflect delivered state");
    }

    @Then("PDF packaging slip link is available")
    public void pdfPackagingSlipLinkIsAvailable() {
        AssertionUtils.assertTrue(
                shipmentDetailsPage.isPdfPackagingSlipAvailable(),
                "PDF packaging slip link should be available on shipment details");
    }

    private void ensureShipmentExists() {
        if (getShipmentId() != null && !getShipmentId().isBlank()) {
            return;
        }
        if (ScenarioContext.get(ORDER_ID_KEY) == null) {
            aProcessingPaidOrderExistsForAutomation();
        }
        adminCreatesShipmentForOrder();
    }

    private String getOrderId() {
        return ScenarioContext.get(ORDER_ID_KEY);
    }

    private String getShipmentId() {
        return ScenarioContext.get(SHIPMENT_ID_KEY);
    }

    private String extractShipmentIdFromUrl() {
        String currentUrl = com.nopcommerce.automation.config.DriverManager.getDriver().getCurrentUrl();
        return currentUrl.substring(currentUrl.lastIndexOf('/') + 1);
    }
}
