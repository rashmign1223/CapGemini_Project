
package DemoWebShopPositiveScripts;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import DemoWebShopBase.BaseClass;
import DemoWebShopPOMPage.RegisterPage;
import GenericUtilites.ExcelUtility;

public class RegisterScriptTest extends BaseClass {

    @DataProvider(name = "registrationData")
    public Object[][] getRegistrationData() {
        return ExcelUtility.getDataArray("Sheet1");
    }

    @Test(
        dataProvider = "registrationData",
        groups = {"functional", "integration", "smoke", "system", "positive"}
    )
    public void registrationTest(
            String testCaseId,
            String gender,
            String firstName,
            String lastName,
            String email,
            String password,
            String confirmPassword,
            String expectedResult) {

        System.out.println("==================================");
        System.out.println("Test Case ID: " + testCaseId);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Excel Email: " + email);
        System.out.println("==================================");

        // Create the RegisterPage object
        RegisterPage rp = new RegisterPage(driver);

        // Generate a new email because the Excel email may already exist
        String uniqueEmail = rp.generateUniqueEmail();

        System.out.println("Registration Email Used: " + uniqueEmail);

        // Open the registration page
        rp.openRegisterPage();

        // Register using the unique email
        rp.registerUser(
                gender,
                firstName,
                lastName,
                uniqueEmail,
                password,
                confirmPassword
        );

        // Verify registration
        Assert.assertTrue(
                rp.isRegistrationSuccessful(),
                "Registration failed for " + uniqueEmail
        );

        System.out.println(
                testCaseId + " - Registration successful for " + uniqueEmail
        );
    }
}
