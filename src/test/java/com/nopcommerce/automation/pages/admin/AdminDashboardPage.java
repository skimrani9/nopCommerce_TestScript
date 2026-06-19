package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class AdminDashboardPage extends BasePage {

    private final By dashboardHeading = By.cssSelector(".content-header h1");
    private final By orderStatisticsCard = By.id("order-statistics-card");
    private final By customerStatisticsCard = By.id("customer-statistics-card");
    private final By orderStatisticsChart = By.id("order-statistics-chart");
    private final By orderStatisticsMonthButton =
            By.cssSelector("#order-statistics-card button[data-chart-role='toggle-chart'][data-chart-period='month']");
    private final By bestsellersQuantityCard = By.id("bestsellers-report-quantity-card");
    private final By bestsellersQuantityContent = By.id("bestsellersBriefReportByQuantity");
    private final By incompleteOrdersCard = By.id("order-incomplete-report-card");
    private final By incompleteOrdersContent = By.id("orderIncompleteReport");
    private final By adminLayout = By.cssSelector("div.wrapper");

    public void openDashboard() {
        navigateTo(Constants.ADMIN_AREA_PATH);
    }

    public boolean isDashboardDisplayed() {
        return isDisplayed(dashboardHeading);
    }

    public String getDashboardHeadingText() {
        return getText(dashboardHeading);
    }

    public boolean isOrderStatisticsWidgetDisplayed() {
        return isDisplayed(orderStatisticsCard);
    }

    public boolean isCustomerStatisticsWidgetDisplayed() {
        return isDisplayed(customerStatisticsCard);
    }

    public boolean waitForDashboardLoad() {
        WaitUtils.waitForUrlContains(Constants.ADMIN_AREA_PATH);
        WaitUtils.waitForElement(dashboardHeading, WaitType.VISIBLE);
        WaitUtils.waitForElement(adminLayout, WaitType.VISIBLE);
        return isDashboardDisplayed();
    }

    public void loadOrderStatisticsForMonth() {
        expandAdminCard("order-statistics-card");
        click(orderStatisticsMonthButton);
        WaitUtils.waitForElement(orderStatisticsChart, WaitType.VISIBLE);
    }

    public boolean isOrderStatisticsChartDisplayed() {
        return isDisplayed(orderStatisticsChart);
    }

    public void waitForBestsellersWidgetContent() {
        expandAdminCard("bestsellers-report-quantity-card");
        WaitUtils.waitForElement(bestsellersQuantityContent, WaitType.VISIBLE);
    }

    public boolean isBestsellersWidgetDisplayed() {
        expandAdminCard("bestsellers-report-quantity-card");
        return isDisplayed(bestsellersQuantityCard)
                && !getText(bestsellersQuantityContent).isBlank();
    }

    public void waitForIncompleteOrdersWidgetContent() {
        expandAdminCard("order-incomplete-report-card");
        WaitUtils.waitForElement(incompleteOrdersContent, WaitType.VISIBLE);
    }

    public boolean isIncompleteOrdersWidgetDisplayed() {
        expandAdminCard("order-incomplete-report-card");
        WebElement content = WaitUtils.waitForElement(incompleteOrdersContent, WaitType.VISIBLE);
        return isDisplayed(incompleteOrdersCard) && !content.getText().isBlank();
    }
}
