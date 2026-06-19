package com.nopcommerce.automation.steps;

import com.nopcommerce.automation.pages.storefront.StorefrontHomePage;
import com.nopcommerce.automation.pages.storefront.StorefrontSeoPage;
import com.nopcommerce.automation.utils.AssertionUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchEngineSeoSteps {

    private final StorefrontSeoPage storefrontSeoPage = new StorefrontSeoPage();
    private final StorefrontHomePage storefrontHomePage = new StorefrontHomePage();

    @When("search engine crawler opens robots.txt")
    public void searchEngineCrawlerOpensRobotsTxt() {
        storefrontSeoPage.openRobotsTxt();
    }

    @When("search engine crawler opens XML sitemap")
    public void searchEngineCrawlerOpensXmlSitemap() {
        storefrontSeoPage.openXmlSitemap();
    }

    @When("search engine crawler opens the HTML sitemap page")
    public void searchEngineCrawlerOpensTheHtmlSitemapPage() {
        storefrontHomePage.openSitemap();
    }

    @Then("robots.txt content is displayed")
    public void robotsTxtContentIsDisplayed() {
        AssertionUtils.assertTrue(
                storefrontSeoPage.isRobotsTxtDisplayed(),
                "robots.txt should contain crawler directives");
    }

    @Then("XML sitemap content is displayed")
    public void xmlSitemapContentIsDisplayed() {
        AssertionUtils.assertTrue(
                storefrontSeoPage.isXmlSitemapDisplayed(),
                "XML sitemap should contain urlset or sitemapindex content");
    }
}
