@Guest @guest @smoke @regression @Sanity @guest-cart

Feature: Guest Shopping Cart

  As a guest visitor
  I want to manage my shopping cart
  So that I can review items before checkout

  @TC-NC-TG01007 @GuestCartFlow @requires-data
  Scenario: View cart with added items
    When guest navigates to the storefront homepage
    And guest adds product "E2E Guest Product" to cart from search
    And guest opens the shopping cart page
    Then cart displays product "E2E Guest Product" with totals

  @TC-NC-TG01008 @GuestCartFlow @requires-data
  Scenario: Proceed to checkout from cart
    When guest navigates to the storefront homepage
    And guest adds product "E2E Guest Product" to cart from search
    And guest opens the shopping cart page
    And guest clicks checkout from cart
    Then guest is redirected to checkout page

  @TC-NC-TG01009 @GuestCartFlow @requires-data
  Scenario: Apply invalid discount coupon shows error
    When guest navigates to the storefront homepage
    And guest adds product "E2E Guest Product" to cart from search
    And guest applies discount coupon "INVALID-CODE-XYZ" on cart page
    Then invalid discount coupon error is displayed
