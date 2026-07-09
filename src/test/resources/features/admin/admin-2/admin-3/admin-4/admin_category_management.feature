@Administrator @admin-categories @admin @smoke @regression @Sanity @StableTests

Feature: Category Management

  As an administrator
  I want to manage product categories
  So that products are organized on the storefront

  @TC-NC-T32001 @AdminCategoryFlow @requires-data
  Scenario: Create root category
    Given admin is logged into the admin panel
    When admin creates a published root category with name prefix "E2E Test Category"
    Then category appears in admin category list

  @TC-NC-T32002 @AdminCategoryFlow @requires-data
  Scenario: Create subcategory under parent
    Given admin is logged into the admin panel
    And a published root category exists with name prefix "E2E Parent Category"
    When admin creates a subcategory with name prefix "E2E Sub Laptops" under the parent category
    Then subcategory appears nested under parent in admin category list

  @TC-NC-T32003 @AdminCategoryFlow @requires-data
  Scenario: Assign product to category
    Given admin is logged into the admin panel
    And a published root category exists with name prefix "E2E Mapped Category"
    And a published product exists with name "E2E Category Product" sku prefix "E2E-CAT-" and price "99.99"
    When admin assigns the product to the category
    Then product is mapped to the category in admin

  @TC-NC-T32004 @AdminCategoryFlow @requires-data
  Scenario: Edit category SEO name
    Given admin is logged into the admin panel
    And a published root category exists with name prefix "E2E SEO Category"
    When admin updates the category SEO name to "e2e-test-category"
    Then category is accessible on storefront at SEO slug "e2e-test-category"

  @TC-NC-T32005 @AdminCategoryFlow @requires-data
  Scenario: Delete category without products
    Given admin is logged into the admin panel
    And an empty published category exists with name prefix "E2E Delete Category"
    When admin deletes the category
    Then category is not listed in admin category list

  @TC-NC-T32006 @AdminCategoryFlow @requires-data
  Scenario: Unpublish category hides from storefront navigation
    Given admin is logged into the admin panel
    And a published root category exists with name prefix "E2E Unpublish Category"
    When admin unpublishes the category
    Then category is not visible in storefront navigation
