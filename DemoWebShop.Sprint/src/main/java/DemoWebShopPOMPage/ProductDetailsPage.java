package DemoWebShopPOMPage;
	import java.util.List;
	import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.support.ui.Select;

	public class ProductDetailsPage {

	    WebDriver driver;

	    By productTitle = By.cssSelector(".product-name h1");

	    By lengthInput = By.cssSelector(
	        ".attributes input[type='text']"
	    );

	    By dropdowns = By.cssSelector(".attributes select");

	    By radios = By.cssSelector(
	        ".attributes input[type='radio']"
	    );

	    By addToWishlist = By.id("add-to-wishlist-button-71");

	    By addToCart = By.id("add-to-cart-button-71");

	    public ProductDetailsPage(WebDriver driver) {
	        this.driver = driver;
	    }

	    public boolean isCorrectProductDisplayed() {
	        return driver.findElements(productTitle).size() > 0
	            && driver.findElement(productTitle).getText()
	                .toLowerCase().contains("create your own jewelry");
	    }

	    public void selectMaterial() {
	        List<WebElement> options = driver.findElements(dropdowns);

	        if (!options.isEmpty()) {
	            Select material = new Select(options.get(0));

	            if (material.getOptions().size() > 1) {
	                material.selectByIndex(1);
	            }
	        }
	    }

	    public void enterLength(String length) {
	        List<WebElement> fields = driver.findElements(lengthInput);

	        if (!fields.isEmpty()) {
	            fields.get(0).clear();
	            fields.get(0).sendKeys(length);
	        }
	    }

	    public void selectPendant() {
	        List<WebElement> options = driver.findElements(dropdowns);

	        if (options.size() > 1) {
	            Select pendant = new Select(options.get(1));

	            for (WebElement option : pendant.getOptions()) {
	                if (option.getText().trim()
	                        .equalsIgnoreCase("Heart")) {
	                    pendant.selectByVisibleText(option.getText());
	                    return;
	                }
	            }
	        }

	        List<WebElement> radioOptions = driver.findElements(radios);

	        for (WebElement option : radioOptions) {
	            if (option.isEnabled()) {
	                option.click();
	                break;
	            }
	        }
	    }

	    public void configureJewelry(String length) {
	        selectMaterial();
	        enterLength(length);
	        selectPendant();
	    }

	    public void clickAddToWishlist() {
	        driver.findElement(addToWishlist).click();
	    }

	    public void clickAddToCart() {
	        driver.findElement(addToCart).click();
	    }
	}


