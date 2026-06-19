@Administrator @admin-tax @admin @smoke @regression @Sanity @StableTests

Feature: Tax Configuration

  As an administrator
  I want to configure tax settings
  So that tax is calculated correctly at checkout

  @TC-NC-T38001 @AdminTaxFlow @requires-data
  Scenario: View tax settings page
    Given admin is logged into the admin panel
    When admin opens tax settings page
    Then tax settings page is displayed

  @TC-NC-T38002 @AdminTaxFlow @requires-data
  Scenario: Create tax category
    Given admin is logged into the admin panel
    When admin creates tax category "E2E Electronics"
    Then tax category "E2E Electronics" appears in admin tax categories list

  @TC-NC-T38003 @AdminTaxFlow @requires-data
  Scenario: View tax categories page
    Given admin is logged into the admin panel
    When admin opens tax categories page
    Then tax categories grid is displayed

  @TC-NC-T38004 @AdminTaxFlow @requires-data
  Scenario: Update tax display settings
    Given admin is logged into the admin panel
    When admin opens tax settings page
    Then tax common settings card is available

  @TC-NC-T38005 @AdminTaxFlow @requires-data
  Scenario: View tax providers page
    Given admin is logged into the admin panel
    When admin opens tax providers page
    Then tax providers grid is displayed
