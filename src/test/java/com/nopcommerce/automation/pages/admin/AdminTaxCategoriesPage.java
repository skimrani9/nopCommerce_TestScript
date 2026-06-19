package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminTaxCategoriesPage extends BasePage {

    private final By taxCategoriesGrid = By.id("tax-categories-grid");
    private final By addTaxCategoryButton = By.id("addTaxCategory");
    private final By addTaxCategoryNameInput = By.id("AddTaxCategory_Name");

    public void openTaxCategories() {
        navigateTo(Constants.ADMIN_TAX_CATEGORIES_PATH);
        WaitUtils.waitForElement(taxCategoriesGrid, WaitType.VISIBLE);
    }

    public void createTaxCategory(String name) {
        openTaxCategories();
        type(addTaxCategoryNameInput, name);
        click(addTaxCategoryButton);
        reloadDataGrid("#tax-categories-grid");
    }

    public boolean isTaxCategoryListed(String name) {
        openTaxCategories();
        By categoryCell = By.xpath(
                "//table[@id='tax-categories-grid']//td[contains(normalize-space(.), '" + name + "')]");
        try {
            WaitUtils.waitForElement(categoryCell, WaitType.VISIBLE);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean isTaxCategoriesGridDisplayed() {
        return isDisplayed(By.id("tax-categories-grid"));
    }
}
