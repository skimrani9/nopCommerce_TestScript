package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AdminShippingMethodsPage extends BasePage {

    private final By shippingMethodGrid = By.id("shippingmethod-grid");
    private final By addNewButton = By.cssSelector("a[href*='CreateMethod']");

    public void openShippingMethods() {
        navigateTo(Constants.ADMIN_SHIPPING_METHODS_PATH);
        WaitUtils.waitForElement(shippingMethodGrid, WaitType.VISIBLE);
    }

    public boolean isShippingMethodListed(String methodName) {
        By methodCell = By.xpath(
                "//table[@id='shippingmethod-grid']//td[contains(normalize-space(.), '" + methodName + "')]");
        return isDisplayed(methodCell);
    }

    public void clickAddNewMethod() {
        click(addNewButton);
        WaitUtils.waitForUrlContains("/createmethod");
    }

    public void fillAndSaveShippingMethod(String name, String description) {
        type(By.id("Name"), name);
        type(By.id("Description"), description);
        click(By.cssSelector("button[name='save']"));
        WaitUtils.waitForUrlContains(Constants.ADMIN_SHIPPING_METHODS_PATH);
    }
}
