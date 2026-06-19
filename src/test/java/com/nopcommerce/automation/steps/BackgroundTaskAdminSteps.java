package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.admin.AdminScheduleTaskListPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BackgroundTaskAdminSteps {

    private final AdminScheduleTaskListPage adminScheduleTaskListPage = new AdminScheduleTaskListPage();

    @When("admin opens the schedule task list page")
    public void adminOpensTheScheduleTaskListPage() {
        adminScheduleTaskListPage.openScheduleTaskList();
    }

    @Then("schedule task {string} is listed in admin")
    public void scheduleTaskIsListedInAdmin(String taskName) {
        AssertionUtils.assertTrue(
                adminScheduleTaskListPage.isTaskListed(taskName),
                "Schedule task should be listed in admin: " + taskName);
    }

    @Then("admin schedule task list page is displayed")
    public void adminScheduleTaskListPageIsDisplayed() {
        AssertionUtils.assertTrue(
                adminScheduleTaskListPage.isScheduleTaskListDisplayed(),
                "Admin schedule task list page should be displayed");
    }
}
