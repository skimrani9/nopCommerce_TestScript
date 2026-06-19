package com.nopcommerce.automation.pages.storefront;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.pages.BasePage;

public class StorefrontSeoPage extends BasePage {

    public void openRobotsTxt() {
        navigateTo(Constants.STORE_ROBOTS_TXT_PATH);
    }

    public void openXmlSitemap() {
        navigateTo(Constants.STORE_SITEMAP_XML_PATH);
    }

    public boolean isRobotsTxtDisplayed() {
        String bodyText = getPageSourceText();
        return bodyText.contains("User-agent") || bodyText.contains("Disallow");
    }

    public boolean isXmlSitemapDisplayed() {
        String bodyText = getPageSourceText();
        return bodyText.contains("<urlset") || bodyText.contains("<sitemapindex");
    }

    private String getPageSourceText() {
        return driver.getPageSource();
    }
}
