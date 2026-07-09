@Administrator @admin-login @admin @smoke @regression @Sanity @StableTests

Feature: Admin Login and Security

  As an administrator

  I want secure access to the admin panel

  So that only authorized users can manage the store



  @TC-NC-T29001 @AdminLoginFlow

  Scenario: Admin login with valid credentials

    Given an admin user exists with valid credentials

    When user navigates to the admin area

    And user submits valid admin email and password on login page

    Then user is redirected to the admin dashboard



  @TC-NC-T29002 @AdminLoginFlow

  Scenario: Admin login fails with invalid password

    Given an admin user exists with valid credentials

    When user navigates to the admin area

    And user submits correct admin email with wrong password "InvalidAdminPass"

    Then login error is displayed and admin area remains inaccessible



  @TC-NC-T29003 @AdminLoginFlow @customer

  Scenario: Non-admin customer denied admin area access

    Given a registered customer without admin permissions exists

    And customer is logged into the storefront

    When user navigates to the admin area

    Then customer is denied access to the admin area



  @TC-NC-T29004 @AdminLoginFlow

  Scenario: Admin logout ends admin session

    Given admin is logged into the admin panel

    When admin logs out from the admin area

    Then admin session ends and admin area requires re-authentication



  @TC-NC-T29005 @AdminLoginFlow @requires-data @admin-mfa

  Scenario: MFA verification required when enabled

    Given admin is logged into the admin panel

    And admin has Google Authenticator MFA enabled

    And admin session ends and admin area requires re-authentication

    When user navigates to the admin area

    And user submits valid admin email and password on login page

    Then user is redirected to multi-factor verification page

    When user submits invalid MFA verification code "000000"

    Then MFA verification error is displayed

    When user submits valid MFA verification code

    Then user is redirected to the admin dashboard



  @TC-NC-T29006 @AdminLoginFlow @requires-data @admin-captcha

  Scenario: CAPTCHA on admin login when configured

    Given admin is logged into the admin panel

    And CAPTCHA is enabled on the login page

    When admin logs out from the admin area

    And user opens storefront login page with admin return url

    And user submits valid admin email and password on login page without completing CAPTCHA

    Then login error is displayed and admin area remains inaccessible

    And CAPTCHA is displayed on login page

