package com.petclinic.selenium.seleniumbillingservicetest;

import com.petclinic.selenium.SeleniumLoginTestHelper;
import io.github.bonigarcia.seljup.SeleniumExtension;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@ExtendWith(SeleniumExtension.class)
public class SeleniumBillingServiceTest {
    ChromeDriver driver;
    SeleniumLoginTestHelper helper;
    private final String SCREENSHOTS = "./src/test/onDemandBillServiceScreenshots";

    public SeleniumBillingServiceTest(ChromeDriver driver) {
        this.driver = driver;

        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        System.setProperty("sel.jup.screenshot.at.the.end.of.tests", "whenfailure");
        System.setProperty("sel.jup.screenshot.format", "png");
        System.setProperty("sel.jup.output.folder", "./src/test/onFailureScreenshots");
    }

    public static void takeSnapShot(WebDriver webDriver, String fileWithPath) throws Exception {

        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) webDriver);
        //call getScreenshotAs method to create the actual image file
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        //Move image file to new destination
        File DestFile = new File(fileWithPath);
        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
    }

    @BeforeEach
    void setupLogin() throws Exception {
        this.helper = new SeleniumLoginTestHelper("LoginTestHelper", driver);
        helper.loginTest();
    }

    @Test
    @DisplayName("Test to see if the history page loads")
    public void takeBillingServiceSnapshot(TestInfo testInfo) throws Exception {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement billsTab = helper.getDriver().findElement(By.linkText("Bills"));
        billsTab.click();

        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BillHistoryTitle")));

        WebElement billHistoryHeader = helper.getDriver().findElement(By.id("BillHistoryTitle"));

        String method = testInfo.getDisplayName();
        takeSnapShot(helper.getDriver(), SCREENSHOTS + "\\" + method + "_" + System.currentTimeMillis() + ".png");

        assertThat(billHistoryHeader.getText(), is("Bill History"));

        TimeUnit.SECONDS.sleep(1);

        helper.getDriver().quit();
    }

    @Test
    @DisplayName("Take a snapshot of bill details page")
    public void takeBillingServiceDetailsPageSnapshot(TestInfo testInfo) throws Exception {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String method = testInfo.getDisplayName();
        WebElement billsTab = helper.getDriver().findElement(By.linkText("Bills"));
        billsTab.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bill")));
        takeSnapShot(helper.getDriver(), SCREENSHOTS + "\\Take a snapshot to get the table data_" + System.currentTimeMillis() + ".png");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bill")));
        WebElement billDetailsLink = helper.getDriver().findElement(By.linkText("Get Details"));
        billDetailsLink.click();

        WebDriverWait waitDetails = new WebDriverWait(driver, 2);
        waitDetails.until(ExpectedConditions.visibilityOfElementLocated(By.id("BillDetailsTitle")));

        WebElement billIDDetail = helper.getDriver().findElement(By.id("BillDetailsTitle"));

        takeSnapShot(helper.getDriver(), SCREENSHOTS + "\\" + method + "_" + System.currentTimeMillis() + ".png");

        TimeUnit.SECONDS.sleep(1);

        assertThat(billIDDetail, not("Bill Details: "));

        helper.getDriver().quit();

    }

    @Test
    @DisplayName("Take a snapshot after search bar")
    public void takeBillingServiceHistoryPageSearchBarSnapShot() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement billsTab = helper.getDriver().findElement(By.linkText("Bills"));
        billsTab.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String id = helper.getDriver().findElement(By.id("BillIdColumn")).getText();
        helper.getDriver().findElement(By.xpath("//*[@id=\"bg\"]/div/div/div/ui-view/bill-history/form/div/input")).sendKeys(id.substring(1, 3));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String amount = helper.getDriver().findElement(By.xpath("//*[@id=\"billId\"]/td[4]")).getText();

        assertThat(amount, is("59.99"));

        helper.getDriver().quit();
    }

    @Test
    @DisplayName("Test a snapshot to delete the bill")
    public void takeBillingServiceDelete(TestInfo testInfo) throws Exception {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement billsTab = helper.getDriver().findElement(By.linkText("Bills"));
        billsTab.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement billHistoryLink = helper.getDriver().findElement(By.xpath("//a[@href='#!/bills']"));
        billHistoryLink.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='table table-striped']")));

        WebElement table = helper.getDriver().findElement(By.xpath("//table[@class='table table-striped']"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        List<WebElement> buttons = driver.findElements(By.xpath("//*[@class='btn btn-danger']"));
        buttons.get(0).click();
        driver.switchTo().alert().accept();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        String method = testInfo.getDisplayName();
        takeSnapShot(helper.getDriver(), SCREENSHOTS + "\\" + method + "_" + System.currentTimeMillis() + ".png");


        TimeUnit.SECONDS.sleep(1);

        helper.getDriver().quit();
    }

    @Test
    @DisplayName("Take a snapshot of information hover")
    public void takeBillingServiceInfoHoverSnapshot(TestInfo testInfo) throws Exception {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement billsTab = helper.getDriver().findElement(By.linkText("Bills"));
        billsTab.click();

        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ownerName")));

        WebElement ownerName = helper.getDriver().findElement(By.id("ownerName"));

        Actions actions = new Actions(driver);
        actions.moveToElement(ownerName).perform();

        String method = testInfo.getDisplayName();
        takeSnapShot(helper.getDriver(), SCREENSHOTS + "\\" + method + "_" + System.currentTimeMillis() + ".png");
    }

    @Test
    @DisplayName("Take a snapshot of different sorting modes")
    public void takeBillingServiceSoringSnapshots(TestInfo testInfo) throws Exception {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //navigate to bills page
        WebElement billsTab = helper.getDriver().findElement(By.linkText("Bills"));
        billsTab.click();

        //wait for sorting button to render & retrieve it
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sortTestDefault1")));

        WebElement sortButton = helper.getDriver().findElement(By.id("sortTestDefault1"));

        //wait until page has rendered data
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Get Details")));

        //click sort button & take screenshot
        sortButton.click();
        String method = testInfo.getDisplayName();
        takeSnapShot(helper.getDriver(), SCREENSHOTS + "\\" + method + "_" + System.currentTimeMillis() + "-1.png");

        //get new sort button, click it & take screenshot
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sortTestUp1")));
        sortButton = helper.getDriver().findElement(By.id("sortTestUp1"));
        sortButton.click();
        takeSnapShot(helper.getDriver(), SCREENSHOTS + "\\" + method + "_" + System.currentTimeMillis() + "-2.png");
    }
}
