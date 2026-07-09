@Administrator @admin-orders @admin @smoke @regression @Sanity @StableTests

Feature: Order Management

  As an administrator
  I want to manage customer orders
  So that order fulfillment and payments are handled correctly

  @TC-NC-T34001 @AdminOrderFlow @requires-data
  Scenario: View order list with filters
    Given admin is logged into the admin panel
    And at least one order exists in the system
    When admin filters order list by status "Pending"
    Then filtered order grid displays matching orders

  @TC-NC-T34002 @AdminOrderFlow @requires-data
  Scenario: View order detail
    Given admin is logged into the admin panel
    And a pending order exists for automation
    When admin opens the order detail page
    Then order line items addresses totals and status are displayed

  @TC-NC-T34003 @AdminOrderFlow @requires-data
  Scenario: Change order status
    Given admin is logged into the admin panel
    And a pending order exists for automation
    When admin changes the order status to "Processing"
    Then order status is updated to "Processing"

  @TC-NC-T34004 @AdminOrderFlow @requires-data
  Scenario: Mark order as paid
    Given admin is logged into the admin panel
    And a pending order exists for automation
    When admin marks the order as paid
    Then order payment status is "Paid"

  @TC-NC-T34005 @AdminOrderFlow @requires-data
  Scenario: Refund order offline
    Given admin is logged into the admin panel
    And a paid order exists for automation
    When admin refunds the order offline
    Then order payment status is "Refunded"

  @TC-NC-T34006 @AdminOrderFlow @requires-data
  Scenario: Cancel order from admin
    Given admin is logged into the admin panel
    And a pending order exists for automation
    When admin cancels the order
    Then order status is "Cancelled"
