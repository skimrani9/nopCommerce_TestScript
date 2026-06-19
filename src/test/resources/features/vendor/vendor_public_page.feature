@VendorStorefront @vendor @smoke @regression @vendor-public-page

Feature: Public Vendor Page

  As a storefront visitor
  I want to view an active vendor page
  So that I can learn about the seller and contact them

  @TC-NC-TV01004 @VendorPublicPageFlow @requires-data
  Scenario: View active vendor public page
    Given an active E2E vendor storefront exists
    When guest opens the E2E vendor public page
    Then vendor public page displays the E2E vendor shop name

  @TC-NC-TV01005 @VendorPublicPageFlow @requires-data
  Scenario: Vendor public page displays shop description
    Given an active E2E vendor storefront exists
    When guest opens the E2E vendor public page
    Then vendor public page displays the E2E vendor description

  @TC-NC-TV01006 @VendorPublicPageFlow @requires-data
  Scenario: Submit contact vendor form from vendor page
    Given an active E2E vendor storefront exists
    When guest opens the E2E vendor public page
    And guest submits contact vendor enquiry
    Then contact vendor success message is displayed
