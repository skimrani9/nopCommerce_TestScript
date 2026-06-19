@SearchEngine @system @smoke @regression @Sanity @search-engine-seo

Feature: Search Engine SEO Endpoints

  As a search engine crawler
  I want to access SEO discovery endpoints
  So that site structure can be indexed efficiently

  @TC-NC-TSE01004 @SearchEngineSeoFlow
  Scenario: Search engine crawler can access robots.txt
    Given search engine crawler session is active
    When search engine crawler opens robots.txt
    Then robots.txt content is displayed

  @TC-NC-TSE01005 @SearchEngineSeoFlow
  Scenario: Search engine crawler can access XML sitemap
    Given search engine crawler session is active
    When search engine crawler opens XML sitemap
    Then XML sitemap content is displayed

  @TC-NC-TSE01006 @SearchEngineSeoFlow
  Scenario: Search engine crawler can access HTML sitemap
    Given search engine crawler session is active
    When search engine crawler opens the HTML sitemap page
    Then sitemap page displays category and product links
