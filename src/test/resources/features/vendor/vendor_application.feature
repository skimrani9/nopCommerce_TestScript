@VendorStorefront @vendor @smoke @regression @Sanity @vendor-application

Feature: Vendor Application

  As a registered customer
  I want to apply for a vendor account on the storefront
  So that I can sell products in the marketplace

  @TC-NC-TV01001 @VendorApplicationFlow @requires-data
  Scenario: Apply for vendor account with valid data
    Given a registered customer without a vendor account exists
    And customer is logged into the storefront
    When customer opens the vendor application page
    And customer submits vendor application with shop details
    Then vendor application success message is displayed

  @TC-NC-TV01002 @VendorApplicationFlow
  Scenario: Guest is redirected from vendor application page
    When guest navigates to the vendor application page directly
    Then guest is redirected to the login page

  @TC-NC-TV01003 @VendorApplicationFlow @requires-data
  Scenario: Customer with existing vendor application sees pending result
    Given an E2E vendor applicant with submitted application exists
    And customer is logged into the storefront
    When customer opens the vendor application page
    Then vendor application pending message is displayed
