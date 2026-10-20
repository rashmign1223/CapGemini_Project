
package DemoWebShopPOMPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage {

    private WebDriver driver;

    // Locators
    @FindBy(className = "ico-register")
    private WebElement registerLink;

    @FindBy(id = "gender-male")
    private WebElement genderMale;

    @FindBy(id = "gender-female")
    private WebElement genderFemale;

    @FindBy(id = "FirstName")
    private WebElement firstName;

    @FindBy(id = "LastName")
    private WebElement lastName;

    @FindBy(id = "Email")
    private WebElement email;

    @FindBy(id = "Password")
    private WebElement password;

    @FindBy(id = "ConfirmPassword")
    private WebElement confirmPassword;

    @FindBy(id = "register-button")
    private WebElement registerButton;

    @FindBy(className = "result")
    private WebElement successMessage;

    @FindBy(css = "input.register-continue-button")
    private WebElement continueButton;

    // Constructor
    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Generate a unique email
    public String generateUniqueEmail() {
        return "rashmi" + System.currentTimeMillis()
                + "@gmail.com";
    }

    // Open registration page
    public void openRegisterPage() {
        driver.findElement(By.className("ico-register")).click();
    }

    // Select gender
    public void selectGender(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            genderMale.click();
        } else if (gender.equalsIgnoreCase("female")) {
            genderFemale.click();
        } else {
            throw new IllegalArgumentException(
                "Gender must be male or female"
            );
        }
    }

    // Enter registration details
    public void enterFirstName(String fname) {
        firstName.sendKeys(fname);
    }

    public void enterLastName(String lname) {
        lastName.sendKeys(lname);
    }

    public void enterEmail(String emailAddress) {
        email.sendKeys(emailAddress);
    }

    public void enterPassword(String pwd) {
        password.sendKeys(pwd);
    }

    public void enterConfirmPassword(String confirmPwd) {
        confirmPassword.sendKeys(confirmPwd);
    }

    // Click Register
    public void clickRegister() {
        registerButton.click();
    }

    // Complete registration workflow
    public void registerUser(
            String gender,
            String fname,
            String lname,
            String emailAddress,
            String pwd,
            String confirmPwd) {

        openRegisterPage();
        selectGender(gender);
        enterFirstName(fname);
        enterLastName(lname);
        enterEmail(emailAddress);
        enterPassword(pwd);
        enterConfirmPassword(confirmPwd);
        clickRegister();
    }

    // Verify successful registration
    public boolean isRegistrationSuccessful() {
        return !driver.findElements(By.className("result")).isEmpty()
            && successMessage.isDisplayed()
            && successMessage.getText()
                .contains("Your registration completed");
    }

    // Get registration message
    public String getRegistrationMessage() {
        return successMessage.getText();
    }

    // Continue after registration
    public void clickContinue() {
        continueButton.click();
    }

    // Verify gender fields
    public boolean isGenderDisplayed() {
        return genderMale.isDisplayed()
            && genderFemale.isDisplayed();
    }

    // Verify registration form fields
    public boolean isRegisterFormDisplayed() {
        return firstName.isDisplayed()
            && lastName.isDisplayed()
            && email.isDisplayed()
            && password.isDisplayed()
            && confirmPassword.isDisplayed()
            && registerButton.isDisplayed();
    }

public void registerCustomer(String string, String string2, String email2, String string3) {
	// TODO Auto-generated method stub
	
}
}

