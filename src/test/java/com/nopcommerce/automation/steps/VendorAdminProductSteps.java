package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.pages.admin.AdminProductEditPage;
import com.nopcommerce.automation.pages.admin.AdminProductListPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class VendorAdminProductSteps {

    private static final String PRODUCT_NAME_KEY = "vendorAdminProductName";
    private static final String PRODUCT_SKU_KEY = "vendorAdminProductSku";
    private static final String PRODUCT_PRICE_KEY = "vendorAdminProductPrice";

    private final AdminProductListPage adminProductListPage = new AdminProductListPage();
    private final AdminProductEditPage adminProductEditPage = new AdminProductEditPage();

    @When("vendor admin creates a published simple product with name {string} sku prefix {string} and price {string}")
    public void vendorAdminCreatesAPublishedSimpleProduct(String productName, String skuPrefix, String price) {
        String sku = buildUniqueSku(skuPrefix);
        String uniqueProductName = productName + " " + sku;
        storeProductContext(uniqueProductName, sku, price);
        createProductInAdmin(uniqueProductName, sku, price);
    }

    @Given("a vendor admin product exists with name {string} sku prefix {string} and price {string}")
    public void aVendorAdminProductExists(String productName, String skuPrefix, String price) {
        String sku = buildUniqueSku(skuPrefix);
        String uniqueProductName = productName + " " + sku;
        storeProductContext(uniqueProductName, sku, price);
        createProductInAdmin(uniqueProductName, sku, price);
    }

    @When("vendor admin updates the product price to {string}")
    public void vendorAdminUpdatesTheProductPriceTo(String updatedPrice) {
        ScenarioContext.set(PRODUCT_PRICE_KEY, updatedPrice);
        adminProductListPage.openProductEditFromList(getProductName());
        adminProductEditPage.waitForEditPageLoad();
        adminProductEditPage.updatePrice(updatedPrice);
        adminProductEditPage.clickSave();
        adminProductEditPage.waitForProductListRedirect();
    }

    @When("vendor admin opens the product edit page from the list")
    public void vendorAdminOpensTheProductEditPageFromTheList() {
        adminProductListPage.openProductEditFromList(getProductName());
    }

    @Then("vendor admin product appears in product list")
    public void vendorAdminProductAppearsInProductList() {
        AssertionUtils.assertTrue(
                adminProductListPage.isProductListed(getProductName()),
                "Vendor admin product should appear in the scoped product list");
    }

    @Then("vendor admin product price is updated in admin to {string}")
    public void vendorAdminProductPriceIsUpdatedInAdminTo(String expectedPrice) {
        adminProductListPage.openProductEditFromList(getProductName());
        adminProductEditPage.waitForEditPageLoad();
        AssertionUtils.assertTrue(
                adminProductEditPage.getPriceValue().contains(expectedPrice),
                "Vendor admin product price should be updated to " + expectedPrice);
    }

    @Then("vendor admin product edit page is displayed")
    public void vendorAdminProductEditPageIsDisplayed() {
        adminProductEditPage.waitForEditPageLoad();
        AssertionUtils.assertTrue(
                adminProductEditPage.getPriceValue() != null,
                "Vendor admin product edit page should display product details");
    }

    private void createProductInAdmin(String productName, String sku, String price) {
        adminProductListPage.openProductList();
        adminProductListPage.clickAddNewProduct();
        adminProductEditPage.fillProductDetails(productName, sku, price, true);
        adminProductEditPage.configureSimpleProductInventory(100);
        adminProductEditPage.clickSave();
        adminProductEditPage.waitForProductListRedirect();
    }

    private void storeProductContext(String productName, String sku, String price) {
        ScenarioContext.set(PRODUCT_NAME_KEY, productName);
        ScenarioContext.set(PRODUCT_SKU_KEY, sku);
        ScenarioContext.set(PRODUCT_PRICE_KEY, price);
    }

    private String getProductName() {
        return ScenarioContext.get(PRODUCT_NAME_KEY);
    }

    private String buildUniqueSku(String skuPrefix) {
        return skuPrefix + (System.currentTimeMillis() % 100000);
    }
}
