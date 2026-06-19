package com.nopcommerce.automation.pages.storefront;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class StorefrontHomePage extends BasePage {

    private final By header = By.cssSelector("header.header");
    private final By footer = By.cssSelector("footer.footer");
    private final By mainContent = By.cssSelector(".master-wrapper-content, main, .page.home-page");
    private final By loginLink = By.cssSelector(".header-links a.ico-login");
    private final By registerLink = By.cssSelector(".header-links a.ico-register");
    private final By sitemapHeading = By.cssSelector(".sitemap-page .page-title h1, .html-sitemap-page h1");

    public void openHomePage() {
        navigateTo(Constants.STORE_HOME_PATH);
        waitForHomePageLoad();
    }

    public void waitForHomePageLoad() {
        WaitUtils.waitForElement(header, WaitType.VISIBLE);
        WaitUtils.waitForElement(footer, WaitType.VISIBLE);
    }

    public boolean isHomePageDisplayed() {
        return isDisplayed(header) && isDisplayed(footer) && isDisplayed(mainContent);
    }

    public boolean areGuestHeaderLinksVisible() {
        return isDisplayed(loginLink) && isDisplayed(registerLink);
    }

    public void openSitemap() {
        navigateTo(Constants.STORE_SITEMAP_PATH);
        WaitUtils.waitForElement(sitemapHeading, WaitType.VISIBLE);
    }

    public boolean isSitemapPageDisplayed() {
        return isDisplayed(sitemapHeading)
                && isDisplayed(By.cssSelector(".sitemap-page .entity, .sitemap-page .entity-body ul li"));
    }

    public boolean isGuestSession() {
        return isDisplayed(loginLink) && !isDisplayed(By.cssSelector(".header-links a.ico-logout"));
    }

    public boolean isCustomerLoggedIn() {
        return isDisplayed(By.cssSelector(".header-links a.ico-logout"))
                && isDisplayed(By.cssSelector(".header-links a.ico-account, .header-links a[href*='customer/info']"));
    }

    public void logout() {
        navigateTo(Constants.STORE_LOGOUT_PATH);
        WaitUtils.waitForElement(loginLink, WaitType.VISIBLE);
    }
}
