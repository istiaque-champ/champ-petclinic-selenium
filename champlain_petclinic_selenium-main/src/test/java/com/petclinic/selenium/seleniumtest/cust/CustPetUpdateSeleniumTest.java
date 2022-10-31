package com.petclinic.selenium.seleniumtest.cust;

import com.petclinic.selenium.SeleniumLoginTestHelper;
import io.github.bonigarcia.seljup.SeleniumExtension;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SeleniumExtension.class)
public class CustPetUpdateSeleniumTest {

    ChromeDriver driver;
    SeleniumLoginTestHelper loginHelper;
    private final String SCREENSHOTS = "./src/test/onDemandCustServiceScreenshots/OwnerUpdate";

    public CustPetUpdateSeleniumTest(ChromeDriver driver) {
        this.driver = driver;

        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        System.setProperty("sel.jup.screenshot.at.the.end.of.tests", "whenfailure");
        System.setProperty("sel.jup.screenshot.format", "png");
        System.setProperty("sel.jup.output.folder", "./src/test/onFailureCustServiceScreenshots");
    }

    public static void takeSnapShot(WebDriver webDriver, String fileWithPath) throws Exception {
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) webDriver);
        //call get Screenshot method to create actual image file
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        //Move image file to new destination
        File DestFile = new File(fileWithPath);
        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
    }

    @BeforeEach
    public void setup() throws Exception {
        this.loginHelper = new SeleniumLoginTestHelper("Cust", driver);
        loginHelper.loginTest();
    }

    @Test
    @DisplayName("Test_Cust_UpdateOwner")
    public void testCustUpdatePet() throws Exception {
        boolean error = false, error2 = false, error3 = false;

        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.urlToBe("http://localhost:8080/#!/welcome"));
            loginHelper.getDriver().get("http://localhost:8080/#!/owners");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='#!/pets/1/1']")));
            loginHelper.getDriver().findElement(By.xpath("//a[@href='#!/pets/1/1']")).click();
            wait.until(ExpectedConditions.urlToBe("http://localhost:8080/#!/pets/1/1"));
            Thread.sleep(1000);

            WebElement initialFirstName = loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petName\"]"));
            assertThat(initialFirstName.getAttribute("value"), is("Leo"));
            WebElement initialLastName = loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petBDay\"]"));
            assertThat(initialLastName.getAttribute("value"), is("2000-09-07"));
            WebElement initialAddress = loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petTypeName\"]"));
            assertThat(initialAddress.getAttribute("value"), is("cat"));
        } catch (AssertionError e) {
            e.printStackTrace();
            error = true;
            throw new AssertionError(e);
        } finally {
            if (!error) {
                takeSnapShot(driver, SCREENSHOTS + "/pass/checkInitialOwnerFields_" + System.currentTimeMillis() + ".png");
            } else {
                takeSnapShot(driver, SCREENSHOTS + "/fail/checkInitialOwnerFields_" + System.currentTimeMillis() + ".png");
            }
        }

        try {
            loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petName\"]")).sendKeys("-Updated");
            loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petBDay\"]")).clear();
            loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petBDay\"]")).sendKeys("2001-10-08");
            loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petTypeName\"]")).clear();
            loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petTypeName\"]")).sendKeys("cat");
            loginHelper.getDriver().findElement(By.xpath("//*[@type=\"submit\"]")).click();

            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='#!/pets/1/1']")));
            loginHelper.getDriver().findElement(By.xpath("//a[@href='#!/pets/1/1']")).click();
            Thread.sleep(5000);
            WebElement previewName = loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petName\"]"));
            assertThat(previewName.getText(), is("Leo-Updated"));

            WebElement previewAddress = loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petBDay\"]"));
            assertThat(previewAddress.getText(), is("2001-10-08"));

            WebElement previewCity = loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petTypeName\"]"));
            assertThat(previewCity.getText(), is("cat"));
        } catch (AssertionError e2) {
            e2.printStackTrace();
            error2 = true;
            throw new AssertionError(e2);
        } finally {
            if (!error2) {
                takeSnapShot(driver, SCREENSHOTS + "/pass/checkUpdatedFieldsInList_" + System.currentTimeMillis() + ".png");
            } else {
                takeSnapShot(driver, SCREENSHOTS + "/fail/checkUpdatedFieldsInList_" + System.currentTimeMillis() + ".png");
            }
        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            loginHelper.getDriver().navigate().back();
            loginHelper.getDriver().navigate().refresh();
            loginHelper.getDriver().findElement(By.xpath("//a[@href='#!/owners/details/1']")).click();
            wait.until(ExpectedConditions.urlToBe("http://localhost:8080/#!/owners/details/1"));
            Thread.sleep(1000);

            WebElement initialName = loginHelper.getDriver().findElement(By.xpath("//*[@id=\"bg\"]/div/div/div/ui-view/owner-details/table[2]/tbody/tr[1]/td[1]/dl[1]/dd[1]"));
            assertThat(initialName.getAttribute("value"), is("Leo-Updated"));
            WebElement initialDOB = loginHelper.getDriver().findElement(By.xpath("//*[@id=\"bg\"]/div/div/div/ui-view/owner-details/table[2]/tbody/tr[1]/td[1]/dl[1]/dd[2]"));
            assertThat(initialDOB.getAttribute("value"), is("2001-10-08"));
            WebElement initialPetType = loginHelper.getDriver().findElement(By.xpath("//*[@id=\"bg\"]/div/div/div/ui-view/owner-details/table[2]/tbody/tr[1]/td[1]/dl[1]/dd[3]"));
            assertThat(initialPetType.getAttribute("value"), is("cat"));
        } catch (AssertionError e3) {
            e3.printStackTrace();
            error3 = true;
            throw new AssertionError(e3);
        } finally {
            if (!error3) {
                takeSnapShot(driver, SCREENSHOTS + "/pass/checkUpdatedFieldsInDetails_" + System.currentTimeMillis() + ".png");
            } else {
                takeSnapShot(driver, SCREENSHOTS + "/fail/checkUpdatedFieldsInDetails_" + System.currentTimeMillis() + ".png");
            }
        }
        loginHelper.getDriver().quit();
    }
}
