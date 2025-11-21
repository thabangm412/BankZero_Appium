package tests.boss;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.boss.BossLoginPage;
import pageObjects.boss.HomeLandingPage;

public class FicaVerificationTests {

    private WebDriver driver;

    @Test
    public void bossLaunchTest() throws InterruptedException {
        WebDriverManager.chromedriver().setup(); // automatically downloads & sets path
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        BossLoginPage bossLoginPage = new BossLoginPage(driver);
        bossLoginPage.goToLink("https://api-qa.bankzerosa.co.za/bank-zero/#/");
        Assert.assertTrue(bossLoginPage.isAt());
        Thread.sleep(3000);
        driver.close();
    }

    @Test
    public void bossLoginTest() throws InterruptedException {
        WebDriverManager.chromedriver().setup(); // automatically downloads & sets path
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        BossLoginPage bossLoginPage = new BossLoginPage(driver);
        HomeLandingPage homeLandingPage = new HomeLandingPage(driver);

        bossLoginPage.goToLink("https://api-qa.bankzerosa.co.za/bank-zero/#/");
        Thread.sleep(3000);
        bossLoginPage.enterBossLoginDetails("thabang-cdd","CNSPass@1602016");
        bossLoginPage.clickBossLogin();
        Assert.assertTrue(homeLandingPage.isAt());
        Thread.sleep(3000);
        homeLandingPage.clickLogoutButton();
    }

    @Test
    public void ficaVerification() throws InterruptedException
    {
        WebDriverManager.chromedriver().setup(); // automatically downloads & sets path
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        BossLoginPage bossLoginPage = new BossLoginPage(driver);
        HomeLandingPage homeLandingPage = new HomeLandingPage(driver);

        try {
            bossLoginPage.goToLink("https://api-qa.bankzerosa.co.za/bank-zero/#/");
            Thread.sleep(3000);
            bossLoginPage.enterBossLoginDetails("thabang-cdd", "CNSPass@1602016");
            bossLoginPage.clickBossLogin();
            Thread.sleep(3000);
            homeLandingPage.clickOnBoardingButton();
            homeLandingPage.clickCddIndButton();
            Thread.sleep(3000);
            homeLandingPage.clickClaimButton("Nikko");
            Thread.sleep(10000);
            homeLandingPage.verifyBiometrics();
            homeLandingPage.verifyDocument("green id book");
            homeLandingPage.scrollToBottom();
            homeLandingPage.searchVerification();
            homeLandingPage.clickApproveKYC();

            WebElement taskQueueTable = driver.findElement(By.xpath("//div[@class='task-table-container p-0 m-0']"));
            Assert.assertTrue(taskQueueTable.isDisplayed());
        } catch (RuntimeException | AssertionError e) {
            Assert.fail("Test failed due to: {}", e);
            throw new RuntimeException(e);
        }
        homeLandingPage.clickLogoutButton();
        driver.close();

    }
}
