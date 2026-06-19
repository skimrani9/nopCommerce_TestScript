package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminCategoryListPage extends BasePage {

    private final By categoriesGrid = By.id("categories-grid");
    private final By searchCategoryNameInput = By.id("SearchCategoryName");
    private final By searchCategoriesButton = By.id("search-categories");
    private final By addNewButton = By.cssSelector("a.btn.btn-primary[href*='category/create']");

    public void openCategoryList() {
        navigateTo(Constants.ADMIN_CATEGORY_LIST_PATH);
        WaitUtils.waitForElement(categoriesGrid, WaitType.VISIBLE);
    }

    public void clickAddNewCategory() {
        navigateTo(Constants.ADMIN_CATEGORY_CREATE_PATH);
        WaitUtils.waitForElement(By.id("Name"), WaitType.VISIBLE);
    }

    public void searchByCategoryName(String categoryName) {
        type(searchCategoryNameInput, categoryName);
        click(searchCategoriesButton);
        waitForCategoryInGrid(categoryName);
    }

    public boolean isCategoryListed(String categoryName) {
        openCategoryList();
        try {
            searchByCategoryName(categoryName);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public void openCategoryEditFromList(String categoryName) {
        openCategoryList();
        searchByCategoryName(categoryName);
        By editLink = By.xpath(
                "//table[@id='categories-grid']//tr[.//td[contains(normalize-space(.), '" + categoryName + "')]]"
                        + "//a[contains(@href,'Category/Edit')]");
        click(editLink);
        WaitUtils.waitForUrlContains("/category/edit");
    }

    public boolean isCategoryNestedUnderParent(String subcategoryName, String parentName) {
        searchByCategoryName(subcategoryName);
        By parentCell = By.xpath(
                "//table[@id='categories-grid']//tr[.//td[contains(normalize-space(.), '" + subcategoryName + "')]]"
                        + "//td[contains(normalize-space(.), '" + parentName + "')]");
        return isDisplayed(parentCell);
    }

    private void waitForCategoryInGrid(String categoryName) {
        By categoryCell = By.xpath(
                "//table[@id='categories-grid']//td[contains(normalize-space(.), '" + categoryName + "')]");
        WaitUtils.waitForElement(categoryCell, WaitType.VISIBLE);
    }
}
