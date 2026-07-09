@Administrator @admin-discounts @admin @smoke @regression @Sanity @StableTests

Feature: Discount and Coupon Management

  As an administrator
  I want to manage discounts and coupon codes
  So that promotional pricing can be applied at checkout

  @TC-NC-T37001 @AdminDiscountFlow @requires-data
  Scenario: Create percentage discount with coupon code
    Given admin is logged into the admin panel
    When admin creates an active percentage discount with name prefix "E2E 10 Percent Off" coupon "SAVE10" and percentage "10"
    Then discount appears in admin discount list

  @TC-NC-T37002 @AdminDiscountFlow @requires-data
  Scenario: Apply coupon on storefront cart
    Given admin is logged into the admin panel
    And an active discount exists with name prefix "E2E Cart Discount" coupon "SAVE10" and percentage "10"
    And a published product exists with name "E2E Discount Product" sku prefix "E2E-DISC-" and price "100.00"
    When customer adds the product to cart and applies coupon "SAVE10"
    Then discount is applied to cart total

  @TC-NC-T37003 @AdminDiscountFlow @requires-data
  Scenario: Discount with future start date is not active yet
    Given admin is logged into the admin panel
    And a future dated discount exists with name prefix "E2E Future Discount" coupon "FUTURE10" and percentage "10"
    When customer applies coupon "FUTURE10" on cart
    Then coupon is rejected on storefront cart

  @TC-NC-T37004 @AdminDiscountFlow @requires-data
  Scenario: Delete discount
    Given admin is logged into the admin panel
    And an active discount exists with name prefix "E2E Delete Discount" coupon "DEL10" and percentage "10"
    When admin deletes the discount
    Then discount is not listed in admin discount list

  @TC-NC-T37005 @AdminDiscountFlow @requires-data
  Scenario: Fixed amount discount applied correctly
    Given admin is logged into the admin panel
    And an active fixed discount exists with name prefix "E2E Flat Discount" coupon "FLAT20" and amount "20"
    And a published product exists with name "E2E Flat Product" sku prefix "E2E-FLAT-" and price "100.00"
    When customer adds the product to cart and applies coupon "FLAT20"
    Then cart total is reduced by fixed discount amount
