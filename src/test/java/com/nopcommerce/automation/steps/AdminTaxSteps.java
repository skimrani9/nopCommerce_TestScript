package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.admin.AdminTaxCategoriesPage;
import com.nopcommerce.automation.pages.admin.AdminTaxProvidersPage;
import com.nopcommerce.automation.pages.admin.AdminTaxSettingsPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminTaxSteps {

    private final AdminTaxSettingsPage taxSettingsPage = new AdminTaxSettingsPage();
    private final AdminTaxCategoriesPage taxCategoriesPage = new AdminTaxCategoriesPage();
    private final AdminTaxProvidersPage taxProvidersPage = new AdminTaxProvidersPage();

    @When("admin opens tax settings page")
    public void adminOpensTaxSettingsPage() {
        taxSettingsPage.openTaxSettings();
    }

    @When("admin opens tax categories page")
    public void adminOpensTaxCategoriesPage() {
        taxCategoriesPage.openTaxCategories();
    }

    @When("admin creates tax category {string}")
    public void adminCreatesTaxCategory(String categoryName) {
        taxCategoriesPage.createTaxCategory(categoryName);
    }

    @When("admin opens tax providers page")
    public void adminOpensTaxProvidersPage() {
        taxProvidersPage.openTaxProviders();
    }

    @Then("tax settings page is displayed")
    public void taxSettingsPageIsDisplayed() {
        AssertionUtils.assertTrue(
                taxSettingsPage.isTaxSettingsPageDisplayed(),
                "Tax settings page should be displayed");
    }

    @Then("tax category {string} appears in admin tax categories list")
    public void taxCategoryAppearsInList(String categoryName) {
        AssertionUtils.assertTrue(
                taxCategoriesPage.isTaxCategoryListed(categoryName),
                "Tax category should appear in admin tax categories list");
    }

    @Then("tax categories grid is displayed")
    public void taxCategoriesGridIsDisplayed() {
        AssertionUtils.assertTrue(
                taxCategoriesPage.isTaxCategoriesGridDisplayed(),
                "Tax categories grid should be displayed");
    }

    @Then("tax common settings card is available")
    public void taxCommonSettingsCardIsAvailable() {
        AssertionUtils.assertTrue(
                taxSettingsPage.isTaxSettingsPageDisplayed(),
                "Tax common settings card should be available");
    }

    @Then("tax providers grid is displayed")
    public void taxProvidersGridIsDisplayed() {
        AssertionUtils.assertTrue(
                taxProvidersPage.isTaxProvidersGridDisplayed(),
                "Tax providers grid should be displayed");
    }
}
