@Guest @guest @smoke @regression @Sanity @guest-homepage

Feature: Guest Homepage and Navigation

  As a guest visitor
  I want to browse the storefront homepage and site navigation
  So that I can discover products without logging in

  @TC-NC-TG01001 @GuestHomeFlow
  Scenario: Homepage loads successfully for guest
    When guest navigates to the storefront homepage
    Then homepage displays header footer and main content
    And guest login and register links are visible

  @TC-NC-TG01002 @GuestHomeFlow
  Scenario: View HTML sitemap as guest
    When guest navigates to the storefront homepage
    And guest opens the HTML sitemap page
    Then sitemap page displays category and product links

  @TC-NC-TG01003 @GuestHomeFlow
  Scenario: Guest session is anonymous on homepage
    When guest navigates to the storefront homepage
    Then guest is browsing as an anonymous visitor
