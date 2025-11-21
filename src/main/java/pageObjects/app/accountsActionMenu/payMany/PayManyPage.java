package pageObjects.app.accountsActionMenu.payMany;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.time.Duration;
import java.util.List;

public class PayManyPage {

    private static final Logger log = LoggerFactory.getLogger(PayManyPage.class);

    protected AndroidDriver driver;

    public PayManyPage(AndroidDriver driver) {
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

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/pay_amount")
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
        String xpath = String.format("//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/groupName\" and @text=\"%s\"]", group);
        WebElement groupName = driver.findElement(By.xpath(xpath));
        groupName.click();
        log.info("Macthed group name clicked:{}",group);
    }

    public String getRecipientName()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pay Many", driver);
        WebElement recipient = driver.findElement(
                AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"za.co.neolabs.bankzero:id/recipientName\")")
        );
        return recipient.getText();
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
        clickUpdate();
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
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Thank you", driver);
        finishButton.click();
        log.info("Finish button clicked");
    }


//    public void schedulePayment(String scheduleType)
//    {
//
//        tapSchedule.click();
//        log.info("Schedule tab clicked");
//
//        AppiumUtils.waitForElementToAppear(scheduleConfirmPage,"dislayed","true",driver);
//
//        AndroidActions androidActions = new AndroidActions(driver);
//        log.info("Selecting scheduling type: {}", scheduleType);
//
//        LocalDate currentDate = LocalDate.now();
//        int todayDayOfMonth = currentDate.getDayOfMonth();
//        int lastDayOfCurrentMonth = currentDate.lengthOfMonth();
//        boolean isTodayLastDayOfMonth = todayDayOfMonth == lastDayOfCurrentMonth;
//
//        log.info("Current date: {}", currentDate);
//        log.info("Today's day of month: {}, Last day of current month: {}", todayDayOfMonth, lastDayOfCurrentMonth);
//        log.info("Is today the last day of the month? {}", isTodayLastDayOfMonth);
//
//        log.info("Selecting scheduling type: {}", scheduleType);
//
//        switch (scheduleType.toUpperCase()) {
//            case "WEEKLY":
//
//                scheduleDropDownButtn.click();
//                log.info("Schedule drop down clicked");
//
//                androidActions.scrollToTextAndClick2("Never",driver);
//                log.info("Updating Scheduling to Never");
//
//                updateButton.click();
//                log.info("Update botton clicked");
//
//                scheduleButton.click();
//
//                scheduleDropDownButtn.click();
//                log.info("Drop down menu button clicked");
//
//                androidActions.scrollToTextAndClick2(scheduleType,driver);
//                log.info("Scheduled type once-off selected");
//
//                String fromDate = AppiumUtils.getFutureDateFormatted(daysToAdd);
//                log.info("From Date to select: {}", fromDate);
//
//                String toDate = AppiumUtils.getFutureDateFormatted(daysToAdd + 7);
//                log.info("To Date to select: {}", toDate);
//
//                driver.findElement(By.id("za.co.neolabs.bankzero:id/fromdate")).click();
//                AppiumUtils.waitForElement(By.xpath("//android.widget.LinearLayout[@resource-id=\"android:id/date_picker_header\"]/android.widget.LinearLayout"), driver);
//
//                if (isTodayLastDayOfMonth) {
//                    log.info("Today is the last day of the month — clicking 'Next' button in calendar");
//                    driver.findElement(By.id("android:id/next")).click();
//                } else {
//                    log.info("Today is not the last day of the month — skipping 'Next' button click");
//                }
//
//// Now select the future date
//                log.info("Attempting to select date: {}", fromDate);
//                driver.findElement(By.xpath("//android.view.View[@content-desc='" + fromDate + "']")).click();
//                driver.findElement(By.id("android:id/button1")).click();
//
//                driver.findElement(By.id("za.co.neolabs.bankzero:id/todate")).click();
//                AppiumUtils.waitForElement(By.xpath("//android.widget.LinearLayout[@resource-id=\"android:id/date_picker_header\"]/android.widget.LinearLayout"), driver);
//
//                if (isTodayLastDayOfMonth) {
//                    log.info("Today is the last day of the month — clicking 'Next' button in calendar");
//                    driver.findElement(By.id("android:id/next")).click();
//                } else {
//                    log.info("Today is not the last day of the month — skipping 'Next' button click");
//                }
//
//                log.info("Attempting to select date: {}", toDate);
//                driver.findElement(By.xpath("//android.view.View[@content-desc='" + toDate + "']")).click();
//                driver.findElement(By.id("android:id/button1")).click();
//
//                //Select a day
//                driver.findElement(By.id("za.co.neolabs.bankzero:id/schedule_when_dd_arrow")).click();
//                androidActions.scrollToTextAndClick2("Monday",driver);
//                log.info("Day selected");
//
//                refInputField.clear();
//                log.info("Reference input field cleared");
//                refInputField.sendKeys(ref);
//
//                amountInputField.clear();
//                log.info("Amount input field cleared");
//                amountInputField.sendKeys(amount);
//                log.info("Amount entered");
//
//                addButtn.click();
//                log.info("Add schedule button clicked");
//
//                break;
//
//            case "MONTHLY":
//
//                scheduleDropDownButtn.click();
//                log.info("Schedule drop down clicked");
//
//                androidActions.scrollToTextAndClick2("Never",driver);
//                log.info("Updating Scheduling to Never");
//
//                updateButton.click();
//                log.info("Update botton clicked");
//
//                scheduleButton.click();
//
//                scheduleDropDownButtn.click();
//                log.info("Drop down menu button clicked");
//
//                androidActions.scrollToTextAndClick2(scheduleType,driver);
//                log.info("Scheduled type once-off selected");
//
//                String monthlyFromDate = AppiumUtils.getFutureDateFormatted(daysToAdd);
//                log.info("From Date to select: {}", monthlyFromDate);
//
//                String monthlyToDate = AppiumUtils.getFutureDateFormatted(daysToAdd + 35);
//                log.info("To Date to select: {}", monthlyToDate);
//
//                driver.findElement(By.id("za.co.neolabs.bankzero:id/fromdate")).click();
//                AppiumUtils.waitForElement(By.xpath("/hierarchy/android.widget.FrameLayout"), driver);
//
////                boolean isMonthlyFromDateVisible = !driver.findElements(By.xpath("//*[contains(@text, '" + monthlyFromDate + "')]")).isEmpty();
////
////                if (!isMonthlyFromDateVisible) {
////                    log.info("{} is not visible — clicking 'Next' button in calendar", monthlyFromDate);
////                    driver.findElement(By.id("android:id/next")).click();
////                } else {
////                    log.info("{} is already visible — no need to click 'Next'", monthlyFromDate);
////                }
//
//                log.info("Attempting to select date: {}", monthlyFromDate);
//                androidActions.clickWithRetry((By.xpath("//android.view.View[@content-desc='" + monthlyFromDate + "']")),2);
//                //driver.findElement(By.xpath("//android.view.View[@content-desc='" + monthlyFromDate + "']")).click();
//                driver.findElement(By.id("android:id/button1")).click();
//
//                driver.findElement(By.id("za.co.neolabs.bankzero:id/todate")).click();
//                AppiumUtils.waitForElement(By.xpath("//android.widget.LinearLayout[@resource-id=\"android:id/date_picker_header\"]/android.widget.LinearLayout"), driver);
//
//                boolean isMonthlyToDateVisible = !driver.findElements(By.xpath("//*[contains(@text, '" + monthlyToDate + "')]")).isEmpty();
//
//                if (!isMonthlyToDateVisible) {
//                    log.info("{} is not visible — clicking 'Next' button in calendar", monthlyToDate);
//                    driver.findElement(By.id("android:id/next")).click();
//                } else {
//                    log.info("{} is already visible — no need to click 'Next'", monthlyToDate);
//                }
//
//                log.info("Attempting to select date: {}", monthlyToDate);
//                driver.findElement(By.xpath("//android.view.View[@content-desc='" + monthlyToDate + "']")).click();
//                driver.findElement(By.id("android:id/button1")).click();
//
//
//                driver.findElement(By.id("za.co.neolabs.bankzero:id/schedule_when_dd_arrow")).click();
//                androidActions.scrollToTextAndClick2("2nd day",driver);
//                log.info("Day selected");
//
//                refInputField.clear();
//                log.info("Reference input field cleared");
//                refInputField.sendKeys(ref);
//
//                amountInputField.clear();
//                log.info("Amount input field cleared");
//                amountInputField.sendKeys(amount);
//                log.info("Amount entered");
//
//                addButtn.click();
//                log.info("Add schedule button clicked");
//
//                break;
//        }
   // }

}
