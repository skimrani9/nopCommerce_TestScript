@Registered @customer @smoke @regression @Sanity @registered-login

Feature: Customer Login and Logout

  As a registered customer
  I want to sign in and sign out of the storefront
  So that I can access my account securely

  @TC-NC-TR01001 @RegisteredLoginFlow @requires-data
  Scenario: Login with valid email and password
    Given a registered E2E customer account exists
    When registered customer opens the login page
    And registered customer submits valid credentials
    Then registered customer is authenticated on the storefront

  # @TC-NC-TR01002 @RegisteredLoginFlow @requires-data
  # Scenario: Login fails with incorrect password
  #   Given a registered E2E customer account exists
  #   When registered customer opens the login page
  #   And registered customer submits correct email with wrong password "WrongPass123"
  #   Then login error is displayed and customer remains unauthenticated

  # @TC-NC-TR01003 @RegisteredLoginFlow @requires-data
  # Scenario: Logout ends customer session
  #   Given a registered E2E customer account exists
  #   And customer is logged into the storefront
  #   When registered customer logs out from the storefront
  #   Then registered customer session is cleared and guest links are visible
