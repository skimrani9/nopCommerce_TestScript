package com.nopcommerce.automation.pages;

import com.nopcommerce.automation.config.ConfigReader;
import com.nopcommerce.automation.config.DriverManager;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class BasePage {

    protected final Logger logger = LogManager.getLogger(getClass());
    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
    }

    protected void navigateTo(String path) {
        String url = ConfigReader.getBaseUrl() + path;
        logger.info("Navigating to {}", url);
        driver.get(url);
    }

    protected void click(By locator) {
        try {
            waitForAjaxComplete();
            WebElement element = WaitUtils.waitForElement(locator, WaitType.CLICKABLE);
            logger.info("Clicking element {}", locator);
            element.click();
        } catch (Exception exception) {
            logger.error("Unable to click element {}", locator);
            throw exception;
        }
    }

    protected void waitForAjaxComplete() {
        try {
            WaitUtils.waitForInvisibility(By.id("ajaxBusy"));
        } catch (Exception exception) {
            logger.debug("Ajax busy indicator not present or already hidden");
        }
    }

    protected void type(By locator, String value) {
        try {
            WebElement element = WaitUtils.waitForElement(locator, WaitType.VISIBLE);
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            element.sendKeys(Keys.DELETE);
            logger.info("Typing into element {}", locator);
            element.sendKeys(value);
        } catch (Exception exception) {
            logger.error("Unable to type into element {}", locator);
            throw exception;
        }
    }

    protected String getText(By locator) {
        WebElement element = WaitUtils.waitForElement(locator, WaitType.VISIBLE);
        return element.getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return WaitUtils.waitForElement(locator, WaitType.VISIBLE).isDisplayed();
        } catch (Exception exception) {
            return false;
        }
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String currentUrl() {
        return getCurrentUrl();
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }

    protected void setCheckbox(By locator, boolean checked) {
        WebElement element = WaitUtils.waitForElement(locator, WaitType.PRESENCE);
        if (element.isSelected() != checked) {
            scrollIntoView(element);
            try {
                element.click();
            } catch (Exception exception) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }
        }
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    protected void scrollIntoView(By locator) {
        scrollIntoView(WaitUtils.waitForElement(locator, WaitType.PRESENCE));
    }

    protected String getInputValue(By locator) {
        WebElement element = WaitUtils.waitForElement(locator, WaitType.VISIBLE);
        return element.getAttribute("value");
    }

    protected void expandAdminCard(String cardId) {
        By cardLocator = By.id(cardId);
        WebElement card = WaitUtils.waitForElement(cardLocator, WaitType.PRESENCE);
        if (card.getAttribute("class").contains("collapsed-card")) {
            click(By.cssSelector("#" + cardId + " button[data-card-widget='collapse']"));
        }
        WaitUtils.waitForElement(By.cssSelector("#" + cardId + " .card-body"), WaitType.VISIBLE);
    }

    protected void enableAdvancedSettingsIfPresent() {
        By advancedToggle = By.id("advanced-settings-mode");
        if (!isDisplayedQuick(advancedToggle)) {
            return;
        }
        WebElement toggle = WaitUtils.waitForElement(advancedToggle, WaitType.PRESENCE);
        if (!toggle.isSelected()) {
            click(By.cssSelector("label[for='advanced-settings-mode']"));
            waitForAjaxComplete();
        }
        ((JavascriptExecutor) driver).executeScript(
                "document.body.classList.add('advanced-settings-mode');"
                        + "document.body.classList.remove('basic-settings-mode');");
    }

    protected void expandAdminSearchPanelIfCollapsed() {
        By closedSearchBody = By.cssSelector(".card-search .search-body.closed");
        try {
            if (driver.findElement(closedSearchBody).isDisplayed()) {
                click(By.cssSelector(".card-search .search-row"));
                WaitUtils.waitForInvisibility(closedSearchBody);
            }
        } catch (Exception exception) {
            logger.debug("Admin search panel already expanded or not present");
        }
    }

    protected String toDataGridRowId(String fieldValue) {
        if (fieldValue == null || fieldValue.isBlank()) {
            return "row_";
        }
        return "row_" + fieldValue.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    protected By dataGridRowLocator(String gridId, String rowFieldValue) {
        return By.cssSelector("table#" + gridId + " tr#" + toDataGridRowId(rowFieldValue));
    }

    protected void clickAddToCartOnProductDetailsPage() {
        WebElement addButton = WaitUtils.waitForElement(
                By.cssSelector("button.add-to-cart-button[data-productid]"), WaitType.CLICKABLE);
        addButton.click();
        waitForAjaxComplete();
    }

    protected boolean isDisplayedQuick(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception exception) {
            return false;
        }
    }

    protected void expandAdminCardByName(String cardName) {
        enableAdvancedSettingsIfPresent();
        By cardLocator = By.cssSelector("[data-card-name='" + cardName + "']");
        WebElement card = WaitUtils.waitForElement(cardLocator, WaitType.PRESENCE);
        if (card.getAttribute("class").contains("advanced-setting")) {
            ((JavascriptExecutor) driver).executeScript(
                    "document.body.classList.add('advanced-settings-mode');"
                            + "document.body.classList.remove('basic-settings-mode');");
        }
        scrollIntoView(card);
        if (card.getAttribute("class").contains("collapsed-card")) {
            By collapseButton = By.cssSelector("[data-card-name='" + cardName + "'] button[data-card-widget='collapse']");
            if (isDisplayedQuick(collapseButton)) {
                click(collapseButton);
            }
        }
        WaitUtils.waitForElement(By.cssSelector("[data-card-name='" + cardName + "'] .card-body"), WaitType.VISIBLE);
    }

    protected void selectDropdownOptionByText(By dropdown, String visibleText) {
        WebElement selectElement = WaitUtils.waitForElement(dropdown, WaitType.VISIBLE);
        scrollIntoView(selectElement);
        for (WebElement option : selectElement.findElements(By.tagName("option"))) {
            if (option.getText().trim().equalsIgnoreCase(visibleText)
                    || option.getText().trim().contains(visibleText)) {
                option.click();
                return;
            }
        }
        throw new IllegalStateException("Dropdown option not found: " + visibleText);
    }

    protected void selectDropdownOptionByValue(By dropdown, String value) {
        WebElement selectElement = WaitUtils.waitForElement(dropdown, WaitType.VISIBLE);
        scrollIntoView(selectElement);
        for (WebElement option : selectElement.findElements(By.tagName("option"))) {
            if (value.equals(option.getAttribute("value"))) {
                option.click();
                return;
            }
        }
        throw new IllegalStateException("Dropdown value not found: " + value);
    }

    protected void clickActionConfirmation(String buttonId) {
        By triggerButton = By.id(buttonId);
        scrollIntoView(triggerButton);
        click(triggerButton);
        By confirmButton = By.id(buttonId + "-action-confirmation-submit-button");
        WaitUtils.waitForElement(confirmButton, WaitType.CLICKABLE);
        click(confirmButton);
        waitForAjaxComplete();
    }

    protected void clickDeleteConfirmation(String buttonId) {
        WebElement deleteBtn = WaitUtils.waitForElement(By.id(buttonId), WaitType.VISIBLE);
        String modalSelector = deleteBtn.getAttribute("data-target");
        if (modalSelector == null || modalSelector.isBlank()) {
            throw new IllegalStateException("Delete button is missing data-target for confirmation modal");
        }
        scrollIntoView(deleteBtn);
        click(By.id(buttonId));
        By visibleDeleteModal = By.cssSelector(modalSelector + ".show");
        WaitUtils.waitForElement(visibleDeleteModal, WaitType.VISIBLE);
        click(By.cssSelector(modalSelector + " button[type='submit'].btn-danger"));
        waitForAjaxComplete();
    }

    protected void reloadDataGrid(String gridSelector) {
        ((JavascriptExecutor) driver).executeScript(
                "if (typeof updateTable === 'function') { updateTable('" + gridSelector + "'); }");
        WaitUtils.waitForElement(By.cssSelector(gridSelector), WaitType.VISIBLE);
    }

    protected String extractIdFromCurrentUrl() {
        String currentUrl = getCurrentUrl();
        int lastSlash = currentUrl.lastIndexOf('/');
        if (lastSlash < 0) {
            throw new IllegalStateException("Unable to extract id from URL: " + currentUrl);
        }
        return currentUrl.substring(lastSlash + 1);
    }

    protected void runInNewWindow(Runnable windowAction) {
        String originalWindow = driver.getWindowHandle();
        WaitUtils.waitForNumberOfWindowsToBe(2);
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        windowAction.run();
        closePopupAndReturnTo(originalWindow);
    }

    private void closePopupAndReturnTo(String originalWindow) {
        for (String windowHandle : new java.util.HashSet<>(driver.getWindowHandles())) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                try {
                    driver.close();
                } catch (org.openqa.selenium.NoSuchWindowException exception) {
                    logger.debug("Popup window was already closed");
                }
            }
        }

        java.util.Set<String> remainingHandles = driver.getWindowHandles();
        if (remainingHandles.contains(originalWindow)) {
            driver.switchTo().window(originalWindow);
        } else if (!remainingHandles.isEmpty()) {
            driver.switchTo().window(remainingHandles.iterator().next());
        }
    }
}
