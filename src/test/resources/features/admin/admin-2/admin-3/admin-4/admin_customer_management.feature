@Administrator @admin-customers @admin @smoke @regression @Sanity @StableTests

Feature: Customer Management

  As an administrator
  I want to manage customer accounts
  So that customer data and access can be controlled

  @TC-NC-T36001 @AdminCustomerFlow @requires-data
  Scenario: View customer list with search
    Given admin is logged into the admin panel
    When admin searches customer list by email "admin@yourStore.com"
    Then matching customer record appears in customer grid

  @TC-NC-T36002 @AdminCustomerFlow @requires-data
  Scenario: Create new customer from admin
    Given admin is logged into the admin panel
    When admin creates a customer with email prefix "admin.created" and password "AdminCreate@123"
    Then customer appears in admin customer list

  @TC-NC-T36003 @AdminCustomerFlow @requires-data
  Scenario: Assign customer role to customer
    Given admin is logged into the admin panel
    And a test customer exists with email prefix "e2e.role.customer"
    When admin assigns customer role "Registered" to the customer
    Then customer has role "Registered" assigned in admin

  @TC-NC-T36004 @AdminCustomerFlow @requires-data
  Scenario: Impersonate customer
    Given admin is logged into the admin panel
    And a test customer exists with email prefix "e2e.impersonate.customer"
    When admin impersonates the customer
    Then admin session acts as customer on storefront

  @TC-NC-T36005 @AdminCustomerFlow @requires-data
  Scenario: Export customers to Excel
    Given admin is logged into the admin panel
    When admin exports all customers to Excel from customer list
    Then customer export action is triggered successfully

  @TC-NC-T36006 @AdminCustomerFlow @requires-data
  Scenario: Delete customer
    Given admin is logged into the admin panel
    And a deletable test customer exists with email prefix "e2e.delete.customer"
    When admin deletes the customer
    Then customer is not listed in admin customer list
