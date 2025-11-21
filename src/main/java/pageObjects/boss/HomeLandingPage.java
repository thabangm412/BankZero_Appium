package pageObjects.boss;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageObjects.AbstractPage;

import java.util.List;

public class HomeLandingPage extends AbstractPage {

    private static final Logger log = LoggerFactory.getLogger(HomeLandingPage.class);

    @FindBy(xpath = "//a[normalize-space()='Home']")
    private WebElement bossHomePage;

    @FindBy(xpath = "//*[name()='a' and contains(@type,'button')]")
    private WebElement logoutButtn;

    @FindBy(xpath = "//a[contains(text(),'On-Boarding')]")
    private WebElement onBoardingButtn;

    @FindBy(xpath = "//a[contains(text(),'CDD-Individuals')]")
    private WebElement cddIndivisualsButtn;

    @FindBy(xpath = "//div[@class='task-table-container p-0 m-0']")
    private WebElement taskQueueTable;

    @FindBy(xpath = "//div[@class='task-table-container p-0 m-0']")
    private WebElement claimButton;

    @FindBy(xpath = "//h6[@id='mat-mdc-dialog-title-0']")
    private WebElement actionPanel;

    @FindBy(xpath = "//button[@class='btn btn-primary btn-sm']")
    private WebElement selectName;

    @FindBy(xpath = "//h4[contains(text(),'Customer Details')]")
    private WebElement customerDetailsPage;

    @FindBy(css = "button.btn.btn-secondary.btn-sm.small_btn")
    private WebElement acceptProfileImage;
    //first index

    @FindBy(xpath = "//span[@style='color: green;']")
    private WebElement acceptProfileImagVerification;

    @FindBy(css = "button.btn.btn-secondary.btn-sm.small_btn")
    private WebElement acceptSelfiePics;
    //third index

    @FindBy(xpath = "//span[@style='color: green;']")
    private WebElement acceptSelfiePicsVerification;

    @FindBy(xpath = "//h4[contains(text(),'Id Document View')]")
    private WebElement idDocumentViewPage;

    @FindBy(xpath = "//a[contains(text(),'View Listings')]")
    private WebElement viewListingLink;

    @FindBy(xpath = "//tbody/tr/td[@class='gsc-search-button']/button[@class='gsc-search-button gsc-search-button-v2']/*[1]")
    private WebElement searchButton;

    @FindBy(css = ".gsc-results-close-btn")
    private WebElement closeButton;

    @FindBy(xpath = "//button[contains(text(),'Approve KYC')]")
    private WebElement approveKYCButton;

    @FindBy(xpath = "//button[contains(text(),'Approve KYC')]")
    private WebElement lexisNexisPage;

    @FindBy(xpath = "//span[@class='mat-expansion-indicator ng-tns-c2622717266-10 ng-trigger ng-trigger-indicatorRotate ng-star-inserted']")
    private WebElement dropDownButton;


    public HomeLandingPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public boolean isAt()
    {
        this.wait.until(ExpectedConditions.visibilityOf(this.bossHomePage));
        log.info("Home page displayed");
        return this.bossHomePage.isDisplayed();

    }

    public void clickOnBoardingButton()
    {
        onBoardingButtn.click();
        log.info("On-Boarding button clicked");
    }

    public void clickLogoutButton()
    {
        logoutButtn.click();
        log.info("Logout button clicked");
    }

    public void clickCddIndButton()
    {
        cddIndivisualsButtn.click();
        log.info("CDD-Indivisuals button clicked");
    }

//    public void clickClaimButton(String name) throws InterruptedException {
//        this.wait.until(ExpectedConditions.visibilityOf(this.taskQueueTable));
//        log.info("Task queue table displayed");
//
//        List<WebElement> preferredNames  = driver.findElements(By.xpath("//td[@class='mat-mdc-cell mdc-data-table__cell cdk-cell cdk-column-customerPreferredName mat-column-customerPreferredName']"));
//
//        for(WebElement preferredName : preferredNames)
//        {
//            String text = preferredName.getText().trim();;
//
//            if(text.equalsIgnoreCase(name))
//            {
//                log.info("preferred name selected: {}", text);
//                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", preferredName);
//
//                // Find the claim button relative to the name's row
//                //WebElement claimButton = preferredName.findElement(By.xpath("./ancestor::tr//button[contains(@class, 'btn-secondary')]"));
//                WebElement claimButton = preferredName.findElement(By.xpath("(//button[@class='btn btn-secondary btn-sm'][normalize-space()='claim'])[181]"));
//                //WebElement checkBox = preferredName.findElement(By.xpath("./ancestor::tr//div[contains(@class, 'd-inline')]"));
//                WebElement checkBox = preferredName.findElement(By.cssSelector("#mat-mdc-checkbox-1428-input"));
//
//                wait.until(ExpectedConditions.elementToBeClickable(claimButton)).click();
//                //claimButton.click();
//                //Thread.sleep(000);
//                log.info("Claim button clicked for name: {}", name);
//
//                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", preferredName);
//                //Find the checkbox button relative to the name's row
//                wait.until(ExpectedConditions.elementToBeClickable(checkBox)).click();
//                //checkBox.click();
//                //Thread.sleep(3000);
//                log.info("Check button clicked for name: {}", name);
//
//                //After check box is clicked popup window is displayed
//                wait.until(ExpectedConditions.elementToBeClickable(selectName)).click();
//                //selectName.click();
//                Thread.sleep(5000);
//                log.info("Select button clicked");
//                break;
//            }
//        }

    public void clickClaimButton(String name) {
        wait.until(ExpectedConditions.visibilityOf(taskQueueTable));
        log.info("Task queue table displayed");

        List<WebElement> preferredNames = driver.findElements(By.xpath(
                "//td[contains(@class, 'cdk-column-customerPreferredName')]"));

        for (WebElement preferredName : preferredNames) {
            String text = preferredName.getText().trim();
            if (!text.equalsIgnoreCase(name)) {
                continue;
            }

            log.info("Preferred name found: {}", text);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", preferredName);

            WebElement row = preferredName.findElement(By.xpath("./ancestor::tr"));

            // Find claim button inside that row (skip disabled ones)
            List<WebElement> claimButtons = row.findElements(By.xpath(".//button[contains(@class, 'btn-secondary') and normalize-space(text())='claim' and not(@disabled)]"));
            if (claimButtons.isEmpty()) {
                log.warn("No active claim button found for name: {}", name);
                return;
            }

            WebElement claimButton = claimButtons.getFirst();
            wait.until(ExpectedConditions.elementToBeClickable(claimButton)).click();
            log.info("Claim button clicked for name: {}", name);

            // After claim click → page resets → re-find preferred name
            wait.until(ExpectedConditions.stalenessOf(preferredName));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(@class, 'cdk-column-customerPreferredName')]")));

            List<WebElement> updatedPreferredNames = driver.findElements(By.xpath(
                    "//td[contains(@class, 'cdk-column-customerPreferredName')]"));
            WebElement updatedPreferredName = updatedPreferredNames.stream()
                    .filter(e -> e.getText().trim().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Preferred name not found after claim click"));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", updatedPreferredName);
            WebElement updatedRow = updatedPreferredName.findElement(By.xpath("./ancestor::tr"));

            WebElement checkBox = updatedRow.findElement(By.xpath(".//div[contains(@class, 'd-inline')]"));
            wait.until(ExpectedConditions.elementToBeClickable(checkBox)).click();
            log.info("Checkbox clicked for name: {}", name);

            wait.until(ExpectedConditions.elementToBeClickable(selectName)).click();
            log.info("Select button clicked");
            break;
        }
    }



    public void verifyBiometrics() throws InterruptedException {
        this.wait.until(ExpectedConditions.visibilityOf(this.customerDetailsPage));
        log.info("Customer details page displayed");

       WebElement selfieAcceptButton =  driver.findElement(By.cssSelector("div[class='row py-4'] div:nth-child(1) div:nth-child(2) div:nth-child(2) div:nth-child(1) button:nth-child(1)"));
       WebElement profImageAcceptButton =  driver.findElement(By.cssSelector("div[class='row py-4'] div:nth-child(2) div:nth-child(2) div:nth-child(2) div:nth-child(1) button:nth-child(1"));
        //Click third index
        wait.until(ExpectedConditions.elementToBeClickable(selfieAcceptButton)).click();
//        selfieButtons.get(3).click();
//        Thread.sleep(3000);
        log.info("Profile image accept button clicked");

        //Click first index
        wait.until(ExpectedConditions.elementToBeClickable(profImageAcceptButton)).click();
//        selfieButtons.getFirst().click();
//        Thread.sleep(3000);
        log.info("selfie Pics accept button clicked");

    }

    public void verifyDocument(String idType) throws InterruptedException {

        log.info("Starting document verification for ID type: {}", idType);

        switch (idType.toUpperCase()) {
            case "GREEN ID BOOK":

                WebElement document = driver.findElement(By.xpath("//button[@data-bs-toggle='modal']//img[@type='file']"));
                document.click();
                log.info("Document clicked");
                Thread.sleep(3000);

                this.wait.until(ExpectedConditions.visibilityOf(this.idDocumentViewPage));
                log.info("Id document view page displayed");

                List<WebElement> checkboxbuttons =  driver.findElements(By.xpath("//div[@class='mdc-checkbox']"));

                for (WebElement checkbox : checkboxbuttons) {
                    try {
                        checkbox.click();
                        log.info("Clicked checkbox label");
                    } catch (Exception e) {
                        log.error("Failed to click checkbox label: {}", e.getMessage());
                    }
                }

                List<WebElement> acceptButtons =  driver.findElements(By.xpath("//div[@class='d-inline mx-2']"));

                acceptButtons.getFirst().click();
                log.info("Accept button clicked");

                Thread.sleep(3000);


                break;

            case "SA ID CARD":

        }
    }

    public void viewListingVerification()
    {
        viewListingLink.click();
        log.info("View listing button clicked ");

        this.wait.until(ExpectedConditions.visibilityOf(this.lexisNexisPage));
        dropDownButton.click();
        log.info("Dropdown button clicked ");

    }
    public void searchVerification() throws InterruptedException {

        WebElement element = driver.findElement(By.xpath("//tbody/tr/td[@class='gsc-search-button']/button[@class='gsc-search-button gsc-search-button-v2']/*[1]"));

        WebElement inputField = driver.findElement(By.xpath("//*[name()='input' and @id='gsc-i-id1']"));

        inputField.sendKeys("Nikko Test4");
        log.info("Search text added");
        element.click();
        log.info("Search button clicked");

//        Actions actions = new Actions(driver);
//        actions.clickAndHold(element)
//                .pause(Duration.ofSeconds(2)) // Long press duration
//                .release()
//                .build()
//                .perform();


        //searchButton.click();
        Thread.sleep(3000);
        log.info("Search button clicked");

        closeButton.click();
        Thread.sleep(3000);
        log.info("Close button clicked");

    }
    public void scrollToBottom() throws InterruptedException {

        ((JavascriptExecutor)driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'end'});",
                approveKYCButton
        );
        Thread.sleep(3000);
    }


    public void clickApproveKYC()
    {
        approveKYCButton.click();
        log.info("Approve button clicked");
    }

}
