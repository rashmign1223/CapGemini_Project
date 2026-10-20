
package DemoWebShopPOMPage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchPage {

    WebDriver driver;

    // Search keyword
    private By searchBox = By.id("small-searchterms");

    // Search button
    private By searchButton = By.xpath("//input[@value='Search']");

    // Create Your Own Jewelry - product title link
    private By jewelryProduct = By.xpath(
        "//h2[contains(@class,'product-title')]" +
        "/a[normalize-space()='Create Your Own Jewelry']"
    );

    // Product image
    private By jewelryImage = By.xpath(
        "//div[contains(@class,'product-item')]" +
        "[.//h2[contains(@class,'product-title')]" +
        "/a[normalize-space()='Create Your Own Jewelry']]" +
        "//div[contains(@class,'picture')]//img"
    );

    // Product price
    private By jewelryPrice = By.xpath(
        "//div[contains(@class,'product-item')]" +
        "[.//h2[contains(@class,'product-title')]" +
        "/a[normalize-space()='Create Your Own Jewelry']]" +
        "//span[contains(@class,'price')]"
    );

    // Product rating
    private By jewelryRating = By.xpath(
        "//div[contains(@class,'product-item')]" +
        "[.//h2[contains(@class,'product-title')]" +
        "/a[normalize-space()='Create Your Own Jewelry']]" +
        "//*[contains(@class,'rating')]"
    );

    // Sort By
    private By sortDropdown =
            By.className("product-sorting");

    // Display
    private By displayDropdown =
            By.className("product-page-size");

    // View As
    private By viewAsDropdown =
            By.className("product-viewmode");

    // Advanced Search
    private By advancedSearch =
            By.id("As");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }

    // Enter product name in search box
    public void enterSearch(String product) {

        driver.findElement(searchBox).clear();
        driver.findElement(searchBox).sendKeys(product);
    }

    // Click Search button
    public void clickSearch() {

        driver.findElement(searchButton).click();
    }

    // Search for a product
    public void searchProduct(String product) {

        enterSearch(product);
        clickSearch();
    }

    // Verify jewelry product is displayed
    public boolean isJewelryProductDisplayed() {

        List<WebElement> products =
                driver.findElements(jewelryProduct);

        for (WebElement product : products) {

            if (product.isDisplayed()) {
                return true;
            }
        }

        return false;
    }

    // Get jewelry product name
    public String getJewelryProductName() {

        for (WebElement product :
                driver.findElements(jewelryProduct)) {

            if (product.isDisplayed()) {
                return product.getText().trim();
            }
        }

        throw new NoSuchElementException(
            "Visible Create Your Own Jewelry product was not found"
        );
    }

    // Get jewelry price
    public String getJewelryPrice() {

        for (WebElement price :
                driver.findElements(jewelryPrice)) {

            if (price.isDisplayed()) {
                return price.getText().trim();
            }
        }

        throw new NoSuchElementException(
            "Jewelry product price was not found"
        );
    }

    // Verify jewelry image is displayed
    public boolean isJewelryImageDisplayed() {

        for (WebElement image :
                driver.findElements(jewelryImage)) {

            if (image.isDisplayed()) {
                return true;
            }
        }

        return false;
    }

    // Verify product rating is displayed
    public boolean isRatingDisplayed() {

        for (WebElement rating :
                driver.findElements(jewelryRating)) {

            if (rating.isDisplayed()) {
                return true;
            }
        }

        return false;
    }

    // Click the visible jewelry product



public void clickJewelryProduct() {

    By productLocator = By.xpath(
        "//h2[contains(@class,'product-title')]" +
        "/a[normalize-space()='Create Your Own Jewelry']"
    );

    List<WebElement> products =
            driver.findElements(productLocator);

    boolean clicked = false;

    for (int i = 0; i < products.size(); i++) {

        WebElement product = products.get(i);

        if (product.isDisplayed() && product.isEnabled()) {

            // Scroll the product into view
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                product
            );

            // Re-locate the element because the page may have changed
            List<WebElement> refreshedProducts =
                    driver.findElements(productLocator);

            for (WebElement refreshedProduct : refreshedProducts) {

                if (refreshedProduct.isDisplayed()
                        && refreshedProduct.isEnabled()) {

                    refreshedProduct.click();
                    clicked = true;
                    break;
                }
            }

            break;
        }
    }

    if (!clicked) {
        throw new NoSuchElementException(
            "Unable to click the visible Create Your Own Jewelry product"
        );
    }

    System.out.println("Jewelry product clicked successfully.");
}



    // Select sorting option
    public void selectSortOption(String option) {

        driver.findElement(sortDropdown).sendKeys(option);
    }

    // Select number of products displayed
    public void selectDisplayOption(String option) {

        driver.findElement(displayDropdown).sendKeys(option);
    }

    // Select grid or list view
    public void selectViewOption(String option) {

        driver.findElement(viewAsDropdown).sendKeys(option);
    }

    // Click Advanced Search
    public void clickAdvancedSearch() {

        driver.findElement(advancedSearch).click();
    }
}
