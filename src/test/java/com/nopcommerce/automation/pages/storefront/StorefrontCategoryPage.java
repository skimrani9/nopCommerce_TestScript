package com.nopcommerce.automation.pages.storefront;

import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class StorefrontCategoryPage extends BasePage {

    public void openHomePage() {
        navigateTo("/");
    }

    public void openCategoryBySeoName(String seoName) {
        navigateTo("/" + seoName);
        waitForAjaxComplete();
        try {
            WaitUtils.waitForElement(
                    By.cssSelector(".html-category-page, .category-page .page-title h1"),
                    WaitType.VISIBLE);
        } catch (Exception exception) {
            logger.debug("Category page marker not visible yet for slug {}", seoName);
        }
    }

    public boolean isCategoryPageDisplayed(String categoryName) {
        By categoryHeading = By.xpath(
                "//div[contains(@class,'page-title')]//h1[contains(normalize-space(.), '" + categoryName + "')]"
                        + " | //div[contains(@class,'category-page')]//h1[contains(normalize-space(.), '"
                        + categoryName + "')]"
                        + " | //h1[contains(normalize-space(.), '" + categoryName + "')]");
        try {
            WaitUtils.waitForElement(categoryHeading, WaitType.VISIBLE);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean isCategoryNotFound() {
        return getCurrentUrl().contains("page-not-found")
                || driver.getTitle().toLowerCase().contains("not found")
                || isDisplayed(By.xpath("//*[contains(text(),'Page not found') or contains(text(),'404')]"));
    }

    public boolean isProductVisibleInCategory(String productName) {
        By productLink = By.xpath(
                "//article[contains(@class,'product-item')]//a[contains(normalize-space(.), '" + productName + "')]");
        return isDisplayed(productLink);
    }

    public boolean hasProductGrid() {
        return isDisplayed(By.cssSelector(".product-grid .item-box, .product-list .item-box"));
    }

    public boolean isCategoryVisibleInNavigation(String categoryName) {
        By navLink = By.xpath(
                "//ul[contains(@class,'top-menu')]//a[contains(normalize-space(.), '" + categoryName + "')]");
        try {
            WaitUtils.waitForElement(navLink, WaitType.VISIBLE);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
