
package DemoWebShopPositiveScripts;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import DemoWebShopBase.BaseClass;
import GenericUtilites.ScreenShotPage;


public class RegisterNegativeScriptTest extends BaseClass {

    // REG_17 - Leave confirmation password empty
    @Test(groups = {"functional"})
    public void REG_17_ConfirmPasswordEmpty() {

        driver.findElement(By.className("ico-register")).click();
        driver.findElement(By.id("gender-male")).click();
        driver.findElement(By.id("FirstName")).sendKeys("Rashmi");
        driver.findElement(By.id("LastName")).sendKeys("GN");

        driver.findElement(By.id("Email"))
                .sendKeys("rashmi" + System.currentTimeMillis() + "@example.com");

        driver.findElement(By.id("Password")).sendKeys("Test@123");

        driver.findElement(By.id("register-button")).click();

        Assert.assertTrue(
                driver.findElement(By.tagName("body")).getText()
                        .contains("Password is required."),
                "Confirmation-password validation should be displayed");
        
    }
    

    // REG_14 - Enter an existing registered email
    @Test(groups = {"integration"})
    public void REG_14_DuplicateEmail() {

        driver.findElement(By.className("ico-register")).click();
        driver.findElement(By.id("gender-male")).click();
        driver.findElement(By.id("FirstName")).sendKeys("Rashmi");
        driver.findElement(By.id("LastName")).sendKeys("GN");

        driver.findElement(By.id("Email")).sendKeys("Anush12@gmail.com");

        driver.findElement(By.id("Password")).sendKeys("Test@123");
        driver.findElement(By.id("ConfirmPassword")).sendKeys("Test@123");

        driver.findElement(By.id("register-button")).click();

        Assert.assertTrue(
                driver.findElement(By.tagName("body")).getText().toLowerCase()
                        .contains("the specified email already exists"),
                "Duplicate-email validation should be displayed");
    }

    // REG_15 - Password and confirmation do not match
    @Test(groups = {"system"})
    public void REG_15_PasswordMismatch() throws IOException {

        driver.findElement(By.className("ico-register")).click();
        driver.findElement(By.id("gender-male")).click();
        driver.findElement(By.id("FirstName")).sendKeys("Rashmi");
        driver.findElement(By.id("LastName")).sendKeys("GN");

        driver.findElement(By.id("Email"))
                .sendKeys("rashmi" + System.currentTimeMillis() + "@example.com");

        driver.findElement(By.id("Password")).sendKeys("Test@123");
        driver.findElement(By.id("ConfirmPassword")).sendKeys("Different@123");

        driver.findElement(By.id("register-button")).click();

        Assert.assertTrue(
                driver.findElement(By.tagName("body")).getText()
                        .contains("The password and confirmation password do not match."),
                "Password-mismatch validation should be displayed");
        ScreenShotPage.TakeScreenShot(driver, "REG_15_PasswordMismatch");
    }

    // SMOKE-REG-01 - Verify successful registration
    @Test(groups = {"smoke"})
    public void verifyValidRegistration() throws IOException {

        driver.findElement(By.className("ico-register")).click();
        driver.findElement(By.id("gender-male")).click();
        driver.findElement(By.id("FirstName")).sendKeys("Rashmi");
        driver.findElement(By.id("LastName")).sendKeys("GN");

        driver.findElement(By.id("Email"))
                .sendKeys("rashmi" + System.currentTimeMillis() + "@example.com");

        driver.findElement(By.id("Password")).sendKeys("Test@123");
        driver.findElement(By.id("ConfirmPassword")).sendKeys("Test@123");

        driver.findElement(By.id("register-button")).click();

        Assert.assertTrue(
                driver.findElement(By.tagName("body")).getText()
                        .contains("Your registration completed"),
                "Registration should be successful");
        ScreenShotPage.TakeScreenShot(driver,"Verify successful registration");
    }
}
