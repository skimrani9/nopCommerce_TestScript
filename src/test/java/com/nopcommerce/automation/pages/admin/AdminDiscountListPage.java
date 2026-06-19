package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminDiscountListPage extends BasePage {

    private final By discountsGrid = By.id("discounts-grid");
    private final By searchDiscountNameInput = By.id("SearchDiscountName");
    private final By searchDiscountsButton = By.id("search-discounts");

    public void openDiscountList() {
        navigateTo(Constants.ADMIN_DISCOUNT_LIST_PATH);
        WaitUtils.waitForElement(discountsGrid, WaitType.VISIBLE);
    }

    public void clickAddNewDiscount() {
        navigateTo(Constants.ADMIN_DISCOUNT_CREATE_PATH);
        WaitUtils.waitForElement(By.id("Name"), WaitType.VISIBLE);
    }

    public void searchByDiscountName(String discountName) {
        type(searchDiscountNameInput, discountName);
        click(searchDiscountsButton);
        waitForDiscountInGrid(discountName);
    }

    public boolean isDiscountListed(String discountName) {
        try {
            waitForDiscountInGrid(discountName);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public void openDiscountEditFromList(String discountName) {
        searchByDiscountName(discountName);
        By editLink = By.xpath(
                "//table[@id='discounts-grid']//tr[.//td[contains(normalize-space(.), '" + discountName + "')]]"
                        + "//a[contains(@href,'Discount/Edit')]");
        click(editLink);
        WaitUtils.waitForUrlContains("/discount/edit");
    }

    private void waitForDiscountInGrid(String discountName) {
        By discountCell = By.xpath(
                "//table[@id='discounts-grid']//td[contains(normalize-space(.), '" + discountName + "')]");
        WaitUtils.waitForElement(discountCell, WaitType.VISIBLE);
    }
}
