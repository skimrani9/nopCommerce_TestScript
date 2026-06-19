package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.pages.BasePage;
import org.openqa.selenium.By;

public class AdminLayoutPage extends BasePage {

    private final By logoutLink = By.cssSelector("a.nav-link[href*='logout']");

    public void clickLogout() {
        click(logoutLink);
    }

    public boolean isSidebarMenuVisible(String menuName) {
        By menuLocator = By.xpath(
                "//ul[contains(@class,'nav-sidebar')]//p[normalize-space()='" + menuName + "']");
        return isDisplayed(menuLocator);
    }
}
