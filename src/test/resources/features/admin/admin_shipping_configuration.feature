@Administrator @admin-shipping @admin @smoke @regression @Sanity @StableTests

Feature: Shipping Configuration

  As an administrator
  I want to configure shipping methods and warehouses
  So that shipping options are available at checkout

  @TC-NC-T39001 @AdminShippingFlow @requires-data
  Scenario: View shipping methods list
    Given admin is logged into the admin panel
    When admin opens shipping methods page
    Then shipping methods grid is displayed

  @TC-NC-T39002 @AdminShippingFlow @requires-data
  Scenario: View shipping restrictions page
    Given admin is logged into the admin panel
    When admin opens shipping restrictions page
    Then shipping restrictions page is displayed

  @TC-NC-T39003 @AdminShippingFlow @requires-data
  Scenario: Create warehouse
    Given admin is logged into the admin panel
    When admin creates warehouse "E2E East Coast WH" with city "New York"
    Then warehouse "E2E East Coast WH" appears in warehouse list

  @TC-NC-T39004 @AdminShippingFlow @requires-data
  Scenario: Create shipping method
    Given admin is logged into the admin panel
    When admin creates shipping method "E2E Ground Shipping"
    Then shipping method "E2E Ground Shipping" appears in shipping methods list

  @TC-NC-T39005 @AdminShippingFlow @requires-data
  Scenario: View shipping providers page
    Given admin is logged into the admin panel
    When admin opens shipping providers page
    Then shipping providers grid is displayed
