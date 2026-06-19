package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminDiscountEditPage extends BasePage {

    private final By discountNameInput = By.id("Name");
    private final By usePercentageCheckbox = By.id("UsePercentage");
    private final By discountPercentageInput = By.id("DiscountPercentage");
    private final By discountAmountInput = By.id("DiscountAmount");
    private final By requiresCouponCodeCheckbox = By.id("RequiresCouponCode");
    private final By couponCodeInput = By.id("CouponCode");
    private final By isActiveCheckbox = By.id("IsActive");
    private final By startDateInput = By.id("StartDateUtc");
    private final By endDateInput = By.id("EndDateUtc");
    private final By saveButton = By.cssSelector("#discount-form button[name='save']");

    public void fillPercentageDiscount(String name, String couponCode, String percentage) {
        expandAdminCardByName("discount-info");
        type(discountNameInput, name);
        setCheckbox(usePercentageCheckbox, true);
        type(discountPercentageInput, percentage);
        setCheckbox(requiresCouponCodeCheckbox, true);
        type(couponCodeInput, couponCode);
        setCheckbox(isActiveCheckbox, true);
    }

    public void fillFixedAmountDiscount(String name, String couponCode, String amount) {
        expandAdminCardByName("discount-info");
        type(discountNameInput, name);
        setCheckbox(usePercentageCheckbox, false);
        type(discountAmountInput, amount);
        setCheckbox(requiresCouponCodeCheckbox, true);
        type(couponCodeInput, couponCode);
        setCheckbox(isActiveCheckbox, true);
    }

    public void setFutureStartDate(String futureDate) {
        expandAdminCardByName("discount-info");
        enableAdvancedSettingsIfPresent();
        type(startDateInput, futureDate);
    }

    public void clickSave() {
        click(saveButton);
    }

    public void waitForDiscountListRedirect() {
        WaitUtils.waitForUrlContains(Constants.ADMIN_DISCOUNT_LIST_PATH);
    }

    public void waitForEditPageLoad() {
        WaitUtils.waitForUrlContains("/discount/edit");
        WaitUtils.waitForElement(discountNameInput, WaitType.VISIBLE);
    }

    public void deleteDiscount() {
        waitForEditPageLoad();
        clickDeleteConfirmation("discount-delete");
        waitForDiscountListRedirect();
    }
}
