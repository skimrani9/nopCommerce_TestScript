@BackgroundTask @system @smoke @regression @Sanity @background-task-restrictions

Feature: Background Task Endpoint Restrictions

  As the platform
  I want the schedule task endpoint to reject unsafe access patterns
  So that background task work context stays internal

  @TC-NC-TBT01007 @BackgroundTaskRestrictionsFlow
  Scenario: Schedule task endpoint rejects GET requests
    When background task runner sends GET to schedule task endpoint
    Then schedule task HTTP response status is 404

  @TC-NC-TBT01008 @BackgroundTaskRestrictionsFlow
  Scenario: Schedule task POST without task type returns empty success response
    When background task runner posts schedule task without task type
    Then schedule task HTTP response status is 204

  @TC-NC-TBT01009 @BackgroundTaskRestrictionsFlow
  Scenario: Schedule task endpoint does not redirect to admin area
    When background task runner posts keep alive schedule task
    Then schedule task HTTP response status is 204
    And schedule task endpoint does not redirect to admin
