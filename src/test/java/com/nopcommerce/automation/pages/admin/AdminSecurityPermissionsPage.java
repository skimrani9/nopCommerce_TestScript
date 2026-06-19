package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminSecurityPermissionsPage extends BasePage {

    public void enableMultiFactorAuthenticationForAdministrators() {
        navigateTo(Constants.ADMIN_SECURITY_PERMISSIONS_PATH);
        WaitUtils.waitForElement(By.id("permissions-grid"), WaitType.VISIBLE);
        By mfaPermissionCheckbox = By.xpath(
                "//table[contains(@id,'permission-items-grid')]//tr[.//td[contains(normalize-space(.), 'Enable Multi-factor authentication') or contains(normalize-space(.), 'EnableMultiFactorAuthentication')]]"
                        + "//input[contains(@name,'Administrators') or contains(@id,'Administrators')]");
        if (isDisplayed(mfaPermissionCheckbox)) {
            setCheckbox(mfaPermissionCheckbox, true);
            click(By.cssSelector("#permissions-form button[type='submit']"));
            WaitUtils.waitForElement(By.id("permissions-grid"), WaitType.VISIBLE);
        }
    }
}
