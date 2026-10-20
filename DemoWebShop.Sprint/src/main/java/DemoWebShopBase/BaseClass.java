
package DemoWebShopBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import DemoWebShopPOMPage.LoginPage;
import DemoWebShopPOMPage.RegisterPage;
import DemoWebShopPOMPage.HomePage;
import DemoWebShopPOMPage.ProductPage;
import DemoWebShopPOMPage.CartPage;
import DemoWebShopPOMPage.CheckoutPage;
import DemoWebShopPOMPage.SearchPage;
import DemoWebShopPOMPage.WishlistPage;

import GenericUtilites.ExcelUtility;

public class BaseClass {

    // WebDriver
    protected WebDriver driver;

    // Page Objects
    protected LoginPage login;
    protected RegisterPage register;
    protected HomePage home;
    protected ProductPage product;
    protected CartPage cart;
    protected CheckoutPage checkout;
    protected SearchPage search;
    protected WishlistPage wishlist;

    // Test data
    protected String url;
    protected String username;
    protected String password;
    protected String couponCode;

    protected Properties p;

    // Browser setup and test data loading
    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void BM(@Optional("chrome") String browser)
            throws IOException {

        // Load common properties
        p = new Properties();

        try (FileInputStream fis = new FileInputStream(
                "./src/main/resources/commondata.properties")) {
            p.load(fis);
        }

        // Read application URL from properties
        url = p.getProperty("url");

        // Read registered credentials from Excel Sheet1
        username = ExcelUtility.readDataFromExcel(
                "Sheet1", 1, 4);

        password = ExcelUtility.readDataFromExcel(
                "Sheet1", 1, 5);

        couponCode = p.getProperty("couponCode", "");

        // Validate test data
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "URL is missing in commondata.properties");
        }

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Email is missing in Excel Sheet1, row 2");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Password is missing in Excel Sheet1, row 2");
        }

        // Launch browser
        if (browser.equalsIgnoreCase("chrome")) {

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");

            driver = new ChromeDriver(options);

        } else if (browser.equalsIgnoreCase("firefox")) {

            driver = new FirefoxDriver();

        } else {

            throw new IllegalArgumentException(
                    "Unsupported browser: " + browser);
        }

        // Browser configuration
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(10));

        // Open application with retry
        openApplicationWithRetry();

        // Initialize Page Objects
        login = new LoginPage(driver);
        register = new RegisterPage(driver);
        home = new HomePage(driver);
        product = new ProductPage(driver);
        cart = new CartPage(driver);
        checkout = new CheckoutPage(driver);
        search = new SearchPage(driver);
        wishlist = new WishlistPage(driver);

        System.out.println("==================================");
        System.out.println("Browser launched: " + browser);
        System.out.println("Application opened: " + driver.getCurrentUrl());
        System.out.println("Login credentials loaded from Excel.");
        System.out.println("==================================");
    }

    // Open application with retry
    private void openApplicationWithRetry() {

        int attempts = 3;

        for (int i = 1; i <= attempts; i++) {

            try {
                System.out.println(
                        "Opening application - attempt " + i);

                driver.navigate().to(url);

                if (driver.getCurrentUrl().startsWith(
                        "https://demowebshop.tricentis.com")) {

                    System.out.println(
                            "Application opened successfully.");
                    return;
                }

                System.out.println(
                        "Unexpected URL: " + driver.getCurrentUrl());

            } catch (WebDriverException e) {

                System.out.println(
                        "Navigation attempt " + i + " failed: "
                        + e.getMessage());
            }

            // Wait before retrying
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                throw new RuntimeException(
                        "Interrupted before retrying navigation.", e);
            }
        }

        throw new RuntimeException(
                "Could not open " + url + " after "
                + attempts
                + " attempts. Check your internet connection "
                + "and whether the website is accessible.");
    }

    // Reusable login method for all modules
    protected void loginToApplication() {

        driver.get(url + "login");

        login.login(username, password);

        System.out.println(
                "Logged in using Excel account: " + username);
    }

    // Navigate to Shopping Cart
    protected void navigateToCart() {

        driver.get(url + "cart");
    }

    // Browser cleanup after every test method
    @AfterMethod(alwaysRun = true)
    public void AM() {

        if (driver != null) {
            driver.quit();
            driver = null;
        }

        // Clear Page Object references
        login = null;
        register = null;
        home = null;
        product = null;
        cart = null;
        checkout = null;
        search = null;
        wishlist = null;
    }
}
