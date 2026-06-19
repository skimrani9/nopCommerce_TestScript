package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

public class AdminCustomerListPage extends BasePage {

    private final By customersGrid = By.id("customers-grid");
    private final By searchEmailInput = By.id("SearchEmail");
    private final By searchCustomersButton = By.id("search-customers");
    private final By exportExcelAllButton = By.cssSelector("button[name='exportexcel-all']");
    private final By customerRoleFilter = By.id("SelectedCustomerRoleIds");

    public void openCustomerList() {
        navigateTo(Constants.ADMIN_CUSTOMER_LIST_PATH);
        WaitUtils.waitForElement(customersGrid, WaitType.VISIBLE);
    }

    public void clickAddNewCustomer() {
        navigateTo(Constants.ADMIN_CUSTOMER_CREATE_PATH);
        WaitUtils.waitForElement(By.id("Email"), WaitType.VISIBLE);
    }

    public void searchByEmail(String email) {
        openCustomerList();
        expandAdminSearchPanelIfCollapsed();
        clearCustomerRoleFilters();
        type(searchEmailInput, email);
        click(searchCustomersButton);
        waitForAjaxComplete();
        waitForCustomerInGrid(email);
    }

    public boolean isCustomerListed(String email) {
        openCustomerList();
        expandAdminSearchPanelIfCollapsed();
        try {
            clearCustomerRoleFilters();
            type(searchEmailInput, email);
            click(searchCustomersButton);
            waitForAjaxComplete();
            waitForCustomerInGrid(email);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public void openCustomerEditByEmail(String email) {
        searchByEmail(email);
        By editLink = By.xpath(
                "//table[@id='customers-grid']//tr[.//td[contains(normalize-space(.), '" + email + "')]]"
                        + "//a[contains(@href,'Customer/Edit')]");
        click(editLink);
        WaitUtils.waitForUrlContains("/customer/edit");
    }

    public void exportAllCustomersToExcel() {
        openCustomerList();
        By exportDropdown = By.cssSelector(".content-header .btn-group .dropdown-toggle");
        click(exportDropdown);
        scrollIntoView(exportExcelAllButton);
        click(exportExcelAllButton);
        waitForAjaxComplete();
    }

    private void waitForCustomerInGrid(String email) {
        String normalizedEmail = email.toLowerCase();
        By customerCell = By.xpath(
                "//table[@id='customers-grid']//td[contains(translate(normalize-space(.),"
                        + " 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + normalizedEmail + "')]");
        WaitUtils.waitForElement(customerCell, WaitType.VISIBLE);
    }

    private void clearCustomerRoleFilters() {
        if (!isDisplayedQuick(customerRoleFilter)) {
            return;
        }
        ((JavascriptExecutor) driver).executeScript(
                "var select = document.getElementById('SelectedCustomerRoleIds');"
                        + "if (!select) { return; }"
                        + "for (var i = 0; i < select.options.length; i++) {"
                        + "  select.options[i].selected = false;"
                        + "}"
                        + "if (window.jQuery) {"
                        + "  jQuery(select).val(null).trigger('change');"
                        + "}");
    }
}
