
package DemoWebShopProductScripts;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import DemoWebShopBase.BaseClass;
import DemoWebShopPOMPage.HomePage;
import DemoWebShopPOMPage.LoginPage;
import DemoWebShopPOMPage.SearchPage;

public class ProductNegativeScripts extends BaseClass {

    // PROD-002: Search for an invalid product
    @Test(groups = {"functional"})
public void searchInvalidProduct() throws InterruptedException {

    String invalidProduct = "xyz123invalid";

    driver.get("https://demowebshop.tricentis.com/");

    WebElement searchBox = driver.findElement(
        By.id("small-searchterms")
    );

    searchBox.clear();
    searchBox.sendKeys(invalidProduct);

    driver.findElement(
        By.cssSelector("input.button-1.search-box-button")
    ).click();

    Thread.sleep(2000);

    String pageText = driver.findElement(
        By.tagName("body")
    ).getText();

    System.out.println("Search results page text:");
    System.out.println(pageText);

    boolean noProductsFound =
        pageText.toLowerCase().contains("no products");

    Assert.assertTrue(
        noProductsFound,
        "Expected no-product message was not displayed. "
        + "Actual page URL: " + driver.getCurrentUrl()
    );
}


    // PROD-004: Search with an empty value
    @Test(groups = {"Integration"})
    public void searchWithEmptyValue() {

    	 loginToApplication();
        // Step 2: Perform empty search
        HomePage home = new HomePage(driver);
        home.searchProduct("");

        // Step 3: Handle JavaScript alert if displayed
        try {
            Alert alert = driver.switchTo().alert();

            Assert.assertEquals(
                    alert.getText(),
                    "Please enter some search keyword",
                    "Unexpected alert message"
            );

            alert.accept();

        } catch (NoAlertPresentException e) {
            // No alert appeared; continue with page verification.
        }

        // Step 4: Verify application remains accessible
        Assert.assertTrue(
                driver.getTitle().contains("Demo Web Shop"),
                "Application did not handle empty search correctly"
        );
    }

    // PROD-010: Invalid product search system test


@Test(groups = {"system"})
public void invalidProductSearchFlow() {

	 loginToApplication();

    HomePage home = new HomePage(driver);
    home.searchProduct("xyz123");

    SearchPage search = new SearchPage(driver);

    Assert.assertFalse(
            search.isJewelryProductDisplayed(),
            "Jewelry product should not appear for invalid search"
    );

    Assert.assertTrue(
            driver.getPageSource().contains("No products were found"),
            "Expected no-product message was not displayed"
    );
}
}
