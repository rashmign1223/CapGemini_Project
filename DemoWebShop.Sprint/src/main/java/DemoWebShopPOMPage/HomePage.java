
package DemoWebShopPOMPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;

public class HomePage {

    private WebDriver driver;

    // Search
    private By searchBox = By.id("small-searchterms");
    private By searchButton = By.xpath("//input[@value='Search']");

    // Jewelry category
    private By jewelryLink = By.cssSelector("a[href='/jewelry']");

    // Wishlist
    private By wishlistLink =
            By.xpath("//a[contains(@href,'/wishlist')]");

    // Shopping Cart
    private By shoppingCartLink =
            By.xpath("//a[contains(@href,'/cart')]");

    // Logout
    private By logoutLink =
            By.xpath("//a[contains(@href,'/logout')]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Open Jewelry category
    public void clickJewelry() {
        By jewelryLink = By.cssSelector("a[href='/jewelry']");

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                Thread.sleep(1000);

                WebElement jewelry = driver.findElement(jewelryLink);

                if (jewelry.isDisplayed() && jewelry.isEnabled()) {
                    jewelry.click();
                    Thread.sleep(1500);

                    System.out.println("PASS | Jewelry category opened");
                    return;
                }

            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println(
                    "Retrying Jewelry click, attempt: " + attempt
                );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(
                    "Interrupted while opening Jewelry", e
                );
            }
        }

        throw new IllegalStateException(
            "Unable to click Jewelry category after 3 attempts."
        );
    }
    // Enter product name - handles stale elements
    public void enterSearchProduct(String productName) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                WebElement search = driver.findElement(searchBox);
                search.clear();
                search.sendKeys(productName);
                return;
            } catch (StaleElementReferenceException e) {
                if (attempt == 3) {
                    throw e;
                }
            }
        }
    }

    // Click Search
    public void clickSearch() {
        driver.findElement(searchButton).click();
    }

    // Search product in one method
    public void searchProduct(String productName) {
        enterSearchProduct(productName);
        clickSearch();
    }

    // Open Wishlist
    public void clickWishlist() {
        driver.findElement(wishlistLink).click();
    }

    // Open Shopping Cart
    public void clickShoppingCart() {
        driver.findElement(shoppingCartLink).click();
    }

    // Logout
    public void clickLogout() {
        driver.findElement(logoutLink).click();
    }

    // Verify home page
    public boolean isHomePageDisplayed() {
        return driver.getTitle().contains("Demo Web Shop");
    }
}
