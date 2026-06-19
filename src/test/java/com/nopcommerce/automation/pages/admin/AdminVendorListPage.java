package com.nopcommerce.automation.pages.admin;

import com.nopcommerce.automation.constants.Constants;
import com.nopcommerce.automation.constants.WaitType;
import com.nopcommerce.automation.pages.BasePage;
import com.nopcommerce.automation.utils.VendorStorefrontTestDataHelper;
import com.nopcommerce.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class AdminVendorListPage extends BasePage {

    private final By vendorsGrid = By.id("vendors-grid");
    private final By searchNameInput = By.id("SearchName");
    private final By searchEmailInput = By.id("SearchEmail");
    private final By searchVendorsButton = By.id("search-vendors");

    public void openVendorList() {
        navigateTo(Constants.ADMIN_VENDOR_LIST_PATH);
        WaitUtils.waitForElement(vendorsGrid, WaitType.VISIBLE);
    }

    public void searchByVendorName(String vendorName) {
        expandAdminSearchPanelIfCollapsed();
        type(searchNameInput, vendorName);
        click(searchVendorsButton);
        waitForVendorInGrid(vendorName);
    }

    public void searchByVendorEmail(String vendorEmail) {
        expandAdminSearchPanelIfCollapsed();
        type(searchEmailInput, vendorEmail);
        click(searchVendorsButton);
        waitForVendorInGrid(vendorEmail);
    }

    public void openVendorEditFromList(String vendorName) {
        openVendorList();
        try {
            searchByVendorName(vendorName);
        } catch (Exception exception) {
            searchByVendorEmail(VendorStorefrontTestDataHelper.E2E_VENDOR_SHOP_EMAIL);
            waitForVendorInGrid(vendorName);
        }
        By editLink = By.xpath(
                "//table[@id='vendors-grid']//tr[.//td[contains(normalize-space(.), '" + vendorName + "')]"
                        + " or .//td[contains(normalize-space(.), '"
                        + VendorStorefrontTestDataHelper.E2E_VENDOR_SHOP_EMAIL + "')]]"
                        + "//a[contains(@href,'Vendor/Edit')]");
        click(editLink);
        WaitUtils.waitForUrlContains("/vendor/edit");
    }

    public boolean isVendorListed(String vendorName) {
        openVendorList();
        try {
            searchByVendorName(vendorName);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private void waitForVendorInGrid(String searchText) {
        waitForAjaxComplete();
        By vendorRow = By.xpath(
                "//table[@id='vendors-grid']//tr[.//td[contains(normalize-space(.), '" + searchText + "')]]");
        WaitUtils.waitForElement(vendorRow, WaitType.VISIBLE);
    }
}
