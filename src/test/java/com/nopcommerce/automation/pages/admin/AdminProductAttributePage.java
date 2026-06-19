package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminProductAttributePage extends BasePage {

    private final By attributeNameInput = By.id("Name");
    private final By saveButton = By.cssSelector("form button[name='save']");
    private final By attributesGrid = By.id("products-grid");
    private final By searchAttributeNameInput = By.id("SearchProductAttributeName");
    private final By searchAttributesButton = By.id("search-productattributes");

    public void ensureProductAttributeExists(String attributeName) {
        if (isProductAttributeListed(attributeName)) {
            return;
        }
        navigateTo(Constants.ADMIN_PRODUCT_ATTRIBUTE_CREATE_PATH);
        type(attributeNameInput, attributeName);
        click(saveButton);
        WaitUtils.waitForUrlContains(Constants.ADMIN_PRODUCT_ATTRIBUTE_LIST_PATH);
    }

    private boolean isProductAttributeListed(String attributeName) {
        navigateTo(Constants.ADMIN_PRODUCT_ATTRIBUTE_LIST_PATH);
        WaitUtils.waitForElement(attributesGrid, WaitType.VISIBLE);
        type(searchAttributeNameInput, attributeName);
        click(searchAttributesButton);
        By attributeCell = By.xpath(
                "//table[@id='products-grid']//td[contains(normalize-space(.), '" + attributeName + "')]");
        return isDisplayed(attributeCell);
    }
}
