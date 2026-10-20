
package DemoWebShopPOMPage;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {

    private WebDriver driver;

    // PRODUCT LOCATORS
    private By firstProduct =
            By.cssSelector(".product-item .product-title a");

    private By productName =
            By.cssSelector(".product-name h1");

    private By materialDropdown =
            By.xpath("//label[contains(normalize-space(.),'Material')]/following::select[1]");

    private By lengthTextBox =
            By.xpath("//label[contains(normalize-space(.),'Length')]/following::input[1]");

    private By addToCartButton =
            By.cssSelector("input[value='Add to cart']");

    private By addToWishlist =
            By.xpath("//input[contains(translate(@value,"
                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                    + "'abcdefghijklmnopqrstuvwxyz'),"
                    + "'add to wishlist')]");

    private By notification =
            By.cssSelector(".bar-notification");

    // CONSTRUCTOR
    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    // SELECT FIRST PRODUCT
    public void clickFirstProduct() {

        List<WebElement> products =
                driver.findElements(firstProduct);

        if (products.isEmpty()) {
            throw new AssertionError(
                    "No product found on the current page.");
        }

        WebElement first = products.get(0);

        if (!first.isDisplayed()) {
            throw new AssertionError(
                    "The first product is not displayed.");
        }

        first.click();

        System.out.println("PASS | First product selected");
    }

    // GET PRODUCT NAME
    public String getProductName() {

        List<WebElement> names =
                driver.findElements(productName);

        if (names.isEmpty()) {
            throw new AssertionError(
                    "Product name was not found.");
        }

        return names.get(0).getText().trim();
    }

    // PRINT MATERIAL OPTIONS
    public void printMaterialOptions() {

        List<WebElement> dropdowns =
                driver.findElements(materialDropdown);

        if (dropdowns.isEmpty()) {
            System.out.println(
                    "INFO | No material dropdown on this product.");
            return;
        }

        Select select = new Select(dropdowns.get(0));

        for (WebElement option : select.getOptions()) {
            System.out.println(
                    "Material option: " + option.getText());
        }
    }


public void selectMaterial(String materialName) {

    By materialDropdown = By.id("product_attribute_71_9_16");

    List<WebElement> dropdowns =
            driver.findElements(materialDropdown);

    // Fallback locator if the product ID locator is not found
    if (dropdowns.isEmpty()) {
        dropdowns = driver.findElements(
                By.xpath(
                    "//label[contains(normalize-space(.),'Material')]"
                    + "/following::select[1]"
                )
        );
    }

    if (dropdowns.isEmpty()) {
        throw new AssertionError(
                "Material dropdown was not found. Current URL: "
                + driver.getCurrentUrl()
                + ". Verify that the Create Your Own Jewelry "
                + "product details page is open."
        );
    }

    WebElement dropdown = dropdowns.get(0);

    if (!dropdown.isDisplayed() || !dropdown.isEnabled()) {
        throw new AssertionError(
                "Material dropdown is not visible or enabled."
        );
    }

    Select select = new Select(dropdown);
    select.selectByVisibleText(materialName);

    System.out.println(
            "PASS: Material selected: " + materialName
    );
}


    // ENTER LENGTH IN CM
    public void enterLength(String lengthInCm) {

        if (lengthInCm == null || lengthInCm.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Length cannot be empty.");
        }

        List<WebElement> fields =
                driver.findElements(lengthTextBox);

        if (fields.isEmpty()) {
            throw new AssertionError(
                    "Length field was not found on this product.");
        }

        WebElement field = fields.get(0);
        field.clear();
        field.sendKeys(lengthInCm.trim());

        System.out.println(
                "PASS | Length entered: " + lengthInCm + " cm");
    }

    // ADD TO CART

public void clickAddToCart() {

    WebDriverWait wait =
            new WebDriverWait(driver, Duration.ofSeconds(10));

    WebElement button = wait.until(
            ExpectedConditions.elementToBeClickable(addToCartButton));

    button.click();

    try {
        WebElement notification = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.bar-notification")));

        String message = notification.getText().trim();

        System.out.println("Add-to-cart notification: " + message);

        if (message.toLowerCase().contains("added to your shopping cart")) {
            System.out.println("PASS | Product added to cart");
        } else {
            System.out.println("WARNING | Product addition not confirmed");
        }

    } catch (Exception e) {
        System.out.println(
                "WARNING | No add-to-cart confirmation detected");
    }
}


    // ADD TO WISHLIST
    public void clickAddToWishlist() {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(addToWishlist));

        button.click();

        System.out.println("Clicked Add to Wishlist.");

        try {
            WebElement message = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            notification));

            String text = message.getText().trim();

            System.out.println("Wishlist notification: " + text);

            String lowerText = text.toLowerCase();

            if (lowerText.contains("failed")
                    || lowerText.contains("error")
                    || lowerText.contains("already exists")) {

                throw new AssertionError(
                        "Wishlist operation needs checking: " + text);
            }

        } catch (TimeoutException e) {
            throw new AssertionError(
                    "No notification appeared after clicking Add to Wishlist.",
                    e);
        }
    }

    // PRINT NOTIFICATION
    public void printNotification() {

        List<WebElement> messages =
                driver.findElements(notification);

        boolean found = false;

        for (WebElement message : messages) {

            if (message.isDisplayed()
                    && !message.getText().trim().isEmpty()) {

                System.out.println(
                        "Notification: " + message.getText().trim());

                found = true;
            }
        }

        if (!found) {
            System.out.println(
                    "INFO | No notification currently displayed.");
        }
    }

	public void clickCreateYourOwnJewelry() {
		// TODO Auto-generated method stub
		
	}
}
