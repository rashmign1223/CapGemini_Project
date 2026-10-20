package DemoWebShopPOMPage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage {

    private WebDriver driver;

    // Shopping cart link
    private By shoppingCartLink =
            By.cssSelector("a[href='/cart']");

    // Cart items
    private By cartItems =
            By.cssSelector(
                    "table.cart tbody tr.cart-item"
            );

    // Terms and conditions
    private By termsCheckbox =
            By.id("termsofservice");

    // Checkout
    private By checkoutButton =
            By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    // =========================================================
    // OPEN SHOPPING CART
    // =========================================================

    public void openShoppingCart() {

        driver.findElement(
                shoppingCartLink
        ).click();

        System.out.println(
                "Shopping Cart opened."
        );
    }

    // =========================================================
    // VERIFY CART NOT EMPTY
    // =========================================================

    public boolean isCartNotEmpty() {

        List<WebElement> items =
                driver.findElements(cartItems);

        return !items.isEmpty();
    }

    // =========================================================
    // SELECT TERMS
    // =========================================================

    public void selectTermsAndConditions() {

        WebElement terms =
                driver.findElement(
                        termsCheckbox
                );

        if (!terms.isSelected()) {
            terms.click();
        }

        System.out.println(
                "Terms & Conditions selected."
        );
    }

    // =========================================================
    // CLICK CHECKOUT
    // =========================================================

    public void clickCheckout() {

        driver.findElement(
                checkoutButton
        ).click();

        System.out.println(
                "Checkout clicked."
        );
    }
}