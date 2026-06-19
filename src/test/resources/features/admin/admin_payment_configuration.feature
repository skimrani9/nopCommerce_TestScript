@Administrator @admin-payment @admin @smoke @regression @Sanity @StableTests

Feature: Payment Method Configuration

  As an administrator
  I want to configure payment methods
  So that customers can pay at checkout

  @TC-NC-T40001 @AdminPaymentFlow @requires-data
  Scenario: Activate Check Money Order payment method
    Given admin is logged into the admin panel
    When admin activates payment method "Payments.CheckMoneyOrder"
    Then payment method "Payments.CheckMoneyOrder" is active in admin

  @TC-NC-T40002 @AdminPaymentFlow @requires-data
  Scenario: View payment restrictions page
    Given admin is logged into the admin panel
    When admin opens payment restrictions page
    Then payment restrictions page is displayed

  @TC-NC-T40003 @AdminPaymentFlow @requires-data
  Scenario: Deactivate payment method hides from checkout
    Given admin is logged into the admin panel
    And payment method "Payments.CheckMoneyOrder" is active
    When admin deactivates payment method "Payments.CheckMoneyOrder"
    Then payment method "Payments.CheckMoneyOrder" is inactive in admin

  @TC-NC-T40004 @AdminPaymentFlow @requires-data
  Scenario: Reactivate Check Money Order for checkout availability
    Given admin is logged into the admin panel
    When admin activates payment method "Payments.CheckMoneyOrder"
    Then payment method "Payments.CheckMoneyOrder" is listed in payment methods grid

  @TC-NC-T40005 @AdminPaymentFlow @requires-data
  Scenario: View payment methods list
    Given admin is logged into the admin panel
    When admin opens payment methods page
    Then payment methods grid is displayed
