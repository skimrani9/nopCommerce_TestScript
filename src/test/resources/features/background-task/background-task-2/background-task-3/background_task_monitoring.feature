@BackgroundTask @system @smoke @regression @Sanity @background-task-monitoring

Feature: Background Task Admin Monitoring

  As an administrator
  I want to view configured schedule tasks
  So that background task execution can be monitored

  @TC-NC-TBT01004 @BackgroundTaskMonitoringFlow
  Scenario: Admin schedule task list displays keep alive task
    Given admin is logged into the admin panel
    When admin opens the schedule task list page
    Then schedule task "Keep alive" is listed in admin

  @TC-NC-TBT01005 @BackgroundTaskMonitoringFlow
  Scenario: Admin schedule task list displays send emails task
    Given admin is logged into the admin panel
    When admin opens the schedule task list page
    Then schedule task "Send emails" is listed in admin

  @TC-NC-TBT01006 @BackgroundTaskMonitoringFlow
  Scenario: Admin schedule task list page loads successfully
    Given admin is logged into the admin panel
    When admin opens the schedule task list page
    Then admin schedule task list page is displayed
