@Guest @guest @smoke @regression @Sanity @guest-catalog

Feature: Guest Product Catalog and Search

  As a guest visitor
  I want to browse categories and search products
  So that I can find items before adding them to cart

  @TC-NC-TG01004 @GuestCatalogFlow @requires-data
  Scenario: Search products by keyword
    When guest navigates to the storefront homepage
    And guest searches storefront for "E2E Guest"
    Then search results list matching products

  @TC-NC-TG01005 @GuestCatalogFlow
  Scenario: Search returns no results for invalid term
    When guest navigates to the storefront homepage
    And guest searches storefront for "xyznonexistentproduct999"
    Then search results show no products found message

  @TC-NC-TG01006 @GuestCatalogFlow @requires-data
  Scenario: Browse category product listing
    When guest navigates to the storefront homepage
    And guest opens category "e2e-guest-catalog"
    Then category page displays "E2E Guest Catalog" with product grid
