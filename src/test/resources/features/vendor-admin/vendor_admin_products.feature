@VendorAdmin @vendor @admin @smoke @regression @vendor-admin-products

Feature: Vendor Admin Product Management

  As a vendor admin
  I want to manage my vendor products in the admin panel
  So that I can sell items scoped to my vendor account

  @TC-NC-TVA01004 @VendorAdminProductFlow @requires-data
  Scenario: Vendor admin creates a simple product
    Given vendor admin is logged into the admin panel
    When vendor admin creates a published simple product with name "E2E Vendor Admin Product" sku prefix "E2E-VADM-" and price "149.99"
    Then vendor admin product appears in product list

  @TC-NC-TVA01005 @VendorAdminProductFlow @requires-data
  Scenario: Vendor admin updates product price
    Given vendor admin is logged into the admin panel
    And a vendor admin product exists with name "E2E Vendor Edit Product" sku prefix "E2E-VED-" and price "199.99"
    When vendor admin updates the product price to "179.99"
    Then vendor admin product price is updated in admin to "179.99"

  @TC-NC-TVA01006 @VendorAdminProductFlow @requires-data
  Scenario: Vendor admin opens product edit from list
    Given vendor admin is logged into the admin panel
    And a vendor admin product exists with name "E2E Vendor Open Product" sku prefix "E2E-VOP-" and price "89.99"
    When vendor admin opens the product edit page from the list
    Then vendor admin product edit page is displayed
