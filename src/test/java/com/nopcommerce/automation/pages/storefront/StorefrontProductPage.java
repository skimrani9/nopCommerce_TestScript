package com.nopcommerce.automation.pages.storefront;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.config.ConfigReader;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class StorefrontProductPage extends BasePage {

    private final By searchBoxInput = By.cssSelector("#small-search-box-form input.search-box-text");
    private final By searchBoxButton = By.cssSelector("#small-search-box-form button.search-box-button");
    private final By searchResults = By.cssSelector(".search-results .product-grid, .search-results .product-list");
    private final By noSearchResults = By.cssSelector(".search-results .no-result");

    public void searchFromHeader(String query) {
        type(searchBoxInput, query);
        click(searchBoxButton);
        waitForSearchResultsLoaded();
    }

    public void waitForSearchResultsLoaded() {
        By resultsArea = By.cssSelector(".search-results");
        WaitUtils.waitForElement(resultsArea, WaitType.VISIBLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
        wait.until(webDriver -> {
            if (isDisplayedQuick(noSearchResults)) {
                return true;
            }
            return isDisplayedQuick(By.cssSelector(".search-results .item-box"));
        });
        waitForAjaxComplete();
    }

    public boolean hasSearchResults() {
        waitForSearchResultsLoaded();
        return isDisplayed(By.cssSelector(".search-results .product-item, .search-results .item-box"));
    }

    public boolean hasNoSearchResults() {
        waitForSearchResultsLoaded();
        return isDisplayed(noSearchResults);
    }

    public void openProductBySeoName(String seoName) {
        navigateTo("/" + seoName);
        WaitUtils.waitForElement(By.cssSelector(".product-name h1, .product-name, .product-essential"), WaitType.VISIBLE);
    }

    public void openSearchResults(String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        navigateTo(Constants.STORE_SEARCH_PATH + "?q=" + encodedQuery);
    }

    public boolean isProductVisibleInSearchResults(String productName) {
        try {
            By productTitle = By.xpath(
                    "//article[contains(@class,'product-item')]//h2[contains(@class,'product-title')]"
                            + "//a[contains(normalize-space(.), '" + productName + "')]");
            WaitUtils.waitForElement(productTitle, WaitType.VISIBLE);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public void openProductDetailsFromSearch(String productName) {
        By productLink = By.xpath(
                "//article[contains(@class,'product-item')]//h2[contains(@class,'product-title')]"
                        + "//a[contains(normalize-space(.), '" + productName + "')]");
        click(productLink);
        WaitUtils.waitForElement(By.cssSelector(".product-name h1, .product-name"), WaitType.VISIBLE);
    }

    public String getDisplayedPrice() {
        By priceLocator = By.cssSelector("span[class*='price-value'], .product-price .actual-price, .prices .actual-price");
        return getText(priceLocator);
    }

    public void refreshPage() {
        driver.navigate().refresh();
        WaitUtils.waitForElement(By.cssSelector(".product-name h1, .product-name"), WaitType.VISIBLE);
    }

    public boolean isProductDetailsPageDisplayed(String productName) {
        By productHeading = By.xpath(
                "//div[contains(@class,'product-name')]//*[contains(normalize-space(.), '" + productName + "')]");
        return isDisplayed(productHeading);
    }

    public boolean isProductPictureDisplayed() {
        By pictureLocator = By.cssSelector(
                ".picture-gallery img, .product-essential .picture img, .product-item-box .picture img, .gallery .picture img");
        return isDisplayed(pictureLocator);
    }

    public void addCurrentProductToCart() {
        clickAddToCartOnProductDetailsPage();
        waitForAddToCartSuccess();
    }

    public void addCurrentProductToWishlist() {
        By addToWishlistButton = By.cssSelector(".add-to-wishlist-button[data-productid]");
        click(addToWishlistButton);
        waitForWishlistAddSuccess();
    }

    public boolean isWishlistAddConfirmationShown() {
        return isDisplayed(By.cssSelector("#bar-notification.success, .bar-notification.success, .popup-notification"));
    }

    private void waitForWishlistAddSuccess() {
        try {
            WaitUtils.waitForElement(
                    By.cssSelector("#bar-notification.success, .bar-notification.success, .popup-notification"),
                    WaitType.VISIBLE);
        } catch (Exception exception) {
            logger.debug("Wishlist notification not shown, continuing with wishlist page verification");
        }
        waitForAjaxComplete();
    }

    public void waitForAddToCartSuccess() {
        try {
            WaitUtils.waitForElement(
                    By.cssSelector("#bar-notification.success, .bar-notification.success, .popup-notification"),
                    WaitType.VISIBLE);
        } catch (Exception exception) {
            logger.debug("Add-to-cart notification not shown, verifying cart quantity updated");
        }
        waitForAjaxComplete();
    }

    public void attemptAddCurrentProductToCart() {
        clickAddToCartOnProductDetailsPage();
        waitForAjaxComplete();
    }

    public boolean isAddToCartBlockedForSearchEngine() {
        By errorNotification = By.cssSelector(
                "#bar-notification.error .content, .bar-notification.error .content, .bar-notification.error");
        if (isDisplayedQuick(errorNotification)) {
            String message = getText(errorNotification);
            return message.toLowerCase().contains("search engine");
        }
        return isDisplayedQuick(By.xpath(
                "//div[contains(@class,'bar-notification') and contains(@class,'error')]"
                        + "//*[contains(translate(normalize-space(.),"
                        + " 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'search engine')]"));
    }
}
