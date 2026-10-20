package DemoWebShopWishlist;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import DemoWebShopBase.BaseClass;
import DemoWebShopPOMPage.HomePage;
import DemoWebShopPOMPage.SearchPage;
import DemoWebShopPOMPage.ProductPage;
import DemoWebShopPOMPage.WishlistPage;

public class WishlistNegativeScripts extends BaseClass {

    private final By wishlistCheckboxes = By.name("addtocart");

    private final By addToCartButton = By.xpath(
        "//input[contains(translate(@value,"
        + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
        + "'abcdefghijklmnopqrstuvwxyz'),'add to cart')]"
        + " | //button[contains(translate(normalize-space(.),"
        + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
        + "'abcdefghijklmnopqrstuvwxyz'),'add to cart')]"
    );

    private final By cartQuantity = By.cssSelector(".cart-qty");

    /*
     * Helper method:
     * Login -> Search jewelry -> Open product -> Add to wishlist
     */
    private void addJewelryToWishlist() throws InterruptedException {

        loginToApplication();
        Thread.sleep(1500);

        HomePage home = new HomePage(driver);
        home.searchProduct("jewelry");
        Thread.sleep(1500);

        SearchPage search = new SearchPage(driver);

        Assert.assertTrue(
            search.isJewelryProductDisplayed(),
            "PRECONDITION FAILED: Jewelry product was not displayed."
        );

        search.clickJewelryProduct();
        Thread.sleep(1500);

        ProductPage product = new ProductPage(driver);

        product.selectMaterial("Silver");
        product.enterLength("10 cm");
        Thread.sleep(500);

        product.clickAddToWishlist();
        Thread.sleep(1500);

        WishlistPage wishlist = new WishlistPage(driver);
        wishlist.openWishlistPage();

        Assert.assertTrue(
            wishlist.isWishlistDisplayed(),
            "PRECONDITION FAILED: Wishlist page did not open."
        );

        Assert.assertFalse(
            wishlist.isWishlistEmpty(),
            "PRECONDITION FAILED: No product was added to the wishlist."
        );
    }

    /*
     * Returns the cart quantity text, for example "(0)" or "(1)".
     */
    private String getCartQuantity() {

        List<WebElement> elements = driver.findElements(cartQuantity);

        if (elements.isEmpty()) {
            return "";
        }

        return elements.get(0).getText().trim();
    }

    /*
     * NEGATIVE TEST:
     * Click Add to Cart without selecting any wishlist product.
     * The cart quantity should not increase.
     */
    @Test
    public void Wish_Neg_03_AddToCartWithoutSelection() throws InterruptedException {

        addJewelryToWishlist();

        WishlistPage wishlist = new WishlistPage(driver);

        // Ensure every wishlist product checkbox is unselected.
        wishlist.unselectAllProducts();

        Assert.assertTrue(
            wishlist.areAllProductsUnselected(),
            "PRECONDITION FAILED: One or more wishlist products remain selected."
        );

        String cartBefore = getCartQuantity();

        // Find the wishlist Add to Cart button.
        List<WebElement> buttons = driver.findElements(addToCartButton);

        WebElement buttonToClick = null;

        for (WebElement button : buttons) {
            if (button.isDisplayed() && button.isEnabled()) {
                buttonToClick = button;
                break;
            }
        }

        Assert.assertNotNull(
            buttonToClick,
            "PRECONDITION FAILED: Wishlist Add to Cart button was not found. "
            + "Current URL: " + driver.getCurrentUrl()
        );

        buttonToClick.click();
        Thread.sleep(1500);

        // Some versions of the site may display an alert when nothing is selected.
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert message: " + alert.getText());
            alert.accept();
            Thread.sleep(500);
        } catch (NoAlertPresentException e) {
            // No alert is also acceptable for this negative scenario.
        }

        String cartAfter = getCartQuantity();

        Assert.assertEquals(
            cartAfter,
            cartBefore,
            "FAIL: Cart quantity changed even though no wishlist product was selected."
        );

        Assert.assertTrue(
            wishlist.areAllProductsUnselected(),
            "FAIL: Wishlist product selection changed unexpectedly."
        );

        System.out.println(
            "PASS: Cart quantity did not change when Add to Cart was clicked "
            + "without selecting a wishlist product."
        );
    }
}