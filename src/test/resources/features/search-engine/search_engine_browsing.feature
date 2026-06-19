@SearchEngine @system @smoke @regression @Sanity @search-engine-browsing

Feature: Search Engine Crawler Browsing

  As a search engine crawler
  I want to browse public storefront pages
  So that product content can be indexed

  @TC-NC-TSE01001 @SearchEngineBrowsingFlow
  Scenario: Search engine crawler loads storefront homepage
    Given search engine crawler session is active
    When search engine crawler navigates to the storefront homepage
    Then homepage displays header footer and main content

  @TC-NC-TSE01002 @SearchEngineBrowsingFlow @requires-data
  Scenario: Search engine crawler searches storefront products
    Given search engine crawler session is active
    When search engine crawler searches storefront for "E2E Guest"
    Then search results list matching products

  @TC-NC-TSE01003 @SearchEngineBrowsingFlow @requires-data
  Scenario: Search engine crawler opens category listing
    Given search engine crawler session is active
    When search engine crawler opens category "e2e-guest-catalog"
    Then category page displays "E2E Guest Catalog" with product grid
