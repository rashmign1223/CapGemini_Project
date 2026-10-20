
package DemoWebShopPOMPage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WishlistPage {

    private WebDriver driver;

    private static final String WISHLIST_URL =
            "https://demowebshop.tricentis.com/wishlist";

    private By wishlistContent =
            By.cssSelector(".wishlist-content");

    private By wishlistCheckboxes =
            By.name("addtocart");

    private By wishlistLink =
            By.cssSelector("a.ico-wishlist");

    public WishlistPage(WebDriver driver) {
        this.driver = driver;
    }

    // Open wishlist directly
    public void openWishlistPage() {

        try {
            driver.navigate().to(WISHLIST_URL);
            Thread.sleep(1500);

            String currentUrl = driver.getCurrentUrl();

            // Fallback: use a freshly located wishlist link
            if (!currentUrl.contains("/wishlist")) {

                List<WebElement> links =
                        driver.findElements(wishlistLink);

                if (!links.isEmpty() && links.get(0).isDisplayed()) {
                    links.get(0).click();
                    Thread.sleep(1500);
                }
            }

            currentUrl = driver.getCurrentUrl();

            if (!currentUrl.contains("/wishlist")) {
                throw new IllegalStateException(
                        "Wishlist page did not open. Current URL: "
                        + currentUrl
                );
            }

            System.out.println(
                    "PASS: Wishlist page opened successfully."
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Interrupted while opening wishlist page.",
                    e
            );
        }
    }

    // Verify wishlist URL
    public boolean isWishlistDisplayed() {
        return driver.getCurrentUrl().contains("/wishlist");
    }

    // Verify wishlist content exists
    public boolean isWishlistContentDisplayed() {

        List<WebElement> elements =
                driver.findElements(wishlistContent);

        return !elements.isEmpty()
                && elements.get(0).isDisplayed();
    }

    // Get current wishlist product checkboxes
    public List<WebElement> getWishlistCheckboxes() {
        return driver.findElements(wishlistCheckboxes);
    }

    // Check whether wishlist contains products
    public boolean isWishlistEmpty() {
        return getWishlistCheckboxes().isEmpty();
    }

    // Unselect all wishlist products
    public void unselectAllProducts() {

        List<WebElement> checkboxes =
                driver.findElements(wishlistCheckboxes);

        for (int i = 0; i < checkboxes.size(); i++) {

            // Re-find elements after each interaction
            List<WebElement> currentCheckboxes =
                    driver.findElements(wishlistCheckboxes);

            if (i < currentCheckboxes.size()) {
                WebElement checkbox = currentCheckboxes.get(i);

                if (checkbox.isSelected()) {
                    checkbox.click();
                }
            }
        }
    }

    // Verify every wishlist checkbox is unselected
    public boolean areAllProductsUnselected() {

        List<WebElement> checkboxes =
                driver.findElements(wishlistCheckboxes);

        for (WebElement checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                return false;
            }
        }

        return true;
    }
}
