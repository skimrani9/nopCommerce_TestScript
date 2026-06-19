@BackgroundTask @system @smoke @regression @Sanity @background-task-trigger

Feature: Background Task HTTP Trigger

  As the platform scheduler
  I want to invoke schedule tasks over HTTP
  So that background work runs in the built-in task context

  @TC-NC-TBT01001 @BackgroundTaskTriggerFlow
  Scenario: Keep alive schedule task can be triggered via HTTP POST
    When background task runner posts keep alive schedule task
    Then schedule task HTTP response status is 204

  @TC-NC-TBT01002 @BackgroundTaskTriggerFlow
  Scenario: Send emails schedule task can be triggered via HTTP POST
    When background task runner posts send emails schedule task
    Then schedule task HTTP response status is 204

  @TC-NC-TBT01003 @BackgroundTaskTriggerFlow
  Scenario: Unknown schedule task type returns empty success response
    When background task runner posts unknown schedule task type
    Then schedule task HTTP response status is 204
