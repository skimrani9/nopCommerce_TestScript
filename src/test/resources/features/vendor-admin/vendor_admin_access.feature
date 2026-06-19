@VendorAdmin @vendor @admin @smoke @regression @vendor-admin-access

Feature: Vendor Admin Access Control

  As a vendor admin
  I want scoped admin permissions enforced
  So that I cannot manage store-wide admin areas

  @TC-NC-TVA01007 @VendorAdminAccessFlow @requires-data
  Scenario: Vendor admin is denied customer management access
    Given vendor admin is logged into the admin panel
    When vendor admin navigates to the customer list page
    Then vendor admin access denied page is displayed

  @TC-NC-TVA01008 @VendorAdminAccessFlow @requires-data
  Scenario: Vendor admin is denied store configuration access
    Given vendor admin is logged into the admin panel
    When vendor admin navigates to the general settings page
    Then vendor admin access denied page is displayed

  @TC-NC-TVA01009 @VendorAdminAccessFlow @requires-data
  Scenario: Vendor admin sidebar shows catalog without configuration menu
    Given vendor admin is logged into the admin panel
    When vendor admin navigates to the dashboard home page
    Then vendor admin sidebar shows catalog menu
    And vendor admin sidebar does not show configuration menu
