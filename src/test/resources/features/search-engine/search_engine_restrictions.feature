@SearchEngine @system @smoke @regression @Sanity @search-engine-restrictions

Feature: Search Engine Crawler Restrictions

  As the platform
  I want to restrict search engine system account actions
  So that crawlers cannot shop or access admin areas

  @TC-NC-TSE01007 @SearchEngineRestrictionsFlow @requires-data
  Scenario: Search engine cannot add product to cart
    Given search engine crawler session is active
    When search engine crawler opens product "e2e-guest-product"
    And search engine crawler attempts to add current product to cart
    Then search engine add to cart is blocked

  @TC-NC-TSE01008 @SearchEngineRestrictionsFlow @requires-data
  Scenario: Search engine shopping cart remains empty
    Given search engine crawler session is active
    When search engine crawler opens product "e2e-guest-product"
    And search engine crawler attempts to add current product to cart
    And search engine crawler opens the shopping cart page
    Then search engine shopping cart is empty

  @TC-NC-TSE01009 @SearchEngineRestrictionsFlow
  Scenario: Search engine crawler is denied admin area access
    Given search engine crawler session is active
    When user navigates to the admin area
    Then customer is denied access to the admin area
