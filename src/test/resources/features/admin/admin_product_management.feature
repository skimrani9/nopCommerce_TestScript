@Administrator @admin-products @admin @smoke @regression @Sanity @StableTests

Feature: Product Management

  As an administrator

  I want to manage catalog products

  So that products can be sold on the storefront



  @TC-NC-T31001 @AdminProductFlow @requires-data

  Scenario: Create new simple product

    Given admin is logged into the admin panel

    When admin creates a published simple product with name "E2E Test Laptop" sku prefix "E2E-LAP-" and price "999.99"

    Then product appears in admin product list

    And product is visible on storefront with price "999.99"



  @TC-NC-T31002 @AdminProductFlow @requires-data

  Scenario: Edit existing product price

    Given admin is logged into the admin panel

    And a published product exists with name "E2E Edit Laptop" sku prefix "E2E-EDIT-" and price "999.99"

    When admin updates the product price to "899.99"

    Then product price is updated in admin to "899.99"



  @TC-NC-T31003 @AdminProductFlow @requires-data

  Scenario: Delete product

    Given admin is logged into the admin panel

    And a published product exists with name "E2E Delete Laptop" sku prefix "E2E-DEL-" and price "799.99"

    When admin deletes the product from the edit page

    Then product is not listed in admin product list

    And product is not visible on storefront



  @TC-NC-T31004 @AdminProductFlow @requires-data

  Scenario: Add product attribute mapping and combination

    Given admin is logged into the admin panel

    And a published product exists with name "E2E Attribute Laptop" sku prefix "E2E-ATTR-" and price "999.99"

    When admin adds attribute mapping and combination for the product

    Then product combination appears with distinct sku and price



  @TC-NC-T31005 @AdminProductFlow @requires-data

  Scenario: Upload product picture

    Given admin is logged into the admin panel

    And a published product exists with name "E2E Picture Laptop" sku prefix "E2E-PIC-" and price "999.99"

    When admin uploads a product picture on the multimedia tab

    Then product picture appears in admin gallery and on storefront



  @TC-NC-T31006 @AdminProductFlow @requires-data

  Scenario: Bulk delete selected products

    Given admin is logged into the admin panel

    And two deletable products exist with sku prefixes "E2E-BULK-A-" and "E2E-BULK-B-"

    When admin deletes the selected products from the product list

    Then selected products are removed from admin product list

