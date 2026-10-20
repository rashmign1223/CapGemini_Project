
package DemoWebShopProductScripts;

import org.testng.Assert;
import org.testng.annotations.Test;

import DemoWebShopBase.BaseClass;

public class ProdcuctPositiveScripts extends BaseClass {

    // PROD-001: Search for valid jewelry
    @Test(groups = {"functional"})
    public void searchValidJewelry() {

        loginToApplication();

        home.searchProduct("jewelry");

        Assert.assertTrue(
            search.isJewelryProductDisplayed(),
            "Jewelry product is not displayed"
        );
    }

    // PROD-003: Verify jewelry product details
    @Test(groups = {"functional"})
    public void verifyJewelryProductDetails() {

        loginToApplication();

        home.searchProduct("jewelry");

        Assert.assertEquals(
            search.getJewelryProductName(),
            "Create Your Own Jewelry",
            "Incorrect jewelry product name"
        );

        Assert.assertTrue(
            search.isJewelryImageDisplayed(),
            "Product image is not displayed"
        );

        Assert.assertTrue(
            search.getJewelryPrice().contains("100"),
            "Product price is incorrect"
        );

        Assert.assertTrue(
            search.isRatingDisplayed(),
            "Product rating is not displayed"
        );
    }

    // PROD-005: Search and open jewelry product
    @Test(groups = {"integration"})
    public void searchAndOpenJewelryProduct() {

        loginToApplication();

        home.searchProduct("jewelry");
        search.clickJewelryProduct();

        Assert.assertEquals(
            product.getProductName(),
            "Create Your Own Jewelry",
            "Incorrect product page opened"
        );
    }
    // PROD-009: Complete jewelry product verification
    @Test(groups = {"Smoke"})
    public void completeJewelryFlow() {

        // Step 1: Login using Excel credentials
        loginToApplication();

        // Step 2: Search for jewelry
        home.searchProduct("jewelry");

        // Step 3: Open the jewelry product
        search.clickJewelryProduct();

        // Step 4: Verify the product name
        Assert.assertEquals(
            product.getProductName(),
            "Create Your Own Jewelry",
            "Incorrect product page opened"
        );

        // Step 5: Print available material options
        product.printMaterialOptions();

        // Step 6: Enter length in cm
        product.enterLength("2");

        System.out.println(
            "PASS | Jewelry length entered: 2 cm"
        );
    }


    // PROD-006: Add jewelry to wishlist
    @Test(groups = {"integration"})
    public void addJewelryToWishlist() {

        loginToApplication();

        home.searchProduct("jewelry");
        search.clickJewelryProduct();

        product.clickAddToWishlist();

        home.clickWishlist();

        Assert.assertTrue(
            driver.getCurrentUrl().contains("wishlist"),
            "Wishlist page did not open"
        );
    }

    // PROD-007: Add jewelry to shopping cart
    @Test(groups = {"integration"})
    public void addJewelryToCart() {

        loginToApplication();

        home.searchProduct("jewelry");
        search.clickJewelryProduct();

        product.clickAddToCart();

        home.clickShoppingCart();

        Assert.assertTrue(
            driver.getCurrentUrl().contains("cart"),
            "Shopping Cart page did not open"
        );
    }

    // PROD-008: Product smoke test
    @Test(groups = {"smoke"})
    public void productSmokeTest() {

        loginToApplication();

        home.searchProduct("jewelry");

        Assert.assertTrue(
            search.isJewelryProductDisplayed(),
            "Product functionality is not working"
        );
    }

}
