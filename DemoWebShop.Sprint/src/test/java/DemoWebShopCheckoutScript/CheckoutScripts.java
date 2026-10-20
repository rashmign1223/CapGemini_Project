
package DemoWebShopCheckoutScript;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import DemoWebShopBase.BaseClass;
import DemoWebShopPOMPage.HomePage;
import DemoWebShopPOMPage.ProductPage;
import DemoWebShopPOMPage.CartPage;
import DemoWebShopPOMPage.CheckoutPage;
import GenericUtilites.ExcelUtility;

public class CheckoutScripts extends BaseClass {

    @DataProvider(name = "checkoutData")
    public Object[][] checkoutData() {

        List<Object[]> rows = ExcelUtility.getData("Sheet3");

        Assert.assertNotNull(
                rows,
                "Checkout data was not loaded from Sheet3.");

        Assert.assertFalse(
                rows.isEmpty(),
                "Sheet3 contains no checkout data.");

        Object[][] data = new Object[rows.size()][10];

        for (int i = 0; i < rows.size(); i++) {

            Object[] row = rows.get(i);

            Assert.assertNotNull(
                    row,
                    "Empty Excel row at index " + i);

            Assert.assertTrue(
                    row.length >= 10,
                    "Excel row " + (i + 1)
                    + " must contain at least 10 columns.");

            for (int j = 0; j < 10; j++) {
                data[i][j] = row[j] == null
                        ? ""
                        : row[j].toString().trim();
            }
        }

        return data;
    }

    @Test(dataProvider = "checkoutData")
    public void checkoutUsingCashOnDelivery(
            String email,
            String password,
            String firstName,
            String lastName,
            String billingEmail,
            String country,
            String city,
            String address,
            String zip,
            String phone) {

        // Login uses the credentials configured in BaseClass.
        // Excel email/password are retained for the data provider.

        performCheckout(
                firstName,
                lastName,
                billingEmail,
                country,
                city,
                address,
                zip,
                phone);
    }

    private void performCheckout(
            String firstName,
            String lastName,
            String billingEmail,
            String country,
            String city,
            String address,
            String zip,
            String phone) {

        System.out.println();
        System.out.println("======================================");
        System.out.println("STARTING CHECKOUT TEST");
        System.out.println("Customer: " + firstName + " " + lastName);
        System.out.println("Billing Email: " + billingEmail);
        System.out.println("======================================");

        // Step 1: Login
        loginToApplication();
        System.out.println("PASS | Login completed");

        // Step 2: Open Jewelry category
        HomePage homePage = new HomePage(driver);
        homePage.clickJewelry();
        System.out.println("PASS | Jewelry category opened");

        // Step 3: Configure product
        ProductPage productPage = new ProductPage(driver);
        productPage.clickFirstProduct();
        productPage.enterLength("10");
        productPage.clickAddToCart();
        productPage.printNotification();

        System.out.println("PASS | Product configured and added to cart");

        // Step 4: Open cart

CartPage cartPage = new CartPage(driver);
cartPage.openShoppingCart();

System.out.println("Cart URL: " + driver.getCurrentUrl());

System.out.println(
        "Cart quantity: "
        + driver.findElement(By.cssSelector("span.cart-qty")).getText());

System.out.println(
        "Cart page content: "
        + driver.findElement(By.tagName("body")).getText());

List<WebElement> cartRows = driver.findElements(
        By.cssSelector("table.cart tbody tr.cart-item-row"));

System.out.println("Cart rows found: " + cartRows.size());

for (WebElement row : cartRows) {
    System.out.println("Cart row details: " + row.getText());
}

Assert.assertFalse(
        cartRows.isEmpty(),
        "Shopping cart is empty. Check the add-to-cart notification and cart quantity.");


        boolean productFound = false;

        for (WebElement row : cartRows) {
            if (row.getText().contains("Create Your Own Jewelry")) {
                productFound = true;
                break;
            }
        }

        Assert.assertTrue(
                productFound,
                "Create Your Own Jewelry was not found in the cart.");

        System.out.println("PASS | Product verified in shopping cart");

        // Step 6: Accept terms and checkout
        WebElement terms = driver.findElement(
                By.id("termsofservice"));

        if (!terms.isSelected()) {
            terms.click();
        }

        cartPage.clickCheckout();

        System.out.println("PASS | Terms accepted and Checkout clicked");

        CheckoutPage checkoutPage = new CheckoutPage(driver);

        // Step 7: Billing Address
        checkoutPage.enterBillingDetails(
                firstName,
                lastName,
                billingEmail,
                country,
                city,
                address,
                zip,
                phone);

        checkoutPage.continueBilling();

        System.out.println(
                "INFO | URL after Billing: " + driver.getCurrentUrl());

        // Wait for the next checkout step to load.
        pauseForCheckout(1000);

        checkoutPage.debugCheckoutSteps();

        // Step 8: Shipping Address, when required
        if (checkoutPage.isShippingAddressContinueDisplayed()) {

            System.out.println(
                    "PASS | Shipping Address step displayed");

            checkoutPage.enterShippingAddress(
                    firstName,
                    lastName,
                    billingEmail,
                    country,
                    city,
                    address,
                    zip,
                    phone);

            checkoutPage.continueShippingAddress();

            System.out.println(
                    "INFO | Shipping Address submitted");

            pauseForCheckout(1000);
            checkoutPage.debugCheckoutSteps();

        } else {
            System.out.println(
                    "INFO | Shipping Address Continue is not displayed.");
        }

        // Step 9: Wait for Shipping Method.
        // This method must be implemented in CheckoutPage.
        if (!checkoutPage.waitForShippingMethodStep()) {

            printCheckoutFailureDetails(checkoutPage);

            throw new IllegalStateException(
                    "Shipping Method step did not become ready for "
                    + firstName + " " + lastName
                    + ". URL: " + driver.getCurrentUrl());
        }

        // Select shipping method and continue.
        checkoutPage.selectShippingMethod();
        checkoutPage.continueShippingMethod();

        // Step 10: Payment Method
        if (!checkoutPage.isPaymentMethodDisplayed()) {

            printCheckoutFailureDetails(checkoutPage);

            throw new IllegalStateException(
                    "Payment Method step was not displayed.");
        }

        checkoutPage.printAvailablePaymentMethods();

        checkoutPage.selectPaymentMethod("Cash On Delivery");
        checkoutPage.clickPaymentMethodContinue();

        System.out.println(
                "PASS | Cash On Delivery payment method submitted");

        // Step 11: Payment Information
        if (checkoutPage.isPaymentInfoDisplayed()) {

            checkoutPage.clickPaymentInfoContinueIfDisplayed();

        } else {
            System.out.println(
                    "INFO | Payment Information step was skipped.");
        }

        // Step 12: Confirm Order
        pauseForCheckout(800);

        if (!checkoutPage.isConfirmOrderDisplayed()) {

            System.out.println(
                    "FAIL | Confirm Order step did not appear");

            checkoutPage.debugCheckoutSteps();
            checkoutPage.printShippingMethodDebugDetails();

            throw new IllegalStateException(
                    "Confirm Order step is not displayed. URL: "
                    + driver.getCurrentUrl());
        }

        System.out.println("PASS | Confirm Order step displayed");

        checkoutPage.confirmOrder();

        // Step 13: Verify completed order
        Assert.assertTrue(
                checkoutPage.isOrderCompleted(),
                "Order completion message was not displayed.");

        System.out.println("PASS | Order completed successfully");

        System.out.println(
                "Order Number: " + checkoutPage.getOrderNumber());

        System.out.println(
                "PASS | Checkout test completed for "
                + firstName + " " + lastName);

        System.out.println("======================================");
    }

    private void pauseForCheckout(long milliseconds) {

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Checkout wait was interrupted.", e);
        }
    }

    private void printCheckoutFailureDetails(
            CheckoutPage checkoutPage) {

        System.out.println();
        System.out.println("===== CHECKOUT FAILURE DETAILS =====");

        System.out.println("Current URL: " + driver.getCurrentUrl());

        checkoutPage.debugCheckoutSteps();
        checkoutPage.printShippingMethodDebugDetails();

        System.out.println("Visible checkout messages:");

        List<WebElement> messages = driver.findElements(
                By.cssSelector(
                        ".message-error, "
                        + ".field-validation-error, "
                        + ".validation-summary-errors, "
                        + ".message-warning"));

        boolean messageFound = false;

        for (WebElement message : messages) {
            try {
                if (message.isDisplayed()) {
                    String text = message.getText().trim();

                    if (!text.isEmpty()) {
                        System.out.println(text);
                        messageFound = true;
                    }
                }
            } catch (Exception e) {
                System.out.println(
                        "INFO | A message element changed during inspection.");
            }
        }

        if (!messageFound) {
            System.out.println("No visible checkout error message found.");
        }

        System.out.println("===== END CHECKOUT FAILURE DETAILS =====");
    }
}
