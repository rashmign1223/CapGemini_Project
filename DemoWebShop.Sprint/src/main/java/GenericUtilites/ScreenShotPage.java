package GenericUtilites;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class ScreenShotPage {

    public static void TakeScreenShot(WebDriver Driver, String screenshotName)
            throws IOException {

        TakesScreenshot tks = (TakesScreenshot) Driver;

        File scr = tks.getScreenshotAs(OutputType.FILE);

        File folder = new File("./ss");

        // Create the folder automatically
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File dest = new File(folder, screenshotName + ".png");

        FileHandler.copy(scr, dest);

        System.out.println("Screenshot saved: " + dest.getAbsolutePath());
    }
}

