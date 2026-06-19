package com.nopcommerce.automation.pages.storefront;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class StorefrontWishlistPage extends BasePage {

    private final By wishlistPageMarker = By.cssSelector(".html-wishlist-page");
    private final By wishlistTable = By.cssSelector(".wishlist-page table.cart");
    private final By addToCartFromWishlistButton = By.cssSelector(".wishlist-add-to-cart-button");
    private final By emptyWishlistMessage = By.cssSelector(".wishlist-page .no-data");

    public void openWishlist() {
        navigateTo(Constants.STORE_WISHLIST_PATH);
        WaitUtils.waitForElement(wishlistPageMarker, WaitType.VISIBLE);
    }

    public boolean isWishlistPageDisplayed() {
        return isDisplayed(wishlistPageMarker);
    }

    public boolean isProductInWishlist(String productName) {
        By productNameCell = By.xpath("//div[contains(@class,'wishlist-page')]//a[contains(@class,'product-name')"
                + " and contains(normalize-space(.), '" + productName + "')]");
        return isDisplayed(productNameCell);
    }

    public boolean isWishlistItemPriceDisplayed(String productName) {
        By priceCell = By.xpath("//div[contains(@class,'wishlist-page')]//a[contains(@class,'product-name')"
                + " and contains(normalize-space(.), '" + productName + "')]/ancestor::tr//span[contains(@class,'product-unit-price')]");
        return isDisplayed(priceCell);
    }

    public void moveFirstWishlistItemToCart() {
        By firstAddToCartCheckbox = By.cssSelector(".wishlist-page input[name='addtocart']");
        setCheckbox(firstAddToCartCheckbox, true);
        click(addToCartFromWishlistButton);
        waitForAjaxComplete();
    }

    public boolean isWishlistEmpty() {
        return isDisplayed(emptyWishlistMessage);
    }
}
