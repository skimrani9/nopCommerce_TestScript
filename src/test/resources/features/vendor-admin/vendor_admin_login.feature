@VendorAdmin @vendor @admin @smoke @regression @Sanity @vendor-admin-login

Feature: Vendor Admin Login

  As a vendor operator with admin permissions
  I want to sign in to the scoped admin panel
  So that I can manage my marketplace seller account

  @TC-NC-TVA01001 @VendorAdminLoginFlow @requires-data
  Scenario: Vendor admin login with valid credentials
    Given a vendor admin account exists
    When vendor admin opens the admin login page
    And vendor admin submits valid credentials
    Then vendor admin is redirected to the admin dashboard

  @TC-NC-TVA01002 @VendorAdminLoginFlow @requires-data
  Scenario: Vendor admin dashboard loads successfully
    Given vendor admin is logged into the admin panel
    When vendor admin navigates to the dashboard home page
    Then vendor admin dashboard page is displayed

  @TC-NC-TVA01003 @VendorAdminLoginFlow @requires-data
  Scenario: Vendor applicant without admin role is denied admin access
    Given an E2E vendor applicant with submitted application exists
    And customer is logged into the storefront
    When user navigates to the admin area
    Then customer is denied access to the admin area
