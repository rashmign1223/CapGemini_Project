
package DemoWebShopWishlist;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import DemoWebShopBase.BaseClass;
import DemoWebShopPOMPage.HomePage;
import DemoWebShopPOMPage.SearchPage;
import DemoWebShopPOMPage.ProductPage;
import DemoWebShopPOMPage.WishlistPage;

public class WishlistPositiveScript extends BaseClass {

    private final String productName = "Create Your Own Jewelry";

    private final By wishlistProductLocator =
            By.xpath(
                    "//div[contains(@class,'wishlist-content')]"
                    + "//a[contains(normalize-space(.),"
                    + "'Create Your Own Jewelry')]"
            );

    private final By wishlistCheckboxLocator =
            By.name("addtocart");

    private final By wishlistAddToCartLocator =
            By.cssSelector(
                    "input[name='addtocartbutton'], "
                    + "button[name='addtocartbutton']"
            );

    // =====================================================
    // COMMON METHOD: ADD JEWELRY TO WISHLIST
    // =====================================================

    private void addJewelryToWishlist(String material, String length) {

        loginToApplication();

        HomePage home = new HomePage(driver);
        home.searchProduct("jewelry");

        SearchPage search = new SearchPage(driver);

        Assert.assertTrue(
                search.isJewelryProductDisplayed(),
                "FAIL: Create Your Own Jewelry was not found."
        );

        search.clickJewelryProduct();

        ProductPage product = new ProductPage(driver);

        Assert.assertTrue(
                product.getProductName().contains(productName),
                "FAIL: Incorrect product page opened."
        );

        product.selectMaterial(material);
        product.enterLength(length);
        product.clickAddToWishlist();

        WishlistPage wishlist = new WishlistPage(driver);
        wishlist.openWishlistPage();

        Assert.assertTrue(
                wishlist.isWishlistDisplayed(),
                "FAIL: Wishlist page was not displayed."
        );
    }

    // =====================================================
    // WISH-POS-01: ADD PRODUCT TO WISHLIST
    // =====================================================

    @Test(groups = {"smoke"})
    public void Wish_Pos_01_AddProductToWishlist() {

        addJewelryToWishlist("Silver (1 mm)", "2");

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(d ->
                !d.findElements(wishlistProductLocator).isEmpty()
        );

        Assert.assertTrue(
                driver.findElement(wishlistProductLocator).isDisplayed(),
                "FAIL: Jewelry product was not added to wishlist."
        );

        System.out.println(
                "PASS: WISH-POS-01 - Product added to wishlist successfully."
        );
    }

    // =====================================================
    // WISH-POS-02: VERIFY WISHLIST PAGE
    // =====================================================

    @Test(groups = {"functional"})
    public void Wish_Pos_02_VerifyWishlistPage() {

        addJewelryToWishlist("Gold (1 mm)", "3");

        WishlistPage wishlist = new WishlistPage(driver);

        Assert.assertTrue(
                wishlist.isWishlistDisplayed(),
                "FAIL: Wishlist page did not open."
        );

        Assert.assertTrue(
                wishlist.isWishlistContentDisplayed(),
                "FAIL: Wishlist content was not displayed."
        );

        System.out.println(
                "PASS: WISH-POS-02 - Wishlist page displayed successfully."
        );
    }

    // =====================================================
    // WISH-POS-03: ADD WISHLIST PRODUCT TO CART
    // =====================================================

    @Test(groups = {"integration"})
    public void Wish_Pos_03_AddWishlistProductToCart()
            throws InterruptedException {

        addJewelryToWishlist("Silver (1 mm)", "2");

        // Verify the product exists in the wishlist
        Assert.assertFalse(
                driver.findElements(wishlistProductLocator).isEmpty(),
                "FAIL: Jewelry product was not found in wishlist."
        );

        // Select the product checkbox
        List<WebElement> checkboxes =
                driver.findElements(wishlistCheckboxLocator);

        Assert.assertFalse(
                checkboxes.isEmpty(),
                "FAIL: Wishlist product checkbox was not found."
        );

        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }

        // Locate the wishlist Add to Cart button
        List<WebElement> buttons =
                driver.findElements(wishlistAddToCartLocator);

        WebElement addToCartButton = null;

        for (WebElement button : buttons) {
            if (button.isDisplayed() && button.isEnabled()) {
                addToCartButton = button;
                break;
            }
        }

        Assert.assertNotNull(
                addToCartButton,
                "FAIL: Wishlist Add to Cart button was not found."
        );

        addToCartButton.click();
        Thread.sleep(1500);

        // Verify cart page or navigate there if needed
        if (!driver.getCurrentUrl().toLowerCase().contains("/cart")) {
            driver.get(url + "cart");
        }

        Assert.assertTrue(
                driver.getCurrentUrl().toLowerCase().contains("/cart"),
                "FAIL: Shopping cart page was not opened."
        );

        System.out.println(
                "PASS: WISH-POS-03 - Wishlist product added to cart."
        );
    }
}
