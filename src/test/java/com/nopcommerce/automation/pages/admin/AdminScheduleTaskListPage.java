package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminScheduleTaskListPage extends BasePage {

    private final By scheduleTasksGrid = By.id("schedule-tasks-grid");
    private final By pageHeading = By.cssSelector(".content-header h1");

    public void openScheduleTaskList() {
        navigateTo(Constants.ADMIN_SCHEDULE_TASK_LIST_PATH);
        WaitUtils.waitForElement(scheduleTasksGrid, WaitType.VISIBLE);
    }

    public boolean isScheduleTaskListDisplayed() {
        return isDisplayed(pageHeading) && isDisplayed(scheduleTasksGrid);
    }

    public boolean isTaskListed(String taskName) {
        waitForAjaxComplete();
        By taskRow = By.xpath(
                "//table[@id='schedule-tasks-grid']//tr[.//td[contains(normalize-space(.), '"
                        + taskName + "')]]");
        return isDisplayedQuick(taskRow);
    }
}
