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
            Thread.sleep(5000);

            WebElement initialFirstName = loginHelper.getDriver().findElement(By.xpath("//*[@id=\"petName\"]"));
            assertThat(initialFirstName.getAttribute("value"), is("Leo"));
            WebElement initialLastName = loginHelper.getDriver().findElement(By.xpath("//*[@name=\"petBDay\"]"));
            assertThat(initialLastName.getAttribute("value"), is("2000-09-09"));
            WebElement initialAddress = loginHelper.getDriver().findElement(By.xpath("//*[@name=\"petTypeName\"]"));
            assertThat(initialAddress.getAttribute("value"), is("110 W. Liberty St."));
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
            loginHelper.getDriver().findElement(By.xpath("//*[@name=\"firstName\"]")).sendKeys("-Updated");
            loginHelper.getDriver().findElement(By.xpath("//*[@name=\"lastName\"]")).sendKeys("-Updated");
            loginHelper.getDriver().findElement(By.xpath("//*[@name=\"address\"]")).sendKeys("-Updated");
            loginHelper.getDriver().findElement(By.xpath("//*[@name=\"city\"]")).sendKeys("-Updated");
            loginHelper.getDriver().findElement(By.xpath("//*[@id=\"newBtn\"]")).click();

            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='#!/owners/details/1']")));
            WebElement previewName = loginHelper.getDriver().findElement(By.xpath("//td[contains(., 'George-Updated Franklin-Updated')]"));
            assertThat(previewName.getText(), is("George-Updated Franklin-Updated"));

            WebElement previewAddress = loginHelper.getDriver().findElement(By.xpath("//td[contains(., '110 W. Liberty St.-Updated')]"));
            assertThat(previewAddress.getText(), is("110 W. Liberty St.-Updated"));

            WebElement previewCity = loginHelper.getDriver().findElement(By.xpath("//td[contains(., 'Madison-Updated')]"));
            assertThat(previewCity.getText(), is("Madison-Updated"));
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
            loginHelper.getDriver().findElement(By.xpath("//a[@href='#!/owners/details/1']")).click();
            wait.until(ExpectedConditions.urlToBe("http://localhost:8080/#!/owners/details/1"));
            loginHelper.getDriver().findElement(By.xpath("//*[@id=\"bg\"]/div/div/div/ui-view/owner-details/table[1]/tbody/tr[5]/td[1]/a")).click();
            Thread.sleep(5000);

            WebElement initialFirstName = loginHelper.getDriver().findElement(By.xpath("//*[@name=\"firstName\"]"));
            assertThat(initialFirstName.getAttribute("value"), is("George-Updated"));
            WebElement initialLastName = loginHelper.getDriver().findElement(By.xpath("//*[@name=\"lastName\"]"));
            assertThat(initialLastName.getAttribute("value"), is("Franklin-Updated"));
            WebElement initialAddress = loginHelper.getDriver().findElement(By.xpath("//*[@name=\"address\"]"));
            assertThat(initialAddress.getAttribute("value"), is("110 W. Liberty St.-Updated"));
            WebElement initialCity = loginHelper.getDriver().findElement(By.xpath("//*[@name=\"city\"]"));
            assertThat(initialCity.getAttribute("value"), is("Madison-Updated"));
            WebElement initialPhone = loginHelper.getDriver().findElement(By.xpath("//*[@name=\"telephone\"]"));
            assertThat(initialPhone.getAttribute("value"), is("6085551023"));
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
