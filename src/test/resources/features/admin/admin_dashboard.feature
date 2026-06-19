@Administrator @admin-dashboard @admin @smoke @regression @Sanity @StableTests

Feature: Admin Dashboard

  As an administrator

  I want to view store statistics on the dashboard

  So that I can monitor store performance



  @TC-NC-T30001 @AdminDashboardFlow @requires-data

  Scenario: Dashboard loads with statistics widgets

    Given admin is logged into the admin panel

    When admin navigates to the dashboard home page

    Then dashboard renders with statistics widgets



  @TC-NC-T30002 @AdminDashboardFlow @requires-data

  Scenario: Load order statistics chart by period

    Given admin is logged into the admin panel

    When admin navigates to the dashboard home page

    And admin loads order statistics chart for month period

    Then order statistics chart is displayed for selected period



  @TC-NC-T30003 @AdminDashboardFlow @requires-data

  Scenario: Bestsellers brief report displays on dashboard

    Given admin is logged into the admin panel

    When admin navigates to the dashboard home page

    And admin views bestsellers report widget on dashboard

    Then bestsellers report widget lists top selling products



  @TC-NC-T30004 @AdminDashboardFlow @requires-data

  Scenario: Incomplete orders report widget shows pending orders

    Given admin is logged into the admin panel

    When admin navigates to the dashboard home page

    And admin views incomplete orders report widget on dashboard

    Then incomplete orders report widget displays pending order count

