package com.nopcommerce.automation.steps;



import com.nopcommerce.automation.pages.admin.AdminDashboardPage;

import com.nopcommerce.automation.utils.AssertionUtils;

import io.cucumber.java.en.Then;

import io.cucumber.java.en.When;



public class AdminDashboardSteps {



    private final AdminDashboardPage adminDashboardPage = new AdminDashboardPage();



    @When("admin navigates to the dashboard home page")

    public void adminNavigatesToTheDashboardHomePage() {

        adminDashboardPage.openDashboard();

        adminDashboardPage.waitForDashboardLoad();

    }



    @When("admin loads order statistics chart for month period")

    public void adminLoadsOrderStatisticsChartForMonthPeriod() {

        adminDashboardPage.loadOrderStatisticsForMonth();

    }



    @When("admin views bestsellers report widget on dashboard")

    public void adminViewsBestsellersReportWidgetOnDashboard() {

        adminDashboardPage.waitForBestsellersWidgetContent();

    }



    @When("admin views incomplete orders report widget on dashboard")

    public void adminViewsIncompleteOrdersReportWidgetOnDashboard() {

        adminDashboardPage.waitForIncompleteOrdersWidgetContent();

    }



    @Then("dashboard renders with statistics widgets")

    public void dashboardRendersWithStatisticsWidgets() {

        AssertionUtils.assertTrue(

                adminDashboardPage.isDashboardDisplayed(),

                "Dashboard heading should be visible");

        AssertionUtils.assertTrue(

                adminDashboardPage.isOrderStatisticsWidgetDisplayed()

                        || adminDashboardPage.isCustomerStatisticsWidgetDisplayed(),

                "At least one dashboard statistics widget should be visible");

    }



    @Then("order statistics chart is displayed for selected period")

    public void orderStatisticsChartIsDisplayedForSelectedPeriod() {

        AssertionUtils.assertTrue(

                adminDashboardPage.isOrderStatisticsChartDisplayed(),

                "Order statistics chart should be visible after selecting month period");

    }



    @Then("bestsellers report widget lists top selling products")

    public void bestsellersReportWidgetListsTopSellingProducts() {

        AssertionUtils.assertTrue(

                adminDashboardPage.isBestsellersWidgetDisplayed(),

                "Bestsellers report widget should display product data");

    }



    @Then("incomplete orders report widget displays pending order count")

    public void incompleteOrdersReportWidgetDisplaysPendingOrderCount() {

        AssertionUtils.assertTrue(

                adminDashboardPage.isIncompleteOrdersWidgetDisplayed(),

                "Incomplete orders report widget should display pending order information");

    }

}

