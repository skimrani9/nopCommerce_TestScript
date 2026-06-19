package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class AdminProductListPage extends BasePage {

    private final By searchProductNameInput = By.id("SearchProductName");
    private final By searchProductsButton = By.id("search-products");
    private final By goToSkuInput = By.id("GoDirectlyToSku");
    private final By goToSkuButton = By.id("go-to-product-by-sku");
    private final By deleteSelectedButton = By.id("delete-selected");
    private final By deleteSelectedConfirmButton = By.id("delete-selected-action-confirmation-submit-button");
    private final By deleteSelectedConfirmModal = By.cssSelector("#delete-selected-action-confirmation.show");
    private final By productsGrid = By.id("products-grid");

    public void openProductList() {
        navigateTo(Constants.ADMIN_PRODUCT_LIST_PATH);
        WaitUtils.waitForElement(productsGrid, WaitType.VISIBLE);
    }

    public void clickAddNewProduct() {
        navigateTo(Constants.ADMIN_PRODUCT_CREATE_PATH);
        WaitUtils.waitForElement(By.id("Name"), WaitType.VISIBLE);
    }

    public void searchByProductName(String productName) {
        type(searchProductNameInput, productName);
        click(searchProductsButton);
        waitForProductInGrid(productName);
    }

    public void searchBySkuPrefix(String skuPrefix) {
        type(searchProductNameInput, skuPrefix);
        click(searchProductsButton);
        WaitUtils.waitForElement(productsGrid, WaitType.VISIBLE);
    }

    public void openProductEditBySku(String sku) {
        openProductList();
        type(goToSkuInput, sku);
        click(goToSkuButton);
        WaitUtils.waitForUrlContains("/product/edit");
    }

    public void openProductEditFromList(String productName) {
        searchByProductName(productName);
        By editLink = By.xpath(
                "//table[@id='products-grid']//tr[.//td[contains(normalize-space(.), '" + productName + "')]]"
                        + "//a[contains(@href,'Product/Edit')]");
        click(editLink);
        WaitUtils.waitForUrlContains("/product/edit");
    }

    public boolean isProductListedBySku(String sku) {
        openProductList();
        type(goToSkuInput, sku);
        click(goToSkuButton);
        try {
            WaitUtils.waitForUrlContains("/product/edit");
            return true;
        } catch (Exception exception) {
            WaitUtils.waitForUrlContains("/product/list");
            return false;
        }
    }

    public boolean isProductListed(String productName) {
        try {
            waitForProductInGrid(productName);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public void selectProductInGridBySku(String sku) {
        By productCheckbox = By.xpath(
                "//table[@id='products-grid']//tr[.//td[contains(normalize-space(.), '" + sku + "')]]"
                        + "//input[contains(@class,'checkboxGroups')]");
        selectCheckboxAndSyncSelectedIds(productCheckbox);
    }

    public void selectProductInGrid(String productName) {
        By productCheckbox = By.xpath(
                "//table[@id='products-grid']//tr[.//td[contains(normalize-space(.), '" + productName + "')]]"
                        + "//input[contains(@class,'checkboxGroups')]");
        selectCheckboxAndSyncSelectedIds(productCheckbox);
    }

    public void deleteSelectedProducts() {
        Long selectedCount = getSelectedProductCount();
        if (selectedCount == null || selectedCount < 1) {
            throw new IllegalStateException("No products selected for bulk delete");
        }
        click(deleteSelectedButton);
        WaitUtils.waitForElement(deleteSelectedConfirmModal, WaitType.VISIBLE);
        click(deleteSelectedConfirmButton);
        WaitUtils.waitForInvisibility(deleteSelectedConfirmModal);
        WaitUtils.waitForElement(productsGrid, WaitType.VISIBLE);
    }

    public void waitUntilProductNotListed(String productName) {
        By productCell = By.xpath(
                "//table[@id='products-grid']//td[contains(normalize-space(.), '" + productName + "')]");
        WaitUtils.waitForInvisibility(productCell);
    }

    private void selectCheckboxAndSyncSelectedIds(By productCheckbox) {
        WebElement checkbox = WaitUtils.waitForElement(productCheckbox, WaitType.PRESENCE);
        scrollIntoView(checkbox);
        if (!checkbox.isSelected()) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].checked = true;"
                            + "if (typeof jQuery !== 'undefined') { jQuery(arguments[0]).trigger('change'); }"
                            + "else { arguments[0].dispatchEvent(new Event('change', { bubbles: true })); }",
                    checkbox);
        }
    }

    private Long getSelectedProductCount() {
        Object selectedCount = ((JavascriptExecutor) driver).executeScript("return selectedIds ? selectedIds.length : 0;");
        if (selectedCount instanceof Long count) {
            return count;
        }
        if (selectedCount instanceof Integer count) {
            return count.longValue();
        }
        return 0L;
    }

    private void waitForProductInGrid(String productName) {
        By productCell = By.xpath(
                "//table[@id='products-grid']//td[contains(normalize-space(.), '" + productName + "')]");
        WaitUtils.waitForElement(productCell, WaitType.VISIBLE);
    }
}
