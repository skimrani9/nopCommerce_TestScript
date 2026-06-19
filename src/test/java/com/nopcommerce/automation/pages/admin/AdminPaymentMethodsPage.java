package com.nopcommerce.automation.pages.admin;



import com.nopcommerce.automation.constants.Constants;

import com.nopcommerce.automation.constants.WaitType;

import com.nopcommerce.automation.pages.BasePage;

import com.nopcommerce.automation.utils.WaitUtils;

import org.openqa.selenium.By;

import org.openqa.selenium.WebElement;



public class AdminPaymentMethodsPage extends BasePage {



    private static final String PAYMENT_METHODS_GRID_ID = "paymentmethods-grid";



    private final By paymentMethodsGrid = By.id(PAYMENT_METHODS_GRID_ID);



    public void openPaymentMethods() {

        navigateTo(Constants.ADMIN_PAYMENT_METHODS_PATH);

        WaitUtils.waitForElement(paymentMethodsGrid, WaitType.VISIBLE);

    }



    public void setPaymentMethodActive(String systemName, boolean active) {

        openPaymentMethods();

        By row = dataGridRowLocator(PAYMENT_METHODS_GRID_ID, systemName);

        WaitUtils.waitForElement(row, WaitType.VISIBLE);

        String rowId = toDataGridRowId(systemName);

        By editButton = By.cssSelector(

                "table#" + PAYMENT_METHODS_GRID_ID + " tr#" + rowId + " td.column-edit button");

        click(editButton);

        By activeCheckbox = By.cssSelector(

                "table#" + PAYMENT_METHODS_GRID_ID + " tr#" + rowId + " input[name='IsActive']");

        WebElement checkbox = WaitUtils.waitForElement(activeCheckbox, WaitType.VISIBLE);

        if (checkbox.isSelected() != active) {

            checkbox.click();

        }

        By updateButton = By.cssSelector(

                "table#" + PAYMENT_METHODS_GRID_ID + " tr#" + rowId

                        + " td.column-edit button.btn-success");

        click(updateButton);

        reloadDataGrid("#paymentmethods-grid");

    }



    public boolean isPaymentMethodActive(String systemName) {

        openPaymentMethods();

        String rowId = toDataGridRowId(systemName);

        By activeCell = By.cssSelector(

                "table#" + PAYMENT_METHODS_GRID_ID + " tr#" + rowId + " td.column-IsActive i.fa-check");

        return isDisplayed(activeCell);

    }



    public boolean isPaymentMethodListed(String systemName) {

        return isDisplayed(dataGridRowLocator(PAYMENT_METHODS_GRID_ID, systemName));

    }



    public boolean isPaymentMethodsGridDisplayed() {

        openPaymentMethods();

        return isDisplayed(paymentMethodsGrid);

    }

}


