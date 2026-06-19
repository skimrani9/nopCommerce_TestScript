package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.config.ScenarioContext;
import com.nopcommerce.automation.pages.admin.AdminDiscountEditPage;
import com.nopcommerce.automation.pages.admin.AdminDiscountListPage;
import com.nopcommerce.automation.pages.storefront.StorefrontCartPage;
import com.nopcommerce.automation.pages.storefront.StorefrontProductPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdminDiscountSteps {

    private static final String DISCOUNT_NAME_KEY = "discountName";
    private static final String COUPON_CODE_KEY = "couponCode";

    private final AdminDiscountListPage discountListPage = new AdminDiscountListPage();
    private final AdminDiscountEditPage discountEditPage = new AdminDiscountEditPage();
    private final StorefrontProductPage storefrontProductPage = new StorefrontProductPage();
    private final StorefrontCartPage storefrontCartPage = new StorefrontCartPage();

    @When("admin creates an active percentage discount with name prefix {string} coupon {string} and percentage {string}")
    public void adminCreatesPercentageDiscount(String namePrefix, String couponCode, String percentage) {
        createPercentageDiscount(namePrefix, couponCode, percentage, null);
    }

    @Given("an active discount exists with name prefix {string} coupon {string} and percentage {string}")
    public void anActiveDiscountExists(String namePrefix, String couponCode, String percentage) {
        createPercentageDiscount(namePrefix, couponCode, percentage, null);
    }

    @Given("a future dated discount exists with name prefix {string} coupon {string} and percentage {string}")
    public void aFutureDatedDiscountExists(String namePrefix, String couponCode, String percentage) {
        createPercentageDiscount(namePrefix, couponCode, percentage, "12/31/2099 12:00:00 AM");
    }

    @Given("an active fixed discount exists with name prefix {string} coupon {string} and amount {string}")
    public void anActiveFixedDiscountExists(String namePrefix, String couponCode, String amount) {
        String discountName = namePrefix + " " + System.currentTimeMillis() % 100000;
        ScenarioContext.set(DISCOUNT_NAME_KEY, discountName);
        ScenarioContext.set(COUPON_CODE_KEY, couponCode);

        discountListPage.openDiscountList();
        discountListPage.clickAddNewDiscount();
        discountEditPage.fillFixedAmountDiscount(discountName, couponCode, amount);
        discountEditPage.clickSave();
        discountEditPage.waitForDiscountListRedirect();
    }

    @When("admin deletes the discount")
    public void adminDeletesDiscount() {
        discountListPage.openDiscountEditFromList(ScenarioContext.get(DISCOUNT_NAME_KEY));
        discountEditPage.deleteDiscount();
    }

    @When("customer adds the product to cart and applies coupon {string}")
    public void customerAddsProductAndAppliesCoupon(String couponCode) {
        addProductToCartFromContext();
        storefrontCartPage.applyDiscountCoupon(couponCode);
    }

    @When("customer applies coupon {string} on cart")
    public void customerAppliesCouponOnCart(String couponCode) {
        storefrontCartPage.openCart();
        storefrontCartPage.applyDiscountCoupon(couponCode);
    }

    @Then("discount appears in admin discount list")
    public void discountAppearsInAdminDiscountList() {
        AssertionUtils.assertTrue(
                discountListPage.isDiscountListed(ScenarioContext.get(DISCOUNT_NAME_KEY)),
                "Discount should appear in admin discount list");
    }

    @Then("discount is applied to cart total")
    public void discountIsAppliedToCartTotal() {
        AssertionUtils.assertTrue(
                storefrontCartPage.isDiscountApplied(),
                "Discount should be applied to cart total");
    }

    @Then("coupon is rejected on storefront cart")
    public void couponIsRejectedOnStorefrontCart() {
        AssertionUtils.assertTrue(
                storefrontCartPage.isDiscountRejected(),
                "Future-dated coupon should be rejected on storefront cart");
    }

    @Then("discount is not listed in admin discount list")
    public void discountIsNotListedInAdminDiscountList() {
        AssertionUtils.assertFalse(
                discountListPage.isDiscountListed(ScenarioContext.get(DISCOUNT_NAME_KEY)),
                "Deleted discount should not appear in admin discount list");
    }

    @Then("cart total is reduced by fixed discount amount")
    public void cartTotalIsReducedByFixedDiscountAmount() {
        AssertionUtils.assertTrue(
                storefrontCartPage.isDiscountApplied(),
                "Fixed amount discount should be applied to cart");
    }

    private void createPercentageDiscount(String namePrefix, String couponCode, String percentage, String startDate) {
        String discountName = namePrefix + " " + System.currentTimeMillis() % 100000;
        ScenarioContext.set(DISCOUNT_NAME_KEY, discountName);
        ScenarioContext.set(COUPON_CODE_KEY, couponCode);

        discountListPage.openDiscountList();
        discountListPage.clickAddNewDiscount();
        discountEditPage.fillPercentageDiscount(discountName, couponCode, percentage);
        if (startDate != null) {
            discountEditPage.setFutureStartDate(startDate);
        }
        discountEditPage.clickSave();
        discountEditPage.waitForDiscountListRedirect();
    }

    private void addProductToCartFromContext() {
        String productName = ScenarioContext.get("productName");
        storefrontProductPage.openSearchResults(productName);
        storefrontProductPage.openProductDetailsFromSearch(productName);
        storefrontProductPage.addCurrentProductToCart();
        storefrontCartPage.openCart();
    }
}
