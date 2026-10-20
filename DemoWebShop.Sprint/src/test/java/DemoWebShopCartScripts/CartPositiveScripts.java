
package DemoWebShopCartScripts;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import DemoWebShopBase.BaseClass;

public class CartPositiveScripts extends BaseClass {

    private final By addToCartLocator =
            By.cssSelector("input[id^='add-to-cart-button-']");

    private final By quantityLocator =
            By.cssSelector("input.qty-input");

    private final By updateCartLocator =
            By.name("updatecart");

    private final By cartRowsLocator =
            By.cssSelector("table.cart tr.cart-item-row");

    private final By termsCheckboxLocator =
            By.id("termsofservice");

    private final By checkoutLocator =
            By.name("checkout");

    private final String productName = "Computing and Internet";

    // =========================================================
    // COMMON FLOW: LOGIN -> ADD PRODUCT -> OPEN CART
    // =========================================================

    private void prepareCart() throws InterruptedException {

        loginToApplication();

        driver.get(url + "computing-and-internet");
        Thread.sleep(1500);

        List<WebElement> buttons =
                driver.findElements(addToCartLocator);

        WebElement addButton = null;

        for (WebElement button : buttons) {
            if (button.isDisplayed() && button.isEnabled()) {
                addButton = button;
                break;
            }
        }

        Assert.assertNotNull(
                addButton,
                "PRECONDITION FAILED: Add to Cart button was not found."
        );

        addButton.click();
        Thread.sleep(1500);

        driver.get(url + "cart");
        Thread.sleep(1000);

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/cart"),
                "Shopping cart page did not open."
        );

        Assert.assertFalse(
                driver.findElements(quantityLocator).isEmpty(),
                "PRECONDITION FAILED: Cart has no product quantity field."
        );

        System.out.println(
                "PASS: Product added to the shopping cart."
        );
    }

    // =========================================================
    // CART-POS-01: ADD PRODUCT TO CART
    // =========================================================

    @Test(groups = {"functional", "positive"})
    public void CART_POS_01_AddProductToCart()
            throws InterruptedException {

        prepareCart();

        List<WebElement> products = driver.findElements(
                By.xpath(
                        "//table[contains(@class,'cart')]"
                        + "//a[contains(normalize-space(.),'"
                        + productName + "')]"
                )
        );

        Assert.assertFalse(
                products.isEmpty(),
                "Product was not found in the shopping cart."
        );

        System.out.println(
                "PASS: CART-POS-01 - Product added to cart."
        );
    }

    // =========================================================
    // CART-POS-02: VERIFY PRODUCT IN CART
    // =========================================================

    @Test(groups = {"functional", "positive"})
    public void CART_POS_02_VerifyProductInCart()
            throws InterruptedException {

        prepareCart();

        WebElement product = driver.findElement(
                By.xpath(
                        "//table[contains(@class,'cart')]"
                        + "//a[contains(normalize-space(.),'"
                        + productName + "')]"
                )
        );

        Assert.assertTrue(
                product.isDisplayed(),
                "Expected product is not displayed in the cart."
        );

        Assert.assertTrue(
                product.getText().contains(productName),
                "Incorrect product is displayed in the cart."
        );

        System.out.println(
                "PASS: CART-POS-02 - Correct product appears in cart."
        );
    }

    // =========================================================
    // CART-POS-03: UPDATE PRODUCT QUANTITY
    // =========================================================

    @Test(groups = {"Integration", "positive"})
    public void CART_POS_03_UpdateProductQuantity()
            throws InterruptedException {

        prepareCart();

        WebElement quantityField =
                driver.findElement(quantityLocator);

        System.out.println(
                "Old quantity: "
                + quantityField.getAttribute("value")
        );

        quantityField.clear();
        quantityField.sendKeys("2");

        driver.findElement(updateCartLocator).click();

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(d -> {
            List<WebElement> fields =
                    d.findElements(quantityLocator);

            return !fields.isEmpty()
                    && "2".equals(
                            fields.get(0).getAttribute("value")
                    );
        });

        String newQuantity =
                driver.findElement(quantityLocator)
                        .getAttribute("value");

        Assert.assertEquals(
                newQuantity,
                "2",
                "Product quantity was not updated."
        );

        System.out.println(
                "PASS: CART-POS-03 - Product quantity updated successfully."
        );
    }

    // =========================================================
    // CART-POS-04: ACCEPT TERMS AND OPEN CHECKOUT
    // =========================================================

    @Test(groups = {"Smoke", "positive"})
    public void CART_POS_04_AcceptTermsAndCheckout()
            throws InterruptedException {

        prepareCart();

        WebElement termsCheckbox =
                driver.findElement(termsCheckboxLocator);

        if (!termsCheckbox.isSelected()) {
            termsCheckbox.click();
        }

        Assert.assertTrue(
                driver.findElement(termsCheckboxLocator).isSelected(),
                "Terms and Conditions checkbox was not selected."
        );

        WebElement checkoutButton =
                driver.findElement(checkoutLocator);

        Assert.assertTrue(
                checkoutButton.isDisplayed(),
                "Checkout button is not displayed."
        );

        Assert.assertTrue(
                checkoutButton.isEnabled(),
                "Checkout button is not enabled."
        );

        checkoutButton.click();

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(d ->
                d.getCurrentUrl()
                        .toLowerCase()
                        .contains("checkout")
        );

        Assert.assertTrue(
                driver.getCurrentUrl()
                        .toLowerCase()
                        .contains("checkout"),
                "Checkout page did not open. Current URL: "
                        + driver.getCurrentUrl()
        );

        System.out.println(
                "PASS: CART-POS-04 - Terms accepted and checkout opened."
        );
    }
}
