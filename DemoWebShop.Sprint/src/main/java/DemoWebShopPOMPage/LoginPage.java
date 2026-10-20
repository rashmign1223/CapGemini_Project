
package DemoWebShopPOMPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    private WebDriver driver;

    // Login link on the home page
    @FindBy(css = "a.ico-login")
    private WebElement loginLink;

    // Login form fields
    @FindBy(id = "Email")
    private WebElement email;

    @FindBy(id = "Password")
    private WebElement password;

    // Remember me checkbox
    @FindBy(id = "RememberMe")
    private WebElement rememberMe;

    // Login button
    @FindBy(css = "input.button-1.login-button")
    private WebElement loginButton;

    // Logout link displayed after successful login
    @FindBy(css = "a.ico-logout")
    private WebElement logoutLink;

    // Login validation message
    @FindBy(css = ".validation-summary-errors")
    private WebElement loginErrorMessage;

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Click the Login link
    public void clickLoginLink() {
        loginLink.click();
    }

    // Enter email
    public void enterEmail(String emailAddress) {
        email.clear();
        email.sendKeys(emailAddress);
    }

    // Enter password
    public void enterPassword(String passwordValue) {
        password.clear();
        password.sendKeys(passwordValue);
    }

    // Select Remember Me
    public void selectRememberMe() {
        if (!rememberMe.isSelected()) {
            rememberMe.click();
        }
    }

    // Click Login button
    public void clickLoginButton() {
        loginButton.click();
    }

    // Login using email and password
    public void login(String emailAddress, String passwordValue) {
        enterEmail(emailAddress);
        enterPassword(passwordValue);
        clickLoginButton();
    }

    // Login with Remember Me selected
    public void loginWithRememberMe(
            String emailAddress,
            String passwordValue) {

        enterEmail(emailAddress);
        enterPassword(passwordValue);
        selectRememberMe();
        clickLoginButton();
    }

    // Verify successful login
    public boolean isLoginSuccessful() {
        try {
            return !driver.findElements(
                    org.openqa.selenium.By.cssSelector("a.ico-logout")
            ).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // Verify Logout link is displayed
    public boolean isLogoutDisplayed() {
        return isLoginSuccessful();
    }

    // Verify login error message is displayed
    public boolean isLoginErrorDisplayed() {
        try {
            return !driver.findElements(
                    org.openqa.selenium.By.cssSelector(
                            ".validation-summary-errors"
                    )
            ).isEmpty()
                    && loginErrorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Get login error message
    public String getLoginErrorMessage() {
        try {
            if (isLoginErrorDisplayed()) {
                return loginErrorMessage.getText();
            }
        } catch (Exception e) {
            System.out.println("Unable to read login error message.");
        }

        return "";
    }

    // Get current page URL
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }


public void loginToApplication(String username, String password) {
    clickLoginLink();
    enterEmail(username);
    enterPassword(password);
    clickLoginButton();
}
}
