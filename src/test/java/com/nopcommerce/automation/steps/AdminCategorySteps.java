package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.pages.admin.AdminCategoryEditPage;
import com.nopcommerce.automation.pages.admin.AdminCategoryListPage;
import com.nopcommerce.automation.pages.storefront.StorefrontCategoryPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminCategorySteps {

    private static final String CATEGORY_NAME_KEY = "categoryName";
    private static final String PARENT_CATEGORY_NAME_KEY = "parentCategoryName";
    private static final String CATEGORY_SEO_KEY = "categorySeoName";

    private final AdminCategoryListPage categoryListPage = new AdminCategoryListPage();
    private final AdminCategoryEditPage categoryEditPage = new AdminCategoryEditPage();
    private final StorefrontCategoryPage storefrontCategoryPage = new StorefrontCategoryPage();

    @Given("a published root category exists with name prefix {string}")
    public void aPublishedRootCategoryExists(String namePrefix) {
        createCategory(namePrefix, null, true);
    }

    @Given("an empty published category exists with name prefix {string}")
    public void anEmptyPublishedCategoryExists(String namePrefix) {
        createCategory(namePrefix, null, true);
    }

    @When("admin creates a published root category with name prefix {string}")
    public void adminCreatesPublishedRootCategory(String namePrefix) {
        createCategory(namePrefix, null, true);
    }

    @When("admin creates a subcategory with name prefix {string} under the parent category")
    public void adminCreatesSubcategoryUnderParent(String namePrefix) {
        String parentName = ScenarioContext.get(PARENT_CATEGORY_NAME_KEY);
        createCategory(namePrefix, parentName, true);
    }

    @When("admin assigns the product to the category")
    public void adminAssignsProductToCategory() {
        categoryListPage.openCategoryEditFromList(ScenarioContext.get(CATEGORY_NAME_KEY));
        categoryEditPage.addProductToCategory(ScenarioContext.get("productName"));
    }

    @When("admin updates the category SEO name to {string}")
    public void adminUpdatesCategorySeoName(String seoName) {
        String uniqueSeoName = "e2e-cat-seo-" + System.currentTimeMillis();
        ScenarioContext.set(CATEGORY_SEO_KEY, uniqueSeoName);
        categoryListPage.openCategoryEditFromList(ScenarioContext.get(CATEGORY_NAME_KEY));
        categoryEditPage.setSeoName(uniqueSeoName);
        categoryEditPage.clickSave();
        categoryEditPage.waitForCategoryListRedirect();
        categoryListPage.openCategoryEditFromList(ScenarioContext.get(CATEGORY_NAME_KEY));
        String savedSeoName = categoryEditPage.getSeoNameValue();
        AssertionUtils.assertTrue(
                uniqueSeoName.equalsIgnoreCase(savedSeoName),
                "Category SEO name should be saved before storefront verification");
    }

    @When("admin deletes the category")
    public void adminDeletesCategory() {
        categoryListPage.openCategoryEditFromList(ScenarioContext.get(CATEGORY_NAME_KEY));
        categoryEditPage.deleteCategory();
    }

    @When("admin unpublishes the category")
    public void adminUnpublishesCategory() {
        categoryListPage.openCategoryEditFromList(ScenarioContext.get(CATEGORY_NAME_KEY));
        categoryEditPage.setPublished(false);
        categoryEditPage.clickSave();
        categoryEditPage.waitForCategoryListRedirect();
    }

    @Then("category appears in admin category list")
    public void categoryAppearsInAdminCategoryList() {
        AssertionUtils.assertTrue(
                categoryListPage.isCategoryListed(ScenarioContext.get(CATEGORY_NAME_KEY)),
                "Category should appear in admin category list");
    }

    @Then("subcategory appears nested under parent in admin category list")
    public void subcategoryAppearsNestedUnderParent() {
        AssertionUtils.assertTrue(
                categoryListPage.isCategoryNestedUnderParent(
                        ScenarioContext.get(CATEGORY_NAME_KEY),
                        ScenarioContext.get(PARENT_CATEGORY_NAME_KEY)),
                "Subcategory should appear nested under parent in admin category list");
    }

    @Then("product is mapped to the category in admin")
    public void productIsMappedToCategoryInAdmin() {
        categoryListPage.openCategoryEditFromList(ScenarioContext.get(CATEGORY_NAME_KEY));
        AssertionUtils.assertTrue(
                categoryEditPage.isProductMappedToCategory(ScenarioContext.get("productName")),
                "Product should be mapped to category in admin");
    }

    @Then("category is accessible on storefront at SEO slug {string}")
    public void categoryIsAccessibleOnStorefront(String seoSlug) {
        String resolvedSeoSlug = ScenarioContext.get(CATEGORY_SEO_KEY);
        if (resolvedSeoSlug == null || resolvedSeoSlug.isBlank()) {
            resolvedSeoSlug = seoSlug;
        }
        storefrontCategoryPage.openCategoryBySeoName(resolvedSeoSlug);
        AssertionUtils.assertFalse(
                storefrontCategoryPage.isCategoryNotFound(),
                "Category storefront URL should resolve for SEO slug: " + resolvedSeoSlug);
        AssertionUtils.assertTrue(
                storefrontCategoryPage.isCategoryPageDisplayed(ScenarioContext.get(CATEGORY_NAME_KEY)),
                "Category should be accessible on storefront via SEO slug");
    }

    @Then("category is not listed in admin category list")
    public void categoryIsNotListedInAdminCategoryList() {
        AssertionUtils.assertFalse(
                categoryListPage.isCategoryListed(ScenarioContext.get(CATEGORY_NAME_KEY)),
                "Category should be removed from admin category list");
    }

    @Then("category is not visible in storefront navigation")
    public void categoryIsNotVisibleInStorefrontNavigation() {
        storefrontCategoryPage.openHomePage();
        AssertionUtils.assertFalse(
                storefrontCategoryPage.isCategoryVisibleInNavigation(ScenarioContext.get(CATEGORY_NAME_KEY)),
                "Unpublished category should not appear in storefront navigation");
    }

    private void createCategory(String namePrefix, String parentName, boolean published) {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis() % 100000);
        String categoryName = namePrefix + " " + uniqueSuffix;
        ScenarioContext.set(CATEGORY_NAME_KEY, categoryName);
        if (parentName == null) {
            ScenarioContext.set(PARENT_CATEGORY_NAME_KEY, categoryName);
        }

        categoryListPage.openCategoryList();
        categoryListPage.clickAddNewCategory();
        categoryEditPage.fillCategoryDetails(categoryName, published);
        if (parentName != null) {
            categoryEditPage.setParentCategory(parentName);
        }
        categoryEditPage.clickSave();
        categoryEditPage.waitForCategoryListRedirect();
    }
}
