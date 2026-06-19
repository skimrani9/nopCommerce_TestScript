package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.config.ConfigReader;
import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class AdminCategoryEditPage extends BasePage {

    private final By categoryNameInput = By.id("Name");
    private final By parentCategoryDropdown = By.id("ParentCategoryId");
    private final By publishedCheckbox = By.id("Published");
    private final By seoNameInput = By.id("SeName");
    private final By saveButton = By.cssSelector("#category-form button[name='save']");
    private final By productsGrid = By.id("products-grid");

    public void fillCategoryDetails(String name, boolean published) {
        expandAdminCardByName("category-info");
        type(categoryNameInput, name);
        setCheckbox(publishedCheckbox, published);
    }

    public void setParentCategory(String parentCategoryName) {
        expandAdminCardByName("category-info");
        selectDropdownOptionByText(parentCategoryDropdown, parentCategoryName);
    }

    public void setSeoName(String seoName) {
        expandAdminCardByName("category-seo");
        WebElement seoInput = WaitUtils.waitForElement(seoNameInput, WaitType.VISIBLE);
        scrollIntoView(seoInput);
        ((JavascriptExecutor) driver).executeScript(
                "var input = arguments[0];"
                        + "input.value = arguments[1];"
                        + "input.dispatchEvent(new Event('input', { bubbles: true }));"
                        + "input.dispatchEvent(new Event('change', { bubbles: true }));",
                seoInput, seoName);
    }

    public void setPublished(boolean published) {
        expandAdminCardByName("category-display");
        setCheckbox(publishedCheckbox, published);
    }

    public void clickSave() {
        click(saveButton);
    }

    public void waitForCategoryListRedirect() {
        waitForSaveComplete();
    }

    public void waitForSaveComplete() {
        try {
            WaitUtils.waitForUrlContains(Constants.ADMIN_CATEGORY_LIST_PATH);
        } catch (Exception exception) {
            waitForEditPageLoad();
        }
    }

    public void waitForEditPageLoad() {
        WaitUtils.waitForUrlContains("/category/edit");
        WaitUtils.waitForElement(categoryNameInput, WaitType.VISIBLE);
    }

    public String getCategoryId() {
        waitForEditPageLoad();
        return extractIdFromCurrentUrl();
    }

    public void deleteCategory() {
        waitForEditPageLoad();
        clickDeleteConfirmation("category-delete");
        waitForCategoryListRedirect();
    }

    public void addProductToCategory(String productName) {
        waitForEditPageLoad();
        expandAdminCardByName("category-products");
        String categoryId = getCategoryId();
        String popupUrl = ConfigReader.getBaseUrl() + "/Admin/Category/ProductAddPopup?categoryId=" + categoryId;
        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", popupUrl);
        runInNewWindow(() -> {
            WaitUtils.waitForElement(By.id("SearchProductName"), WaitType.VISIBLE);
            type(By.id("SearchProductName"), productName);
            click(By.id("search-products"));
            By productCheckbox = By.xpath(
                    "//table[@id='products-grid']//tr[.//td[contains(normalize-space(.), '"
                            + productName + "')]]//input[contains(@class,'checkboxGroups')]");
            WebElement checkbox = WaitUtils.waitForElement(productCheckbox, WaitType.PRESENCE);
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
            click(By.cssSelector("button[name='save']"));
        });
        if (isDisplayedQuick(By.id("btnRefreshProducts"))) {
            click(By.id("btnRefreshProducts"));
        }
        reloadDataGrid("#products-grid");
    }

    public boolean isProductMappedToCategory(String productName) {
        expandAdminCardByName("category-products");
        By productCell = By.xpath(
                "//table[@id='products-grid']//td[contains(normalize-space(.), '" + productName + "')]");
        try {
            WaitUtils.waitForElement(productCell, WaitType.VISIBLE);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public String getSeoNameValue() {
        expandAdminCardByName("category-seo");
        return getInputValue(seoNameInput);
    }
}
