package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.nio.file.Path;

public class AdminProductEditPage extends BasePage {

    private static final String PRODUCT_ATTRIBUTES_CARD_ID = "product-product-attributes";
    private static final String MULTIMEDIA_CARD_ID = "product-multimedia";
    private static final String DELETE_CONFIRM_SUBMIT_SELECTOR = "button[type='submit'].btn-danger";

    private final By productNameInput = By.id("Name");
    private final By productSkuInput = By.id("Sku");
    private final By productPriceInput = By.id("Price");
    private final By publishedCheckbox = By.id("Published");
    private final By saveButton = By.cssSelector("#product-form button[name='save']");
    private final By deleteButton = By.id("product-delete");
    private final By attributeCombinationsTab = By.cssSelector("a[href*='tab-attribute-combinations'], #tab-attribute-combinations-link");
    private final By generateAllCombinationsButton = By.id("btnGenerateAllCombinations");
    private final By generateAllConfirmButton = By.id("btnGenerateAllCombinations-action-confirmation-submit-button");
    private final By combinationsGrid = By.id("attributecombinations-grid");
    private final By picturesGrid = By.id("productpictures-grid");
    private final By addAttributeMappingLink = By.cssSelector("a[href*='ProductAttributeMappingCreate']");
    private final By productAttributeDropdown = By.id("ProductAttributeId");
    private final By attributeMappingSaveContinueButton = By.cssSelector("#productattribute-form button[name='save-continue']");
    private final By addAttributeValueButton = By.id("btnAddNewValue");
    private final By addCombinationButton = By.id("btnAddNewCombination");
    private final By refreshCombinationsButton = By.id("btnRefreshCombinations");

    public void openCreateProductPage() {
        navigateTo(Constants.ADMIN_PRODUCT_CREATE_PATH);
        WaitUtils.waitForElement(productNameInput, WaitType.VISIBLE);
    }

    public void fillProductDetails(String name, String sku, String price, boolean published) {
        type(productNameInput, name);
        type(productSkuInput, sku);
        type(productPriceInput, price);
        setCheckbox(publishedCheckbox, published);
    }

    public void updatePrice(String price) {
        scrollIntoView(productPriceInput);
        type(productPriceInput, price);
    }

    public String getPriceValue() {
        return getInputValue(productPriceInput);
    }

    public void clickSave() {
        click(saveButton);
    }

    public void setProductSeoName(String seoName) {
        enableAdvancedSettingsIfPresent();
        expandAdminCardByName("product-seo");
        type(By.id("SeName"), seoName);
    }

    public void configureSimpleProductInventory(int quantity) {
        enableAdvancedSettingsIfPresent();
        expandAdminCardByName("product-inventory");
        selectDropdownOptionByValue(By.id("ManageInventoryMethodId"), "1");
        type(By.id("StockQuantity"), String.valueOf(quantity));
    }

    public void waitForProductListRedirect() {
        WaitUtils.waitForUrlContains(Constants.ADMIN_PRODUCT_LIST_PATH);
    }

    public void waitForEditPageLoad() {
        WaitUtils.waitForUrlContains("/admin/product/edit");
        WaitUtils.waitForElement(productNameInput, WaitType.VISIBLE);
    }

    public void deleteProduct() {
        waitForEditPageLoad();
        WebElement deleteBtn = WaitUtils.waitForElement(deleteButton, WaitType.VISIBLE);
        String modalSelector = deleteBtn.getAttribute("data-target");
        if (modalSelector == null || modalSelector.isBlank()) {
            throw new IllegalStateException("Delete button is missing data-target for confirmation modal");
        }
        scrollIntoView(deleteBtn);
        click(deleteButton);
        By visibleDeleteModal = By.cssSelector(modalSelector + ".show");
        WaitUtils.waitForElement(visibleDeleteModal, WaitType.VISIBLE);
        click(By.cssSelector(modalSelector + " " + DELETE_CONFIRM_SUBMIT_SELECTOR));
        waitForProductListRedirect();
    }

    public void addColorAttributeMappingWithRedValue() {
        waitForEditPageLoad();
        String productId = extractIdFromCurrentUrl();
        navigateTo("/Admin/Product/ProductAttributeMappingCreate?productId=" + productId);
        selectDropdownOptionByText(productAttributeDropdown, "Color");
        click(attributeMappingSaveContinueButton);
        WaitUtils.waitForUrlContains("ProductAttributeMappingEdit");
        int windowsBefore = driver.getWindowHandles().size();
        click(addAttributeValueButton);
        runInNewWindow(() -> {
            type(By.id("Name"), "Red");
            click(By.cssSelector("button[name='save']"));
        });
        if (driver.getWindowHandles().size() > windowsBefore) {
            WaitUtils.waitForNumberOfWindowsToBe(windowsBefore);
        }
        navigateTo("/Admin/Product/Edit/" + productId);
        waitForEditPageLoad();
    }

    public void createAttributeCombination(String combinationSku, String combinationPrice) {
        expandAdminCard(PRODUCT_ATTRIBUTES_CARD_ID);
        if (isDisplayed(attributeCombinationsTab)) {
            click(attributeCombinationsTab);
        }
        scrollIntoView(addCombinationButton);
        int windowsBefore = driver.getWindowHandles().size();
        click(addCombinationButton);
        runInNewWindow(() -> {
            By attributeDropdown = By.xpath("//select[contains(@name,'product_attribute_')]");
            selectDropdownOptionByText(attributeDropdown, "Red");
            type(By.id("Sku"), combinationSku);
            type(By.id("OverriddenPrice"), combinationPrice);
            click(By.cssSelector("button[name='save']"));
        });
        ((JavascriptExecutor) driver).executeScript(
                "if (typeof updateTable === 'function') { updateTable('#attributecombinations-grid'); }");
        By combinationCell = By.xpath(
                "//table[@id='attributecombinations-grid']//td[contains(normalize-space(.), '" + combinationSku + "')]");
        WaitUtils.waitForElement(combinationCell, WaitType.VISIBLE);
    }

    public boolean isCombinationListedWithSku(String combinationSku) {
        expandAdminCard(PRODUCT_ATTRIBUTES_CARD_ID);
        if (isDisplayed(attributeCombinationsTab)) {
            click(attributeCombinationsTab);
        }
        By combinationCell = By.xpath(
                "//table[@id='attributecombinations-grid']//td[contains(normalize-space(.), '" + combinationSku + "')]");
        try {
            WaitUtils.waitForElement(combinationCell, WaitType.VISIBLE);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public String getCombinationPriceBySku(String combinationSku) {
        expandAdminCard(PRODUCT_ATTRIBUTES_CARD_ID);
        if (isDisplayed(attributeCombinationsTab)) {
            click(attributeCombinationsTab);
        }
        By priceCell = By.xpath(
                "//table[@id='attributecombinations-grid']//tr[.//td[contains(normalize-space(.), '"
                        + combinationSku + "')]]//td[contains(@class,'column-OverriddenPrice') or position()=7]");
        return getText(priceCell);
    }

    public void uploadProductPicture(java.nio.file.Path imagePath) {
        waitForEditPageLoad();
        expandAdminCard(MULTIMEDIA_CARD_ID);
        By fileInput = By.cssSelector(
                "#" + MULTIMEDIA_CARD_ID + " .filepond--browser input[type='file'], "
                        + "#" + MULTIMEDIA_CARD_ID + " input[type='file']");
        WebElement uploadInput = WaitUtils.waitForElement(fileInput, WaitType.PRESENCE);
        scrollIntoView(uploadInput);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.display='block'; arguments[0].style.visibility='visible';",
                uploadInput);
        uploadInput.sendKeys(imagePath.toAbsolutePath().toString());
        WaitUtils.waitForElement(
                By.xpath("//table[@id='productpictures-grid']//img"),
                WaitType.VISIBLE);
    }

    public boolean isProductPictureListedInAdmin() {
        expandAdminCard(MULTIMEDIA_CARD_ID);
        return isDisplayed(By.cssSelector("#productpictures-grid img"));
    }
}
