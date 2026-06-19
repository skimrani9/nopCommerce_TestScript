package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.utils.AssertionUtils;
import com.nopcommerce.automation.utils.BackgroundTaskHttpHelper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BackgroundTaskHttpSteps {

    private static final String SCHEDULE_TASK_STATUS_KEY = "scheduleTaskHttpStatus";

    @When("background task runner posts keep alive schedule task")
    public void backgroundTaskRunnerPostsKeepAliveScheduleTask() {
        storeStatus(BackgroundTaskHttpHelper.postScheduleTask(Constants.KEEP_ALIVE_TASK_TYPE));
    }

    @When("background task runner posts send emails schedule task")
    public void backgroundTaskRunnerPostsSendEmailsScheduleTask() {
        storeStatus(BackgroundTaskHttpHelper.postScheduleTask(Constants.SEND_EMAILS_TASK_TYPE));
    }

    @When("background task runner posts unknown schedule task type")
    public void backgroundTaskRunnerPostsUnknownScheduleTaskType() {
        storeStatus(BackgroundTaskHttpHelper.postScheduleTask(Constants.INVALID_SCHEDULE_TASK_TYPE));
    }

    @When("background task runner posts schedule task without task type")
    public void backgroundTaskRunnerPostsScheduleTaskWithoutTaskType() {
        storeStatus(BackgroundTaskHttpHelper.postScheduleTaskWithoutTaskType());
    }

    @When("background task runner sends GET to schedule task endpoint")
    public void backgroundTaskRunnerSendsGetToScheduleTaskEndpoint() {
        storeStatus(BackgroundTaskHttpHelper.getScheduleTaskEndpoint());
    }

    @Then("schedule task HTTP response status is {int}")
    public void scheduleTaskHttpResponseStatusIs(int expectedStatus) {
        Integer actualStatus = ScenarioContext.get(SCHEDULE_TASK_STATUS_KEY);
        AssertionUtils.assertTrue(
                actualStatus != null && actualStatus == expectedStatus,
                "Schedule task HTTP status should be " + expectedStatus + " but was " + actualStatus);
    }

    @Then("schedule task endpoint does not redirect to admin")
    public void scheduleTaskEndpointDoesNotRedirectToAdmin() {
        Integer status = ScenarioContext.get(SCHEDULE_TASK_STATUS_KEY);
        AssertionUtils.assertTrue(
                status != null && status != 301 && status != 302 && status != 303 && status != 307 && status != 308,
                "Schedule task endpoint should not redirect, but status was " + status);
    }

    private void storeStatus(int statusCode) {
        ScenarioContext.set(SCHEDULE_TASK_STATUS_KEY, statusCode);
    }
}
