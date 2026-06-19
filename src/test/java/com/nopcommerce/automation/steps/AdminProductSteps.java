package com.nopcommerce.automation.steps;



import com.nopcommerce.automation.config.ScenarioContext;

import com.nopcommerce.automation.pages.admin.AdminCommonPage;

import com.nopcommerce.automation.pages.admin.AdminProductEditPage;

import com.nopcommerce.automation.pages.admin.AdminProductListPage;

import com.nopcommerce.automation.pages.storefront.StorefrontProductPage;

import com.nopcommerce.automation.utils.AssertionUtils;

import io.cucumber.java.en.Given;

import io.cucumber.java.en.Then;

import io.cucumber.java.en.When;



import java.net.URISyntaxException;

import java.net.URL;

import java.nio.file.Path;

import java.nio.file.Paths;



public class AdminProductSteps {



    private static final String PRODUCT_NAME_KEY = "productName";

    private static final String PRODUCT_SKU_KEY = "productSku";

    private static final String PRODUCT_PRICE_KEY = "productPrice";

    private static final String SECOND_PRODUCT_NAME_KEY = "secondProductName";

    private static final String SECOND_PRODUCT_SKU_KEY = "secondProductSku";

    private static final String COMBINATION_SKU_KEY = "combinationSku";

    private static final String COMBINATION_PRICE_KEY = "combinationPrice";



    private final AdminProductListPage adminProductListPage = new AdminProductListPage();

    private final AdminProductEditPage adminProductEditPage = new AdminProductEditPage();

    private final AdminCommonPage adminCommonPage = new AdminCommonPage();

    private final StorefrontProductPage storefrontProductPage = new StorefrontProductPage();



    @Given("admin is on the product list page")

    public void adminIsOnTheProductListPage() {

        adminProductListPage.openProductList();

    }



    @Given("a published product exists with name {string} sku prefix {string} and price {string}")

    public void aPublishedProductExists(String productName, String skuPrefix, String price) {

        String sku = buildUniqueSku(skuPrefix);

        String uniqueProductName = productName + " " + sku;

        storeProductContext(uniqueProductName, sku, price);

        createProductInAdmin(uniqueProductName, sku, price);

    }



    @Given("two deletable products exist with sku prefixes {string} and {string}")

    public void twoDeletableProductsExist(String firstSkuPrefix, String secondSkuPrefix) {

        String firstSku = buildUniqueSku(firstSkuPrefix);

        String secondSku = buildUniqueSku(secondSkuPrefix);

        String firstName = "E2E Bulk Product A " + firstSku;

        String secondName = "E2E Bulk Product B " + secondSku;



        ScenarioContext.set(PRODUCT_NAME_KEY, firstName);

        ScenarioContext.set(PRODUCT_SKU_KEY, firstSku);

        ScenarioContext.set(SECOND_PRODUCT_NAME_KEY, secondName);

        ScenarioContext.set(SECOND_PRODUCT_SKU_KEY, secondSku);



        createProductInAdmin(firstName, firstSku, "100.00");

        createProductInAdmin(secondName, secondSku, "150.00");

    }



    @When("admin creates a published simple product with name {string} sku prefix {string} and price {string}")

    public void adminCreatesPublishedSimpleProduct(String productName, String skuPrefix, String price) {

        String sku = buildUniqueSku(skuPrefix);

        storeProductContext(productName + " " + sku, sku, price);

        adminProductListPage.openProductList();

        adminProductListPage.clickAddNewProduct();

        fillAndSaveProduct(

                ScenarioContext.get(PRODUCT_NAME_KEY),

                ScenarioContext.get(PRODUCT_SKU_KEY),

                ScenarioContext.get(PRODUCT_PRICE_KEY));

    }



    @When("admin updates the product price to {string}")

    public void adminUpdatesTheProductPrice(String updatedPrice) {

        ScenarioContext.set(PRODUCT_PRICE_KEY, updatedPrice);

        adminProductListPage.openProductList();

        adminProductListPage.openProductEditFromList(ScenarioContext.get(PRODUCT_NAME_KEY));

        adminProductEditPage.updatePrice(updatedPrice);

        adminProductEditPage.clickSave();

        adminProductEditPage.waitForProductListRedirect();

    }



    @When("admin deletes the product from the edit page")

    public void adminDeletesTheProductFromTheEditPage() {

        adminProductListPage.openProductEditBySku(ScenarioContext.get(PRODUCT_SKU_KEY));

        adminProductEditPage.deleteProduct();

    }



    @When("admin adds attribute mapping and combination for the product")

    public void adminAddsAttributeMappingAndCombinationForTheProduct() {

        String baseSku = ScenarioContext.get(PRODUCT_SKU_KEY);

        String combinationSku = baseSku + "-RED";

        String combinationPrice = "1099.99";

        ScenarioContext.set(COMBINATION_SKU_KEY, combinationSku);

        ScenarioContext.set(COMBINATION_PRICE_KEY, combinationPrice);



        new com.nopcommerce.automation.pages.admin.AdminProductAttributePage().ensureProductAttributeExists("Color");

        adminProductListPage.openProductEditBySku(baseSku);

        adminProductEditPage.addColorAttributeMappingWithRedValue();

        adminProductEditPage.createAttributeCombination(combinationSku, combinationPrice);

    }



    @When("admin uploads a product picture on the multimedia tab")

    public void adminUploadsAProductPictureOnTheMultimediaTab() {

        adminProductListPage.openProductEditBySku(ScenarioContext.get(PRODUCT_SKU_KEY));

        adminProductEditPage.uploadProductPicture(getTestProductImagePath());

    }



    @When("admin deletes the selected products from the product list")

    public void adminDeletesTheSelectedProductsFromTheProductList() {

        adminProductListPage.openProductList();

        adminProductListPage.searchBySkuPrefix("E2E-BULK-");

        adminProductListPage.selectProductInGridBySku(ScenarioContext.get(PRODUCT_SKU_KEY));

        adminProductListPage.selectProductInGridBySku(ScenarioContext.get(SECOND_PRODUCT_SKU_KEY));

        adminProductListPage.deleteSelectedProducts();

    }



    @Then("product price is updated in admin to {string}")

    public void productPriceIsUpdatedInAdminTo(String expectedPrice) {

        adminProductListPage.openProductList();

        adminProductListPage.openProductEditFromList(ScenarioContext.get(PRODUCT_NAME_KEY));

        AssertionUtils.assertContains(

                adminProductEditPage.getPriceValue(),

                expectedPrice,

                "Updated price should be saved in admin product edit page");

    }



    @Then("product combination appears with distinct sku and price")

    public void productCombinationAppearsWithDistinctSkuAndPrice() {

        adminProductListPage.openProductEditBySku(ScenarioContext.get(PRODUCT_SKU_KEY));

        String combinationSku = ScenarioContext.get(COMBINATION_SKU_KEY);

        String combinationPrice = ScenarioContext.get(COMBINATION_PRICE_KEY);

        AssertionUtils.assertTrue(

                adminProductEditPage.isCombinationListedWithSku(combinationSku),

                "Product attribute combination should appear with distinct SKU");

        AssertionUtils.assertContains(

                adminProductEditPage.getCombinationPriceBySku(combinationSku),

                combinationPrice,

                "Combination price should be saved in admin");

    }



    @Then("product picture appears in admin gallery and on storefront")

    public void productPictureAppearsInAdminGalleryAndOnStorefront() {

        adminProductListPage.openProductEditBySku(ScenarioContext.get(PRODUCT_SKU_KEY));

        AssertionUtils.assertTrue(

                adminProductEditPage.isProductPictureListedInAdmin(),

                "Uploaded product picture should appear in admin gallery");



        storefrontProductPage.openSearchResults(ScenarioContext.get(PRODUCT_NAME_KEY));

        storefrontProductPage.openProductDetailsFromSearch(ScenarioContext.get(PRODUCT_NAME_KEY));

        AssertionUtils.assertTrue(

                storefrontProductPage.isProductPictureDisplayed(),

                "Uploaded product picture should appear on storefront product page");

    }



    @Then("product appears in admin product list")

    public void productAppearsInAdminProductList() {

        adminProductListPage.openProductList();

        adminProductListPage.searchByProductName(ScenarioContext.get(PRODUCT_NAME_KEY));

        AssertionUtils.assertTrue(

                adminProductListPage.isProductListed(ScenarioContext.get(PRODUCT_NAME_KEY)),

                "Product should appear in admin product list");

    }



    @Then("product is visible on storefront with price {string}")

    public void productIsVisibleOnStorefrontWithPrice(String expectedPrice) {

        verifyStorefrontProduct(ScenarioContext.get(PRODUCT_NAME_KEY), expectedPrice);

    }



    @Then("product price on storefront is {string}")

    public void productPriceOnStorefrontIs(String expectedPrice) {

        verifyStorefrontProduct(ScenarioContext.get(PRODUCT_NAME_KEY), expectedPrice);

    }



    @Then("product is not listed in admin product list")

    public void productIsNotListedInAdminProductList() {

        AssertionUtils.assertFalse(

                adminProductListPage.isProductListedBySku(ScenarioContext.get(PRODUCT_SKU_KEY)),

                "Product should be removed from admin product list");

    }



    @Then("product is not visible on storefront")

    public void productIsNotVisibleOnStorefront() {

        storefrontProductPage.openSearchResults(ScenarioContext.get(PRODUCT_NAME_KEY));

        AssertionUtils.assertFalse(

                storefrontProductPage.isProductVisibleInSearchResults(ScenarioContext.get(PRODUCT_NAME_KEY)),

                "Deleted product should not appear on storefront search results");

    }



    @Then("selected products are removed from admin product list")

    public void selectedProductsAreRemovedFromAdminProductList() {

        AssertionUtils.assertFalse(

                adminProductListPage.isProductListedBySku(ScenarioContext.get(PRODUCT_SKU_KEY)),

                "First bulk product should be deleted");

        AssertionUtils.assertFalse(

                adminProductListPage.isProductListedBySku(ScenarioContext.get(SECOND_PRODUCT_SKU_KEY)),

                "Second bulk product should be deleted");

    }



    private void createProductInAdmin(String productName, String sku, String price) {

        adminProductListPage.openProductList();

        adminProductListPage.clickAddNewProduct();

        fillAndSaveProduct(productName, sku, price);

    }



    private void fillAndSaveProduct(String productName, String sku, String price) {

        adminProductEditPage.fillProductDetails(productName, sku, price, true);

        adminProductEditPage.clickSave();

        adminProductEditPage.waitForProductListRedirect();

    }



    private void storeProductContext(String productName, String sku, String price) {

        ScenarioContext.set(PRODUCT_NAME_KEY, productName);

        ScenarioContext.set(PRODUCT_SKU_KEY, sku);

        ScenarioContext.set(PRODUCT_PRICE_KEY, price);

    }



    private String buildUniqueSku(String skuPrefix) {

        return skuPrefix + (System.currentTimeMillis() % 100000);

    }



    private Path getTestProductImagePath() {

        URL resource = getClass().getClassLoader().getResource("test-data/product-test.jpg");

        if (resource == null) {

            throw new IllegalStateException("Test product image not found at test-data/product-test.jpg");

        }

        try {

            return Paths.get(resource.toURI());

        } catch (URISyntaxException exception) {

            throw new IllegalStateException("Unable to resolve test product image path", exception);

        }

    }



    private void verifyStorefrontProduct(String productName, String expectedPrice) {

        storefrontProductPage.openSearchResults(productName);

        AssertionUtils.assertTrue(

                storefrontProductPage.isProductVisibleInSearchResults(productName),

                "Product should be visible in storefront search results");

        storefrontProductPage.openProductDetailsFromSearch(productName);

        storefrontProductPage.refreshPage();

        AssertionUtils.assertTrue(

                storefrontProductPage.isProductDetailsPageDisplayed(productName),

                "Product details page should be displayed");

        AssertionUtils.assertContains(

                storefrontProductPage.getDisplayedPrice(),

                expectedPrice,

                "Storefront product price should match expected value");

    }

}

