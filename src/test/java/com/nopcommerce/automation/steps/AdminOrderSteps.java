package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.pages.admin.AdminOrderEditPage;
import com.nopcommerce.automation.pages.admin.AdminOrderListPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import com.nopcommerce.automation.utils.CheckoutOrderHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminOrderSteps {

    private static final String ORDER_ID_KEY = "orderId";

    private final AdminOrderListPage orderListPage = new AdminOrderListPage();
    private final AdminOrderEditPage orderEditPage = new AdminOrderEditPage();

    @Given("at least one order exists in the system")
    public void atLeastOneOrderExists() {
        orderListPage.openOrderList();
        AssertionUtils.assertTrue(
                orderListPage.isOrderGridDisplayedWithResults(),
                "At least one order must exist in the system for order list tests");
    }

    @Given("a pending order exists for automation")
    public void aPendingOrderExistsForAutomation() {
        String orderId = CheckoutOrderHelper.createPendingOrderViaCheckout();
        ScenarioContext.set(ORDER_ID_KEY, orderId);
    }

    @Given("a paid order exists for automation")
    public void aPaidOrderExistsForAutomation() {
        String orderId = CheckoutOrderHelper.createPendingOrderViaCheckout();
        orderEditPage.openOrderEdit(orderId);
        orderEditPage.markOrderAsPaid();
        ScenarioContext.set(ORDER_ID_KEY, orderId);
    }

    @When("admin filters order list by status {string}")
    public void adminFiltersOrderListByStatus(String status) {
        orderListPage.filterByOrderStatus(status);
    }

    @When("admin opens the order detail page")
    public void adminOpensOrderDetailPage() {
        orderEditPage.openOrderEdit(getOrderId());
    }

    @When("admin changes the order status to {string}")
    public void adminChangesOrderStatusTo(String status) {
        orderEditPage.openOrderEdit(getOrderId());
        orderEditPage.changeOrderStatus(status);
    }

    @When("admin marks the order as paid")
    public void adminMarksOrderAsPaid() {
        orderEditPage.openOrderEdit(getOrderId());
        orderEditPage.markOrderAsPaid();
    }

    @When("admin refunds the order offline")
    public void adminRefundsOrderOffline() {
        orderEditPage.openOrderEdit(getOrderId());
        orderEditPage.refundOrderOffline();
    }

    @When("admin cancels the order")
    public void adminCancelsOrder() {
        orderEditPage.openOrderEdit(getOrderId());
        orderEditPage.cancelOrder();
    }

    @Then("filtered order grid displays matching orders")
    public void filteredOrderGridDisplaysMatchingOrders() {
        AssertionUtils.assertTrue(
                orderListPage.isOrderGridDisplayedWithResults(),
                "Filtered order grid should display matching orders");
    }

    @Then("order line items addresses totals and status are displayed")
    public void orderLineItemsAddressesTotalsAndStatusAreDisplayed() {
        AssertionUtils.assertTrue(
                orderEditPage.isOrderDetailDisplayed(),
                "Order detail should display line items, addresses, totals, and status");
    }

    @Then("order status is updated to {string}")
    public void orderStatusIsUpdatedTo(String expectedStatus) {
        orderEditPage.openOrderEdit(getOrderId());
        AssertionUtils.assertContains(
                orderEditPage.getOrderStatusText(),
                expectedStatus,
                "Order status should be updated");
    }

    @Then("order payment status is {string}")
    public void orderPaymentStatusIs(String expectedStatus) {
        orderEditPage.openOrderEdit(getOrderId());
        AssertionUtils.assertContains(
                orderEditPage.getPaymentStatusText(),
                expectedStatus,
                "Order payment status should match expected value");
    }

    private String getOrderId() {
        String orderId = ScenarioContext.get(ORDER_ID_KEY);
        if (orderId == null || orderId.isBlank()) {
            orderId = CheckoutOrderHelper.getStoredOrderId();
        }
        return orderId;
    }
}
