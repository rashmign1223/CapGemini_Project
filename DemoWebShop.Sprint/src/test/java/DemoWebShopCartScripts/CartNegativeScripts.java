
package DemoWebShopCartScripts;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import DemoWebShopBase.BaseClass;

public class CartNegativeScripts extends BaseClass {

    // Locators
    private final By quantityLocator =
            By.cssSelector("input.qty-input");

    private final By updateCartLocator =
            By.name("updatecart");

    private final By removeProductLocator =
            By.name("removefromcart");

    private final By cartRowsLocator =
            By.cssSelector("table.cart tr.cart-item-row");

    private final By termsCheckboxLocator =
            By.id("termsofservice");

    private final By checkoutButtonLocator =
            By.name("checkout");

    // =========================================================
    // COMMON METHOD: LOGIN -> ADD PRODUCT -> OPEN CART
    // =========================================================

    private void prepareCart() throws InterruptedException {

        loginToApplication();

        driver.get(url + "computing-and-internet");
        Thread.sleep(1500);

        List<WebElement> buttons = driver.findElements(
                By.cssSelector("input[id^='add-to-cart-button-']")
        );

        WebElement addButton = null;

        for (WebElement button : buttons) {
            if (button.isDisplayed() && button.isEnabled()) {
                addButton = button;
                break;
            }
        }

        Assert.assertNotNull(
                addButton,
                "PRECONDITION FAILED: Product Add to Cart button was not found."
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
                "PRECONDITION FAILED: No product was added to the cart."
        );

        System.out.println(
                "PASS: Product added to cart successfully."
        );
    }

    // =========================================================
    // HELPER: GET QUANTITY AFTER PAGE UPDATE
    // =========================================================

    private String getCurrentQuantity() {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        return wait.until(d -> {
            try {
                List<WebElement> fields =
                        d.findElements(quantityLocator);

                if (fields.isEmpty()) {
                    return null;
                }

                return fields.get(0).getAttribute("value");

            } catch (StaleElementReferenceException e) {
                return null;
            }
        });
    }

    // =========================================================
    // CART-NEG-01: EMPTY QUANTITY
    // =========================================================

    @Test(groups = {"functional", "negative"})
    public void CART_NEG_01_EmptyQuantity()
            throws InterruptedException {

        prepareCart();

        WebElement quantityField =
                driver.findElement(quantityLocator);

        quantityField.clear();

        Assert.assertEquals(
                quantityField.getAttribute("value"),
                "",
                "Quantity field was not cleared."
        );

        driver.findElement(updateCartLocator).click();

        String quantity = getCurrentQuantity();

        Assert.assertNotNull(
                quantity,
                "Quantity field was not found after updating the cart."
        );

        Assert.assertFalse(
                quantity.trim().isEmpty(),
                "Empty quantity was retained after updating the cart."
        );

        System.out.println(
                "PASS: CART-NEG-01 - Empty quantity was not retained."
        );
    }

    // =========================================================
    // CART-NEG-02: INVALID QUANTITY
    // =========================================================

    @Test(groups = {"Integration", "negative"})
    public void CART_NEG_02_InvalidQuantity()
            throws InterruptedException {

        prepareCart();

        WebElement quantityField =
                driver.findElement(quantityLocator);

        quantityField.clear();
        quantityField.sendKeys("abc");

        System.out.println(
                "Entered invalid quantity: abc"
        );

        driver.findElement(updateCartLocator).click();

        String quantity = getCurrentQuantity();

        Assert.assertNotNull(
                quantity,
                "Quantity field was not found after updating the cart."
        );

        Assert.assertFalse(
                quantity.contains("abc"),
                "Invalid text was retained as the product quantity."
        );

        System.out.println(
                "PASS: CART-NEG-02 - Invalid quantity text was not retained."
        );
    }

    // =========================================================
    // CART-NEG-03: CHECKOUT WITHOUT ACCEPTING TERMS
    // =========================================================

    @Test(groups = {"functional", "negative"})
    public void CART_NEG_03_CheckoutWithoutAcceptingTerms()
            throws InterruptedException {

        prepareCart();

        WebElement termsCheckbox =
                driver.findElement(termsCheckboxLocator);

        if (termsCheckbox.isSelected()) {
            termsCheckbox.click();
        }

        Assert.assertFalse(
                driver.findElement(termsCheckboxLocator).isSelected(),
                "Terms checkbox should be unchecked before checkout."
        );

        driver.findElement(checkoutButtonLocator).click();

        Thread.sleep(1000);

        boolean checkoutBlocked =
                driver.getCurrentUrl().contains("/cart");

        // Check for a JavaScript alert
        try {
            Alert alert = driver.switchTo().alert();

            System.out.println(
                    "Checkout validation message: " + alert.getText()
            );

            alert.accept();
            checkoutBlocked = true;

        } catch (NoAlertPresentException e) {
            // The website may display an HTML validation dialog.
        }

        // Check for a website validation dialog
        List<WebElement> dialogs =
                driver.findElements(
                        By.cssSelector("div.ui-dialog-content")
                );

        for (WebElement dialog : dialogs) {
            try {
                if (dialog.isDisplayed()
                        && dialog.getText().toLowerCase()
                                .contains("terms")) {

                    checkoutBlocked = true;
                    break;
                }
            } catch (StaleElementReferenceException e) {
                // Ignore an element replaced during page updates.
            }
        }

        Assert.assertTrue(
                checkoutBlocked,
                "Checkout was not blocked when terms were unchecked."
        );

        System.out.println(
                "PASS: CART-NEG-03 - Checkout was blocked without accepting terms."
        );
    }

    // =========================================================
    // CART-NEG-04: VERIFY EMPTY CART
    // =========================================================

    @Test(groups = {"Smoke", "negative"})
    public void CART_NEG_04_EmptyCartHasNoProduct()
            throws InterruptedException {

        driver.get(url + "cart");
        Thread.sleep(1000);

        WebElement cartHeading =
                driver.findElement(
                        By.cssSelector("div.page-title h1")
                );

        Assert.assertTrue(
                cartHeading.isDisplayed(),
                "Shopping cart page did not open."
        );

        Assert.assertTrue(
                cartHeading.getText().toLowerCase()
                        .contains("shopping cart"),
                "Incorrect shopping cart page heading."
        );

        // Select products for removal
        List<WebElement> removeCheckboxes =
                driver.findElements(removeProductLocator);

        for (WebElement checkbox : removeCheckboxes) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }

        // Remove products if the cart is not empty
        if (!removeCheckboxes.isEmpty()) {

            driver.findElement(updateCartLocator).click();

            WebDriverWait wait =
                    new WebDriverWait(
                            driver,
                            Duration.ofSeconds(10)
                    );

            wait.until(d ->
                    d.findElements(cartRowsLocator).isEmpty()
            );
        }

        // Verify that no product rows remain
        Assert.assertTrue(
                driver.findElements(cartRowsLocator).isEmpty(),
                "Cart is not empty; products are still present."
        );

        // Verify the empty-cart message
        WebElement cartContent =
                driver.findElement(
                        By.cssSelector("div.order-summary-content")
                );

        Assert.assertTrue(
                cartContent.getText().toLowerCase().contains("empty"),
                "Empty-cart message was not displayed."
        );

        System.out.println(
                "PASS: CART-NEG-04 - Cart is empty and contains no products."
        );
    }
}
