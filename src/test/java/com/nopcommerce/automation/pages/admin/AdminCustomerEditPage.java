package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class AdminCustomerEditPage extends BasePage {

    private final By emailInput = By.id("Email");
    private final By passwordInput = By.id("Password");
    private final By firstNameInput = By.id("FirstName");
    private final By lastNameInput = By.id("LastName");
    private final By activeCheckbox = By.id("Active");
    private final By customerRolesSelect = By.id("SelectedCustomerRoleIds");
    private final By saveButton = By.cssSelector(".content-header button[name='save']");
    private final By impersonateButton = By.cssSelector("button[name='impersonate']");

    public void fillNewCustomerDetails(String email, String password, String firstName, String lastName) {
        expandAdminCardByName("customer-info");
        type(emailInput, email);
        type(passwordInput, password);
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        setCheckbox(activeCheckbox, true);
    }

    public void assignCustomerRole(String roleName) {
        expandAdminCardByName("customer-info");
        WebElement rolesSelect = WaitUtils.waitForElement(customerRolesSelect, WaitType.PRESENCE);
        for (WebElement option : rolesSelect.findElements(By.tagName("option"))) {
            if (option.getText().trim().equalsIgnoreCase(roleName)) {
                String roleId = option.getAttribute("value");
                ((JavascriptExecutor) driver).executeScript(
                        "var select = arguments[0]; var value = arguments[1];"
                                + "for (var i = 0; i < select.options.length; i++) {"
                                + "  if (select.options[i].value === value) {"
                                + "    select.options[i].selected = true; break;"
                                + "  }"
                                + "}"
                                + "if (window.jQuery) { jQuery(select).trigger('change'); }",
                        rolesSelect, roleId);
                return;
            }
        }
        throw new IllegalStateException("Customer role not found: " + roleName);
    }

    public void clickSave() {
        click(saveButton);
    }

    public void waitForCustomerListRedirect() {
        WaitUtils.waitForUrlContains(Constants.ADMIN_CUSTOMER_LIST_PATH);
    }

    public void waitForEditPageLoad() {
        WaitUtils.waitForUrlContains("/customer/edit");
        WaitUtils.waitForElement(emailInput, WaitType.VISIBLE);
    }

    public void impersonateCustomer() {
        waitForEditPageLoad();
        expandAdminCardByName("customer-impersonate");
        click(impersonateButton);
        WaitUtils.waitForUrlNotContains("/admin");
    }

    public void deleteCustomer() {
        waitForEditPageLoad();
        clickDeleteConfirmation("customer-delete");
        waitForCustomerListRedirect();
    }

    public boolean isCustomerRoleSelected(String roleName) {
        expandAdminCardByName("customer-info");
        WebElement rolesSelect = WaitUtils.waitForElement(customerRolesSelect, WaitType.VISIBLE);
        for (WebElement option : rolesSelect.findElements(By.tagName("option"))) {
            if (option.getText().trim().equalsIgnoreCase(roleName)) {
                return option.isSelected();
            }
        }
        return false;
    }
}
