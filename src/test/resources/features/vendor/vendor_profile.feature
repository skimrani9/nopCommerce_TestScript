@VendorStorefront @vendor @smoke @regression @vendor-profile

Feature: Vendor Profile

  As an approved vendor on the storefront
  I want to manage my vendor profile
  So that customers see accurate shop information

  @TC-NC-TV01007 @VendorProfileFlow @requires-data
  Scenario: Vendor updates profile description
    Given an active E2E vendor storefront exists
    And E2E vendor owner is logged into the storefront
    When vendor opens the vendor info page
    And vendor updates description to the E2E updated storefront text
    Then vendor profile update success is displayed

  @TC-NC-TV01008 @VendorProfileFlow @requires-data
  Scenario: Updated vendor description appears on public page
    Given an active E2E vendor storefront exists
    And E2E vendor owner is logged into the storefront
    And vendor profile description is set to the E2E updated storefront text
    When guest opens the E2E vendor public page
    Then vendor public page shows the E2E updated storefront description

  @TC-NC-TV01009 @VendorProfileFlow
  Scenario: Vendor info page requires authentication
    When guest navigates to the vendor info page directly
    Then guest is redirected to the login page
