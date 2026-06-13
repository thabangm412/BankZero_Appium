package pageObjects.app.accountsActionMenu.payMany;

import freemarker.ext.beans.BeansWrapperBuilder;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class PayManyPage {

    private static final Logger log = LoggerFactory.getLogger(PayManyPage.class);

    protected AndroidDriver driver;

    public PayManyPage(AndroidDriver driver) {
             if (driver == null) {
        throw new IllegalStateException("AndroidDriver is NULL. Check DriverManager initialization order.");
    }

    this.driver = driver;
    PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Pay Many\"]")
    private WebElement sendMoneyAccountButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_add_recipient")
    private WebElement addRecipienttButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/button_text")
    private WebElement finishButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/_inputText")
    private WebElement recipientInputNameField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/group_dd_arrow")
    private WebElement groupDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/bank_dd_arrow")
    private WebElement bankDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/accountType_dd_arrow")
    private WebElement accountDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/account_code")
    private WebElement accountNo;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/notify_email")
    private WebElement popEmailInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/notify_cellno")
    private WebElement popPhoneInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/schedule_clock_image")
    private WebElement tapSchedule;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/schedule_fields")
    private WebElement scheduleConfirmPage;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/schedule_type_dd_arrow")
    private WebElement scheduleDropDownButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Pay many\"]")
    private WebElement payManyAccountButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement addButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/pay_amount")
    private WebElement amountInput;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/button_text")
    private WebElement payButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/button_text")
    private WebElement confirmButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/button_text")
    private WebElement finishButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/attachments")
    private WebElement addAttachment;

    @AndroidFindBy(accessibility = "Show roots")
    private WebElement openFrom;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement confirmDocsButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_top_3")
    private WebElement deleteButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_import_excel")
    private WebElement importButtn;;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_tools")
    private WebElement exportToolButtn;

    public void clickPayManyButton()
    {
        payManyAccountButtn.click();
        log.info("Pay many menu action button clicked");
    }
    public void clickAddRecipient()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pay Many", driver);
        addRecipienttButtn.click();
        log.info("Add recipient button clicked");
    }

    public void clickFinishButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pay Many", driver);

        addRecipienttButtn.click();
        log.info("Add recipient button clicked");
    }

    public void addRecipientDetails(String name, String group,String bank,String account,String accNo)
    {
        AndroidActions androidActions = new AndroidActions(driver);
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Add recipient", driver);

        recipientInputNameField.clear();
        log.info("Recipient name input field cleared");

        driver.findElement(By.id("za.co.neolabs.bankzero:id/_inputText"))
                .sendKeys(name);
//        recipientInputNameField.sendKeys(name);
        log.info("Recipient name entered: {}",name);

        groupDropDownButtn.click();
        androidActions.scrollToTextAndClick2(group,driver);
        log.info("Group selected: {}", group);

        bankDropDownButtn.click();
        androidActions.scrollToTextAndClick2(bank,driver);
        log.info("Bank selected: {}",bank);

        accountDropDownButtn.click();
        androidActions.scrollToTextAndClick2(account,driver);
        log.info("Account selected: {}",account);

        accountNo.clear();
        log.info("Account number input field cleared");
        accountNo.sendKeys(accNo);
        log.info("Account number entered: {}",accNo);

    }

    public void updateRecipientDetails(String name, String group,String bank,String account,String accNo)
    {
        AndroidActions androidActions = new AndroidActions(driver);
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Recipient", driver);

        recipientInputNameField.clear();
        log.info("Recipient name input field cleared");

        driver.findElement(By.id("za.co.neolabs.bankzero:id/_inputText"))
                .sendKeys(name);
//        recipientInputNameField.sendKeys(name);
        log.info("Recipient name entered: {}",name);

        groupDropDownButtn.click();
        androidActions.scrollToTextAndClick2(group,driver);
        log.info("Group selected: {}", group);

        bankDropDownButtn.click();
        androidActions.scrollToTextAndClick2(bank,driver);
        log.info("Bank selected: {}",bank);

        accountDropDownButtn.click();
        androidActions.scrollToTextAndClick2(account,driver);
        log.info("Account selected: {}",account);

        accountNo.clear();
        log.info("Account number input field cleared");
        accountNo.sendKeys(accNo);
        log.info("Account number entered: {}",accNo);

    }

    public void enterPOPDetails(String email, String phone)
    {
        popEmailInputField.clear();
        log.info("POP email input field cleared");
        popEmailInputField.sendKeys(email);
        log.info("POP phone entered: {}",phone);
    }

    public void clickAddButton()
    {
        addButtn.click();
        log.info("Add button clicked");
    }

    public void getGroups(String group)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pay Many", driver);

        //WebElement groupName = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/groupName\" and @text=\"Finance\"]"));
        //String xpath = String.format("//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/groupName\" and @text=\"%s\"]", group);
        WebElement groupName = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/groupName\" and @text='" + group + "']"));
        groupName.click();
        log.info("Matched group name clicked:{}",group);
    }

    public List<String> getRecipientNames()
    {
        AppiumUtils.waitForTextToAppear(
                By.id("za.co.neolabs.bankzero:id/toolbar_title"),
                "Pay Many",
                driver
        );

        List<WebElement> recipients = driver.findElements(
                By.id("za.co.neolabs.bankzero:id/recipientName")
        );

        List<String> recipientNames = new ArrayList<>();

        for (WebElement recipient : recipients) {
            recipientNames.add(recipient.getText());
        }
        log.info("Recipient names found: {}", recipientNames);

        return recipientNames;
    }

    public void clickNewPayment(String name)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pay Many", driver);
        List<WebElement> rows = driver.findElements(
                AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"za.co.neolabs.bankzero:id/mainLayout\")")
        );
        log.info("Found rows:{}",rows);

        for (WebElement row : rows) {
            // find the recipient inside this row
            WebElement recipient = row.findElement(
                    AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"za.co.neolabs.bankzero:id/recipientName\")")
            );

            if (recipient.getText().equalsIgnoreCase(name)) {
                // find the newPay button in the same row
                WebElement newPay = row.findElement(
                        AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"za.co.neolabs.bankzero:id/new_action\")")
                );
                newPay.click();
                log.info("Matching new button clicked");
                break; // stop after clicking the match
            }
        }

    }

    public void enterAmount(String amount)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/pay_amount"),"Amount", driver);
        amountInput.clear();
        log.info("Amount input cleared");
        amountInput.sendKeys(amount);

    }

    public void clickAttachments()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/pay_amount"),"Amount", driver);
        addAttachment.click();
        log.info("Attachments button clicked");
    }

    public void clickDownloads()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.clickWithRetry(By.xpath("//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"Downloads\"]"),3);
    }

    public void addAttachments()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Attach docs", driver);
        WebElement addAttch = driver.findElement(By.id("za.co.neolabs.bankzero:id/func_button"));
        addAttch.click();
        log.info("Add attachment button clicked");

        AndroidActions androidActions = new AndroidActions(driver);
        openFrom.click();
        log.info("Clicked 'Open from'");
        clickDownloads();
        log.info("Clicked downloads");
        androidActions.scrollToTextAndClick2("sample-pdf.pdf",driver);
        log.info("Selected sample-pdf.pdf for upload");
        androidActions.attachScreenshot(driver,"Attachment added");
        clickUpdate();
        log.info("Attachment added and update clicked");
    }

    public void clickRedoButton(String name)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pay Many", driver);
        List<WebElement> rows = driver.findElements(
                AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"za.co.neolabs.bankzero:id/mainLayout\")")
        );
        log.info("Found rows:{}",rows);

        for (WebElement row : rows) {
            // find the recipient inside this row
            WebElement recipient = row.findElement(
                    AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"za.co.neolabs.bankzero:id/recipientName\")")
            );

            if (recipient.getText().equalsIgnoreCase(name)) {
                // find the newPay button in the same row
                WebElement redo = row.findElement(
                        AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"za.co.neolabs.bankzero:id/redo_action\")")
                );
                redo.click();
                log.info("Matching redo button clicked");
                break; // stop after clicking the match
            }
        }
    }

    public void clickUpdate()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Attach docs", driver);
        confirmDocsButtn.click();
        log.info("Update button clicked");
    }

    public void clickPayButton()
    {
        payButtn.click();
        log.info("Pay button clicked");
    }

    public void clickConfirmButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pay Many", driver);
        confirmButtn.click();
        log.info("Confirm button clicked");
    }

    public String transactionStatus()
    {
        WebElement statusElement = driver.findElement(By.id("za.co.neolabs.bankzero:id/toolbar_title"));
        return  statusElement.getText();
    }

    public void clickFinish()
    {
        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Thank you", driver);
        finishButton.click();
        log.info("Finish button clicked");
    }

    public void clickEditRecipient(String name)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pay Many", driver);
        List<WebElement> rows = driver.findElements(
                AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"za.co.neolabs.bankzero:id/mainLayout\")")
        );
        log.info("Found rows:{}",rows);

        for (WebElement row : rows) {
            // find the recipient inside this row
            WebElement recipient = row.findElement(
                    AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"za.co.neolabs.bankzero:id/recipientName\")")
            );

            if (recipient.getText().equalsIgnoreCase(name)) {
                // find the edit button in the same row
                WebElement edit = row.findElement(
                        AppiumBy.androidUIAutomator(
                                String.format("new UiSelector().text(\"%s\")", name)
                        )
                );
                edit.click();
                log.info("Matching edit button clicked");
                break;
            }
        }
    }

    public void clickDelete()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        deleteButtn.click();
        log.info("Delete button clicked");

        AppiumUtils.waitForElement(By.id("android:id/message"),driver);
        WebElement confirmDelete = driver.findElement(By.xpath(" //android.widget.Button[@resource-id=\"android:id/button1\" and @text=\"Yes\"]"));
        androidActions.attachScreenshot(driver,"Delete_Recipient_Confirmation");
        confirmDelete.click();
        log.info("Confirm delete button clicked");
    }

    public void clickExportButton() {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pay Many", driver);
        exportToolButtn.click();
        log.info("Export tool button clicked");
        WebElement exportButton = driver.findElement(By.id("za.co.neolabs.bankzero:id/export"));
        exportButton.click();
        log.info("Export button clicked");

    }

    public void confirmFileExport()
    {
        WebElement saveExport = driver.findElement(By.id("android:id/button1"));
        saveExport.click();
        log.info("Save export button clicked");
    }

    public boolean isFileDownloaded() {

        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/alertTitle"),"File saved!", driver);
        WebElement fileSavedMessage = driver.findElement(By.id("android:id/message"));
        String messageText = fileSavedMessage.getText();
        log.info("File export confirmation message: {}", messageText);

        if(messageText.contains("Successfully created and exported data to /")) {
            log.info("File export confirmed with message: {}", messageText);
            return true;
        } else {
            log.warn("File export confirmation message did not match expected text. Actual message: {}", messageText);
            return false;
        }
    }

    public void clickOkAfterExport()
    {
        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"File saved!", driver);
        WebElement okButton = driver.findElement(By.id("android:id/button3"));
        okButton.click();
        log.info("OK button clicked after export confirmation");
    }

    public void clickImportButton() {

        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pay Many", driver);
        importButtn.click();
        log.info("Import button clicked");


        //AppiumUtils.waitForElementToAppear(driver.findElement(By.id("za.co.neolabs.bankzero:id/toolbar_title")), "text", "Import CSV", driver);
        WebElement nextImportButton = driver.findElement(By.id("za.co.neolabs.bankzero:id/btnConfirm"));
        nextImportButton.click();
        log.info("Next import button clicked");
    }

    public void duplicatePaymentCheck()
    {
        if (driver.findElements(By.id("za.co.neolabs.bankzero:id/alertTitle")).size() > 0 && driver.findElement(By.id("za.co.neolabs.bankzero:id/alertTitle")).getText().equals("Failed payments")) {
            log.warn("Possible duplicate payment detected - clicking 'Ok' to proceed with payment");

            WebElement okButton = driver.findElement(By.id("android:id/button3"));
            okButton.click();
            log.info("'Ok' button clicked to confirm duplicate payment");
        } else {
            log.info("No duplicate payment alert detected");
        }
    }

    public void uploadImportFile() {

        AndroidActions androidActions = new AndroidActions(driver);
        openFrom.click();
        log.info("Clicked 'Open from' for import file");

        WebElement documentsOption = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"Galaxy A56 5G\"]"));
        documentsOption.click();
        log.info("Clicked 'Documents' for import file");

        WebElement downloadsOption = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"Download\"]"));
        downloadsOption.click();
        log.info("Clicked 'Downloads' for import file");

        androidActions.scrollToTextAndClick2("export_recipients (1) 1.csv",driver);
        log.info("Selected export_recipients (1) 1.csv for upload");
    }

    public String getFileUploaded()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Import CSV", driver);
        WebElement fileNameElement = driver.findElement(By.id("za.co.neolabs.bankzero:id/selected_file"));
        String fileName = fileNameElement.getText();
        log.info("File uploaded for import: {}", fileName);
        return fileName;
    }

    public void clickConfirmImport()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Import CSV", driver);
        WebElement confirmImportButton = driver.findElement(By.id("za.co.neolabs.bankzero:id/btnConfirm"));
        confirmImportButton.click();
        log.info("Confirm import button clicked");
    }

    public static List<String> getRecipientNamesFromExcel(String filePath)
            throws IOException {

        List<String> recipients = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header row
                Row row = sheet.getRow(i);

                if (row != null && row.getCell(0) != null) {
                    recipients.add(
                            row.getCell(0).toString().trim()
                    );
                }
            }
        }

        return recipients;
    }

}
