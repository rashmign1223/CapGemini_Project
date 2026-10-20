
package DemoWebShopPOMPage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage {

    private final WebDriver driver;

    // ==================================================
    // BILLING ADDRESS
    // ==================================================

    private final By billingStep =
            By.id("checkout-step-billing");

    private final By billingAddressSelect =
            By.id("billing-address-select");

    private final By billingFirstName =
            By.id("BillingNewAddress_FirstName");

    private final By billingLastName =
            By.id("BillingNewAddress_LastName");

    private final By billingEmail =
            By.id("BillingNewAddress_Email");

    private final By billingCountry =
            By.id("BillingNewAddress_CountryId");

    private final By billingCity =
            By.id("BillingNewAddress_City");

    private final By billingAddress =
            By.id("BillingNewAddress_Address1");

    private final By billingZip =
            By.id("BillingNewAddress_ZipPostalCode");

    private final By billingPhone =
            By.id("BillingNewAddress_PhoneNumber");

    private final By billingNewAddressContinue =
            By.cssSelector(
                    "#billing-buttons-container "
                    + "input.new-address-next-step-button");

    private final By billingSavedAddressContinue =
            By.cssSelector(
                    "#billing-buttons-container "
                    + "input.select-billing-address-button");

    private final By billingButtons =
            By.cssSelector(
                    "#billing-buttons-container input[type='button'], "
                    + "#billing-buttons-container button");

    // ==================================================
    // SHIPPING ADDRESS
    // ==================================================

    private final By shippingStep =
            By.id("checkout-step-shipping");

    private final By shippingAddressSelect =
            By.id("shipping-address-select");

    private final By shippingFirstName =
            By.id("ShippingNewAddress_FirstName");

    private final By shippingLastName =
            By.id("ShippingNewAddress_LastName");

    private final By shippingEmail =
            By.id("ShippingNewAddress_Email");

    private final By shippingCountry =
            By.id("ShippingNewAddress_CountryId");

    private final By shippingCity =
            By.id("ShippingNewAddress_City");

    private final By shippingAddress =
            By.id("ShippingNewAddress_Address1");

    private final By shippingZip =
            By.id("ShippingNewAddress_ZipPostalCode");

    private final By shippingPhone =
            By.id("ShippingNewAddress_PhoneNumber");

    private final By shippingNewAddressContinue =
            By.cssSelector(
                    "#shipping-buttons-container "
                    + "input.new-address-next-step-button");

    private final By shippingSavedAddressContinue =
            By.cssSelector(
                    "#shipping-buttons-container "
                    + "input.select-shipping-address-button");

    private final By shippingButtons =
            By.cssSelector(
                    "#shipping-buttons-container input[type='button'], "
                    + "#shipping-buttons-container button");

    // ==================================================
    // SHIPPING METHOD
    // ==================================================

    private final By shippingMethodStep =
            By.id("checkout-step-shipping-method");

    private final By shippingMethodOptions =
            By.cssSelector(
                    "#checkout-step-shipping-method "
                    + "input[name='shippingoption']");

    private final By shippingMethodContinue =
            By.cssSelector(
                    "#shipping-method-buttons-container "
                    + "input.shipping-method-next-step-button");

    // ==================================================
    // PAYMENT METHOD
    // ==================================================

    private final By paymentMethodStep =
            By.id("checkout-step-payment-method");

    private final By paymentMethodOptions =
            By.cssSelector(
                    "#checkout-step-payment-method "
                    + "input[name='paymentmethod']");

    private final By paymentMethodContinue =
            By.cssSelector(
                    "#payment-method-buttons-container "
                    + "input.payment-method-next-step-button");

    // ==================================================
    // PAYMENT INFORMATION
    // ==================================================

    private final By paymentInfoStep =
            By.id("checkout-step-payment-info");

    private final By paymentInfoContinue =
            By.cssSelector(
                    "#payment-info-buttons-container "
                    + "input.payment-info-next-step-button");

    // ==================================================
    // CONFIRM ORDER
    // ==================================================

    private final By confirmOrderStep =
            By.id("checkout-step-confirm-order");

    private final By confirmOrderButton =
            By.cssSelector(
                    "#confirm-order-buttons-container "
                    + "input.confirm-order-next-step-button");

    // ==================================================
    // ORDER COMPLETED
    // ==================================================

    private final By orderCompletedTitle =
            By.cssSelector(".section.order-completed .title");

    private final By orderNumber =
            By.cssSelector(".order-number");

    // ==================================================
    // CONSTRUCTOR
    // ==================================================

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    // ==================================================
    // COMMON HELPERS
    // ==================================================

    private List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    private WebElement findVisibleEnabledElement(By locator) {

        List<WebElement> elements = findElements(locator);

        for (WebElement element : elements) {
            try {
                if (element.isDisplayed() && element.isEnabled()) {
                    return element;
                }
            } catch (Exception ignored) {
                // Element may have changed during checkout.
            }
        }

        return null;
    }

    private WebElement getVisibleEnabledElement(By locator) {

        WebElement element = findVisibleEnabledElement(locator);

        if (element == null) {
            throw new IllegalStateException(
                    "Element not visible or enabled: " + locator
                    + " | URL: " + driver.getCurrentUrl());
        }

        return element;
    }

    private boolean isVisible(By locator) {

        for (WebElement element : findElements(locator)) {
            try {
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (Exception ignored) {
                // Continue checking.
            }
        }

        return false;
    }

    private boolean hasVisibleEnabledElement(By locator) {
        return findVisibleEnabledElement(locator) != null;
    }

    private void pause(long milliseconds) {

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Checkout wait was interrupted.", e);
        }
    }

    public void pauseForCheckoutUpdate() {
        pause(800);
    }

    private void enterText(By locator, String value) {

        WebElement element = getVisibleEnabledElement(locator);

        element.clear();
        element.sendKeys(value == null ? "" : value.trim());
    }

    private void selectCountry(By locator, String country) {

        WebElement element = getVisibleEnabledElement(locator);
        Select select = new Select(element);

        try {
            select.selectByVisibleText(country);
            return;
        } catch (Exception ignored) {
            // Try a case-insensitive match below.
        }

        for (WebElement option : select.getOptions()) {

            String text = option.getText().trim();

            if (text.equalsIgnoreCase(country)
                    || text.toLowerCase().contains(
                            country.toLowerCase().trim())) {

                select.selectByVisibleText(text);
                return;
            }
        }

        throw new IllegalStateException(
                "Country not found in dropdown: " + country);
    }

    private void clickStepContinue(
            By preferredButton, By fallbackButtons) {

        WebElement button =
                findVisibleEnabledElement(preferredButton);

        if (button == null) {
            button = getVisibleEnabledElement(fallbackButtons);
        }

        button.click();
        pauseForCheckoutUpdate();
    }

    private boolean matchesText(String actual, String expected) {

        if (actual == null || expected == null) {
            return false;
        }

        return actual.toLowerCase().contains(
                expected.trim().toLowerCase());
    }

    private boolean isNewAddressSelected(By dropdownLocator) {

        List<WebElement> dropdowns =
                findElements(dropdownLocator);

        By firstNameLocator =
                dropdownLocator.equals(billingAddressSelect)
                ? billingFirstName
                : shippingFirstName;

        if (dropdowns.isEmpty()) {
            return isVisible(firstNameLocator);
        }

        WebElement dropdown = dropdowns.get(0);

        if (!dropdown.isDisplayed()) {
            return isVisible(firstNameLocator);
        }

        Select select = new Select(dropdown);

        String value = select.getFirstSelectedOption()
                .getAttribute("value");

        return value == null
                || value.trim().isEmpty()
                || value.equals("0");
    }

    private String getOptionLabel(WebElement option) {

        String id = option.getAttribute("id");

        if (id != null && !id.trim().isEmpty()) {

            List<WebElement> labels = findElements(
                    By.cssSelector("label[for='" + id + "']"));

            for (WebElement label : labels) {
                try {
                    String text = label.getText().trim();

                    if (label.isDisplayed() && !text.isEmpty()) {
                        return text;
                    }
                } catch (Exception ignored) {
                    // Try the surrounding option container.
                }
            }
        }

        try {
            WebElement parent = option.findElement(
                    By.xpath("./ancestor::li[1]"));

            String text = parent.getText().trim();

            if (!text.isEmpty()) {
                return text;
            }
        } catch (Exception ignored) {
            // Try the immediate parent.
        }

        try {
            return option.findElement(By.xpath(".."))
                    .getText().trim();
        } catch (Exception ignored) {
            return option.getAttribute("value");
        }
    }

    // ==================================================
    // BILLING ADDRESS
    // ==================================================

    public void enterBillingDetails(
            String firstName,
            String lastName,
            String email,
            String country,
            String city,
            String address,
            String zip,
            String phone) {

        if (!isNewAddressSelected(billingAddressSelect)) {

            System.out.println(
                    "INFO | Existing billing address selected. "
                    + "Skipping new billing details.");

            return;
        }

        enterText(billingFirstName, firstName);
        enterText(billingLastName, lastName);
        enterText(billingEmail, email);
        selectCountry(billingCountry, country);
        enterText(billingCity, city);
        enterText(billingAddress, address);
        enterText(billingZip, zip);
        enterText(billingPhone, phone);

        System.out.println(
                "PASS | New billing address details entered.");
    }

    public void continueBilling() {

        if (isNewAddressSelected(billingAddressSelect)) {

            clickStepContinue(
                    billingNewAddressContinue,
                    billingButtons);

        } else {

            clickStepContinue(
                    billingSavedAddressContinue,
                    billingButtons);
        }

        System.out.println(
                "INFO | Billing Continue clicked. Waiting for next step...");

        long endTime = System.currentTimeMillis() + 10000;

        while (System.currentTimeMillis() < endTime) {

            if (isShippingAddressContinueDisplayed()
                    || isShippingMethodDisplayed()) {

                System.out.println(
                        "PASS | Checkout advanced beyond Billing.");

                return;
            }

            pause(300);
        }

        System.out.println(
                "WARNING | Checkout did not advance beyond Billing.");

        debugCheckoutSteps();
        printShippingMethodDebugDetails();
    }

    // ==================================================
    // SHIPPING ADDRESS
    // ==================================================

    public boolean isShippingAddressContinueDisplayed() {

        return isVisible(shippingStep)
                && (hasVisibleEnabledElement(shippingNewAddressContinue)
                || hasVisibleEnabledElement(
                        shippingSavedAddressContinue)
                || hasVisibleEnabledElement(shippingButtons));
    }

    public void enterShippingAddress(
            String firstName,
            String lastName,
            String email,
            String country,
            String city,
            String address,
            String zip,
            String phone) {

        if (!isNewAddressSelected(shippingAddressSelect)) {

            System.out.println(
                    "INFO | Existing shipping address selected. "
                    + "Skipping new shipping details.");

            return;
        }

        enterText(shippingFirstName, firstName);
        enterText(shippingLastName, lastName);
        enterText(shippingEmail, email);
        selectCountry(shippingCountry, country);
        enterText(shippingCity, city);
        enterText(shippingAddress, address);
        enterText(shippingZip, zip);
        enterText(shippingPhone, phone);

        System.out.println(
                "PASS | New shipping address details entered.");
    }

    public void continueShippingAddress() {

        if (isNewAddressSelected(shippingAddressSelect)) {

            clickStepContinue(
                    shippingNewAddressContinue,
                    shippingButtons);

        } else {

            clickStepContinue(
                    shippingSavedAddressContinue,
                    shippingButtons);
        }

        System.out.println(
                "PASS | Shipping Address Continue clicked.");
    }

    // ==================================================
    // SHIPPING METHOD
    // ==================================================

    public boolean isShippingMethodDisplayed() {

        return isVisible(shippingMethodStep)
                && (hasVisibleEnabledElement(shippingMethodOptions)
                || hasVisibleEnabledElement(shippingMethodContinue));
    }

    public boolean isShippingMethodContinueDisplayed() {
        return hasVisibleEnabledElement(shippingMethodContinue);
    }

    public boolean waitForShippingMethodStep() {

        long endTime = System.currentTimeMillis() + 15000;

        while (System.currentTimeMillis() < endTime) {

            if (isShippingMethodDisplayed()) {

                System.out.println(
                        "PASS | Shipping Method step is ready.");

                return true;
            }

            // A validation error may prevent the checkout from advancing.
            List<WebElement> errors = findElements(
                    By.cssSelector(
                            ".message-error, "
                            + ".validation-summary-errors, "
                            + ".field-validation-error"));

            for (WebElement error : errors) {
                try {
                    if (error.isDisplayed()
                            && !error.getText().trim().isEmpty()) {

                        System.out.println(
                                "CHECKOUT VALIDATION ERROR: "
                                + error.getText().trim());
                    }
                } catch (Exception ignored) {
                    // Continue waiting.
                }
            }

            pause(300);
        }

        System.out.println(
                "FAIL | Shipping Method did not become ready "
                + "within 15 seconds.");

        return false;
    }

    public void selectShippingMethod() {
        selectShippingMethod("");
    }

    public void selectShippingMethod(String preferredMethod) {

        List<WebElement> options =
                findElements(shippingMethodOptions);

        WebElement selectedOption = null;

        for (WebElement option : options) {

            try {
                if (!option.isDisplayed() || !option.isEnabled()) {
                    continue;
                }

                if (preferredMethod != null
                        && !preferredMethod.trim().isEmpty()
                        && matchesText(
                                getOptionLabel(option),
                                preferredMethod)) {

                    selectedOption = option;
                    break;
                }

                if (selectedOption == null) {
                    selectedOption = option;
                }

            } catch (Exception ignored) {
                // Try the next available option.
            }
        }

        if (selectedOption == null) {
            printShippingMethodDebugDetails();

            throw new IllegalStateException(
                    "No visible shipping method option found. URL: "
                    + driver.getCurrentUrl());
        }

        if (!selectedOption.isSelected()) {
            selectedOption.click();
        }

        System.out.println(
                "PASS | Shipping method selected: "
                + getOptionLabel(selectedOption));
    }

    public void continueShippingMethod() {

        getVisibleEnabledElement(shippingMethodContinue).click();

        System.out.println(
                "INFO | Shipping Method Continue clicked. "
                + "Waiting for Payment Method...");

        long endTime = System.currentTimeMillis() + 10000;

        while (System.currentTimeMillis() < endTime) {

            if (isPaymentMethodDisplayed()) {

                System.out.println(
                        "PASS | Payment Method step became available.");

                return;
            }

            pause(300);
        }

        printShippingMethodDebugDetails();
        debugCheckoutSteps();

        throw new IllegalStateException(
                "Payment Method did not appear after Shipping Method. URL: "
                + driver.getCurrentUrl());
    }

    public void clickShippingMethodContinue() {
        continueShippingMethod();
    }

    // ==================================================
    // PAYMENT METHOD
    // ==================================================

    public boolean isPaymentMethodDisplayed() {

        return isVisible(paymentMethodStep)
                && (hasVisibleEnabledElement(paymentMethodOptions)
                || hasVisibleEnabledElement(paymentMethodContinue));
    }

    public boolean isPaymentMethodContinueDisplayed() {
        return hasVisibleEnabledElement(paymentMethodContinue);
    }

    public void printAvailablePaymentMethods() {

        System.out.println();
        System.out.println("===== AVAILABLE PAYMENT METHODS =====");

        List<WebElement> options =
                findElements(paymentMethodOptions);

        System.out.println(
                "Payment option count: " + options.size());

        for (WebElement option : options) {

            try {
                System.out.println(
                        "ID: " + option.getAttribute("id")
                        + " | Value: " + option.getAttribute("value")
                        + " | Label: " + getOptionLabel(option)
                        + " | Selected: " + option.isSelected()
                        + " | Displayed: " + option.isDisplayed()
                        + " | Enabled: " + option.isEnabled());

            } catch (Exception e) {
                System.out.println(
                        "INFO | Payment option changed during inspection.");
            }
        }

        System.out.println("===== END PAYMENT METHODS =====");
    }

    public void selectPaymentMethod(String preferredMethod) {

        List<WebElement> options =
                findElements(paymentMethodOptions);

        WebElement selectedOption = null;

        for (WebElement option : options) {

            try {
                if (!option.isDisplayed() || !option.isEnabled()) {
                    continue;
                }

                String label = getOptionLabel(option);
                String value = option.getAttribute("value");

                if (matchesText(label, preferredMethod)
                        || matchesText(value, preferredMethod)) {

                    selectedOption = option;
                    break;
                }

            } catch (Exception ignored) {
                // Try the next option.
            }
        }

        if (selectedOption == null) {

            printAvailablePaymentMethods();

            throw new IllegalStateException(
                    "Payment method not found or not available: "
                    + preferredMethod);
        }

        if (!selectedOption.isSelected()) {
            selectedOption.click();
        }

        System.out.println(
                "PASS | Payment method selected: "
                + getOptionLabel(selectedOption));
    }

    public void clickPaymentMethodContinue() {

        clickVisibleButton(paymentMethodContinue);

        System.out.println(
                "PASS | Payment Method Continue clicked.");
    }

    // ==================================================
    // PAYMENT INFORMATION
    // ==================================================

    public boolean isPaymentInfoDisplayed() {

        return isVisible(paymentInfoStep)
                && hasVisibleEnabledElement(paymentInfoContinue);
    }

    public boolean isPaymentInfoContinueDisplayed() {
        return hasVisibleEnabledElement(paymentInfoContinue);
    }

    public void clickPaymentInfoContinueIfDisplayed() {

        if (isPaymentInfoContinueDisplayed()) {

            clickVisibleButton(paymentInfoContinue);

            System.out.println(
                    "PASS | Payment Information Continue clicked.");

        } else {

            System.out.println(
                    "INFO | Payment Information Continue "
                    + "is not displayed.");
        }
    }

    // ==================================================
    // CONFIRM ORDER
    // ==================================================

    public boolean isConfirmOrderDisplayed() {

        return isVisible(confirmOrderStep)
                && hasVisibleEnabledElement(confirmOrderButton);
    }

    public void confirmOrder() {

        clickVisibleButton(confirmOrderButton);

        System.out.println(
                "PASS | Confirm Order clicked.");
    }

    // ==================================================
    // ORDER COMPLETED
    // ==================================================

    public boolean isOrderCompleted() {

        long endTime = System.currentTimeMillis() + 10000;

        while (System.currentTimeMillis() < endTime) {

            List<WebElement> titles =
                    findElements(orderCompletedTitle);

            for (WebElement title : titles) {

                try {
                    if (title.isDisplayed()
                            && !title.getText().trim().isEmpty()) {

                        String text = title.getText().trim();

                        if (matchesText(
                                text,
                                "Your order has been successfully processed")) {

                            return true;
                        }
                    }
                } catch (Exception ignored) {
                    // Keep checking.
                }
            }

            pause(300);
        }

        return false;
    }

    public boolean isThankYouDisplayed() {
        return isVisible(orderCompletedTitle);
    }

    public String getOrderNumber() {

        List<WebElement> numbers = findElements(orderNumber);

        for (WebElement number : numbers) {

            try {
                if (number.isDisplayed()) {

                    String text = number.getText().trim();

                    if (!text.isEmpty()) {
                        return text;
                    }
                }
            } catch (Exception ignored) {
                // Try another matching element.
            }
        }

        return "Order number not displayed";
    }

    // ==================================================
    // BUTTON HELPER
    // ==================================================

    private void clickVisibleButton(By locator) {

        WebElement button = getVisibleEnabledElement(locator);

        button.click();
        pauseForCheckoutUpdate();
    }

    // ==================================================
    // CHECKOUT DEBUG
    // ==================================================

    public void debugCheckoutSteps() {

        System.out.println();
        System.out.println("========== CHECKOUT DEBUG ==========");

        System.out.println(
                "Current URL: " + driver.getCurrentUrl());

        System.out.println(
                "Billing step visible: " + isVisible(billingStep));

        System.out.println(
                "Shipping Address step visible: "
                + isVisible(shippingStep));

        System.out.println(
                "Shipping Method step visible: "
                + isVisible(shippingMethodStep));

        System.out.println(
                "Payment Method step visible: "
                + isVisible(paymentMethodStep));

        System.out.println(
                "Payment Information step visible: "
                + isVisible(paymentInfoStep));

        System.out.println(
                "Confirm Order step visible: "
                + isVisible(confirmOrderStep));

        System.out.println(
                "Shipping Address Continue visible: "
                + isShippingAddressContinueDisplayed());

        System.out.println(
                "Shipping Method Continue visible: "
                + isShippingMethodContinueDisplayed());

        System.out.println(
                "Payment Method Continue visible: "
                + isPaymentMethodContinueDisplayed());

        System.out.println(
                "Payment Information Continue visible: "
                + isPaymentInfoContinueDisplayed());

        System.out.println(
                "Confirm Order visible: "
                + isConfirmOrderDisplayed());

        System.out.println("====================================");
    }

    public void printShippingMethodDebugDetails() {

        System.out.println();
        System.out.println(
                "===== SHIPPING METHOD DEBUG DETAILS =====");

        System.out.println(
                "Current URL: " + driver.getCurrentUrl());

        List<WebElement> errors = findElements(
                By.cssSelector(
                        ".message-error, "
                        + ".validation-summary-errors, "
                        + ".field-validation-error, "
                        + ".message-warning"));

        boolean errorFound = false;

        for (WebElement error : errors) {

            try {
                if (error.isDisplayed()) {

                    String text = error.getText().trim();

                    if (!text.isEmpty()) {
                        System.out.println(
                                "CHECKOUT ERROR: " + text);
                        errorFound = true;
                    }
                }
            } catch (Exception ignored) {
                // Continue inspecting.
            }
        }

        if (!errorFound) {
            System.out.println(
                    "No visible checkout error message found.");
        }

        List<WebElement> shippingOptions =
                findElements(shippingMethodOptions);

        System.out.println(
                "Shipping options count: " + shippingOptions.size());

        for (WebElement option : shippingOptions) {

            try {
                System.out.println(
                        "Value: " + option.getAttribute("value")
                        + " | Selected: " + option.isSelected()
                        + " | Displayed: " + option.isDisplayed()
                        + " | Enabled: " + option.isEnabled());
            } catch (Exception ignored) {
                // Continue inspecting.
            }
        }

        printSectionDetails(
                "Shipping section", shippingMethodStep);

        printSectionDetails(
                "Payment section", paymentMethodStep);

        System.out.println(
                "===== END SHIPPING METHOD DEBUG =====");
    }

    private void printSectionDetails(
            String description, By locator) {

        List<WebElement> sections = findElements(locator);

        for (WebElement section : sections) {

            try {
                System.out.println(
                        description + " class: "
                        + section.getAttribute("class"));

                System.out.println(
                        description + " text: "
                        + section.getText());

            } catch (Exception ignored) {
                // Section may have updated.
            }
        }
    }
}
