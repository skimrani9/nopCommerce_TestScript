package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminPluginListPage extends BasePage {

    private static final String GOOGLE_AUTHENTICATOR_PLUGIN = "MultiFactorAuth.GoogleAuthenticator";

    private final By pluginsGrid = By.id("plugins-local-grid");
    private final By searchFriendlyNameInput = By.id("SearchFriendlyName");
    private final By searchPluginsButton = By.id("search-plugins-local");

    public void ensureGoogleAuthenticatorPluginInstalled() {
        navigateTo(Constants.ADMIN_PLUGIN_LIST_PATH);
        WaitUtils.waitForElement(pluginsGrid, WaitType.VISIBLE);
        type(searchFriendlyNameInput, "Google Authenticator");
        click(searchPluginsButton);
        WaitUtils.waitForElement(pluginsGrid, WaitType.VISIBLE);

        By installButton = By.cssSelector("button[name='install-plugin-link-" + GOOGLE_AUTHENTICATOR_PLUGIN + "']");
        if (isDisplayed(installButton)) {
            click(installButton);
            WaitUtils.waitForElement(pluginsGrid, WaitType.VISIBLE);
        }

        By installedMarker = By.cssSelector(
                "button.uninstall-plugin-link[name='uninstall-plugin-link-" + GOOGLE_AUTHENTICATOR_PLUGIN + "']");
        WaitUtils.waitForElement(installedMarker, WaitType.VISIBLE);
    }
}
