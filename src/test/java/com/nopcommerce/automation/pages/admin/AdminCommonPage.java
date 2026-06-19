package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminCommonPage extends BasePage {

    public void clearStoreCache() {
        navigateTo("/Admin/Common/ClearCache");
        WaitUtils.waitForElement(By.cssSelector("div.wrapper"), WaitType.VISIBLE);
    }
}
