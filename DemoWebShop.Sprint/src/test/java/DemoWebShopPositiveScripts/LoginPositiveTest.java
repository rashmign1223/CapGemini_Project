
package DemoWebShopPositiveScripts;

import org.testng.Assert;
import org.testng.annotations.Test;

import DemoWebShopBase.BaseClass;

public class LoginPositiveTest extends BaseClass {

    @Test(groups = {
        "functional",
        "integration",
        "smoke",
        "system",
        "positive"
    })
    public void loginWithRegisteredCredentials() {

        System.out.println("Login test started");
        System.out.println("Login email: " + username);

        // Login using credentials loaded from Excel by BaseClass
        loginToApplication();

        // Verify successful login
        Assert.assertTrue(
            login.isLoginSuccessful(),
            "Login failed for " + username
        );

        System.out.println(
            "Login successful using Excel credentials: " + username
        );
    }
}
