package pageObjects.app.accountsActionMenu.pay;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.time.Duration;
import java.time.LocalDate;

public class QuickPayPage {

    private static final Logger log = LoggerFactory.getLogger(QuickPayPage.class);
    protected AndroidDriver driver;

    public QuickPayPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbarTitle\"]")
    private WebElement pageConfimation;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Pay\"]")
    private WebElement payButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/tile_to_image")
    private WebElement payToButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Pay\"]")
    private WebElement payMenuActionButtun;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/tile_to_image")
    private WebElement addRecipienttButtn;

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

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement addButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement pay2Buttn;

    @AndroidFindBy(id = "android:id/text1")
    private WebElement accName;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/amount")
    private WebElement amountInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/reference")
    private WebElement refInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/redo_image")
    private WebElement redoButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement confirmButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/logo")
    private WebElement paymentStatus;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement finishButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/_arrow")
    private WebElement dropDownButton;

    @AndroidFindBy(xpath = "(//android.widget.ImageButton[@resource-id=\"za.co.neolabs.bankzero:id/imgButton\"])[1]")
    private WebElement attachmentLink;

    @AndroidFindBy(accessibility = "Show roots")
    private WebElement openFrom;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/edit_note")
    private WebElement editProfile;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement updateButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_top_3")
    private WebElement deleteButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/account_number")
    private WebElement getAccNo;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/quickpay_selected")
    private WebElement payImmediatelyButton;


    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/schedule_clock_image")
    private WebElement scheduleButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/fromdate")
    private WebElement onDateInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/schedule_type_dd_arrow")
    private WebElement scheduleDropDownButtn;

    @AndroidFindBy(accessibility = "Navigate up")
    private WebElement backButton;

    public void clickPayToButtn()
    {
        payToButtn.click();
        log.info("Pay To button clicked");
    }

    public void clickPayButtn()
    {
        payMenuActionButtun.click();
        log.info("Pay Menu button clicked");
    }

    public void clickPayImmediatelyButtn()
    {
        payImmediatelyButton.click();
        log.info("Pay immediately button clicked");
    }



    public void clickPay2Buttn()
    {
        pay2Buttn.click();
        log.info("Pay button clicked");
    }


    public void clickAddRecipientButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Pay", driver);
        addRecipienttButtn.click();
        log.info("Add recipient button clicked.");
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

//        accountNo.clear();
//        log.info("Account number input field cleared");
//        accountNo.sendKeys(accNo);
//        log.info("Account number entered: {}",accNo);

        updateButtn.click();
        log.info("Add button clicked");

    }

    public void enterPaymentDetails(String amount, String ref)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Pay", driver);
        amountInputField.clear();
        log.info("Amount input field cleared");
        amountInputField.sendKeys(amount);

        refInputField.clear();
        log.info("Reference input field cleared");
        refInputField.sendKeys(ref);
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

    public String getAccName()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        log.info("Retrieving Account Name");
        return androidActions.getTextWithRetry(By.id("android:id/text1"),3);

    }

    public String getAccNo()
    {
        log.info("Retrieving new acc number");
        return getAccNo.getText();
    }

    public void clickConfirmButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Pay", driver);

        confirmButton.click();
        log.info("Confirm button clicked");
    }

    public boolean getPaymentStatus()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Pay", driver);
        log.info("Retrieving Payment status");
        return paymentStatus.isDisplayed();

    }

    public void clickFinish()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Pay", driver);
        finishButton.click();
        log.info("Finish button clicked");
    }

    public void getExistingRecipient(String name)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Pay", driver);
        AndroidActions androidActions = new AndroidActions(driver);

        androidActions.clickWithRetry(By.id("za.co.neolabs.bankzero:id/_arrow"),3);
        log.info("Drop down menu button clicked");
        androidActions.scrollToTextAndClick2(name,driver);

    }

    public void clickRedo()
    {
        redoButton.click();
        log.info("Redo button clicked.");
    }

    public void clickDownloads()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.clickWithRetry(By.xpath("//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"Downloads\"]"),3);
    }

    public void addAttachment()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        attachmentLink.click();
        log.info("Clicked attachments link");
        openFrom.click();
        log.info("Clicked 'Open from'");
        clickDownloads();
        log.info("Clicked downloads");
        androidActions.scrollToTextAndClick2("sample-pdf.pdf",driver);
        log.info("Selected sample-pdf.pdf for upload");
    }

    public String getAttachment()
    {
        return driver.findElement(By.id("za.co.neolabs.bankzero:id/imageData1")).getText();
    }

    public void editProfile()
    {
        editProfile.click();
        log.info("Edit profile button clicked");
    }

    public void clickUpdate()
    {
        updateButtn.click();
        log.info("Update button clicked");
    }

    public void clickDelete()
    {
        deleteButtn.click();
        log.info("Delete button clicked");
    }

    public void addPoP(String email,String phone)
    {
        popEmailInputField.clear();
        log.info("Email input field cleared.");
        popEmailInputField.sendKeys(email);

        popPhoneInputField.clear();
        log.info("Phone input field cleared");
        popPhoneInputField.sendKeys(phone);
    }

    public void choosePaymentSchedule(String scheduleType,int daysToAdd, String ref,String amount){
        AndroidActions androidActions = new AndroidActions(driver);
        log.info("Selecting scheduling type: {}", scheduleType);

        LocalDate currentDate = LocalDate.now();
        int todayDayOfMonth = currentDate.getDayOfMonth();
        int lastDayOfCurrentMonth = currentDate.lengthOfMonth();
        boolean isTodayLastDayOfMonth = todayDayOfMonth == lastDayOfCurrentMonth;

        log.info("Current date: {}", currentDate);
        log.info("Today's day of month: {}, Last day of current month: {}", todayDayOfMonth, lastDayOfCurrentMonth);
        log.info("Is today the last day of the month? {}", isTodayLastDayOfMonth);

        switch (scheduleType.toUpperCase()) {

            case "ONCE-OFF":

                String onceOffDate = AppiumUtils.getFutureDateFormatted(daysToAdd);
                log.info("Once-off Date to select: {}", onceOffDate);

                scheduleButtn.click();
                log.info("Schedule Button clicked");

                onDateInputField.click();
                AppiumUtils.waitForElement(By.xpath("//android.widget.LinearLayout[@resource-id=\"android:id/date_picker_header\"]/android.widget.LinearLayout"), driver);

                if (isTodayLastDayOfMonth) {
                    log.info("Today is the last day of the month — clicking 'Next' button in calendar");
                    driver.findElement(By.id("android:id/next")).click();
                } else {
                    log.info("Today is not the last day of the month — skipping 'Next' button click");
                }

                log.info("Attempting to select date: {}", onceOffDate);


                driver.findElement(By.xpath("//android.view.View[@content-desc='" + onceOffDate + "']")).click();
                driver.findElement(By.id("android:id/button1")).click();
                log.info("Once-off Date selected: {}", onceOffDate);

                refInputField.clear();
                log.info("Reference input field cleared");
                refInputField.sendKeys(ref);

                amountInputField.clear();
                log.info("Amount input field cleared");
                amountInputField.sendKeys(amount);
                log.info("Amount entered");

//                addButtn.click();
//                log.info("Add schedule button clicked");
                break;

            case "WEEKLY":

                scheduleDropDownButtn.click();
                log.info("Schedule drop down clicked");

                androidActions.scrollToTextAndClick2("Never", driver);
                log.info("Updating Scheduling to Never");

                updateButtn.click();
                log.info("Update botton clicked");

                editProfile.click();
                log.info("Edit profile clicked");

                scheduleButtn.click();
                log.info("Schedule button clicked");

                scheduleDropDownButtn.click();
                log.info("Drop down menu button clicked");

                androidActions.scrollToTextAndClick2(scheduleType, driver);
                log.info("Scheduled type weekly selected");

                String fromDate = AppiumUtils.getFutureDateFormatted(daysToAdd);
                log.info("From Date to select: {}", fromDate);

                String toDate = AppiumUtils.getFutureDateFormatted(daysToAdd + 7);
                log.info("To Date to select: {}", toDate);

                driver.findElement(By.id("za.co.neolabs.bankzero:id/fromdate")).click();
                AppiumUtils.waitForElement(By.xpath("//android.widget.LinearLayout[@resource-id=\"android:id/date_picker_header\"]/android.widget.LinearLayout"), driver);

                if (!isDateVisible(fromDate, driver)) {
                    log.info("'From' date {} not visible — clicking 'Next'", fromDate);
                    driver.findElement(By.id("android:id/next")).click();
                } else {
                    log.info("'From' date {} is visible — no need to click 'Next'", fromDate);
                }
// Now select the future date
                driver.findElement(By.xpath("//android.view.View[@content-desc='" + fromDate + "']")).click();
                log.info("Attempting to select date: {}", fromDate);
                driver.findElement(By.id("android:id/button1")).click();
                log.info("Select button selected");

                driver.findElement(By.id("za.co.neolabs.bankzero:id/todate")).click();
                AppiumUtils.waitForElement(By.xpath("//android.widget.LinearLayout[@resource-id=\"android:id/date_picker_header\"]/android.widget.LinearLayout"), driver);

                if (!isDateVisible(toDate, driver)) {
                    log.info("'To' date {} not visible — clicking 'Next'", toDate);
                    driver.findElement(By.id("android:id/next")).click();
                } else {
                    log.info("'To' date {} is visible — no need to click 'Next'", toDate);
                }

                driver.findElement(By.xpath("//android.view.View[@content-desc='" + toDate + "']")).click();
                log.info("Attempting to select date: {}", toDate);
                driver.findElement(By.id("android:id/button1")).click();
                log.info("Select button selected");


                //Select a day
                driver.findElement(By.id("za.co.neolabs.bankzero:id/schedule_when_dd_arrow")).click();
                androidActions.scrollToTextAndClick2("Monday", driver);
                log.info("Day selected");

                refInputField.clear();
                log.info("Reference input field cleared");
                refInputField.sendKeys(ref);

                amountInputField.clear();
                log.info("Amount input field cleared");
                amountInputField.sendKeys(amount);
                log.info("Amount entered");


                break;

            case "MONTHLY":

                scheduleDropDownButtn.click();
                log.info("Schedule drop down clicked");

                androidActions.scrollToTextAndClick2("Never", driver);
                log.info("Updating Scheduling to Never");

                updateButtn.click();
                log.info("Update botton clicked");

                editProfile.click();
                log.info("Edit profile clicked");

                scheduleButtn.click();
                log.info("Schedule button clicked");

                scheduleDropDownButtn.click();
                log.info("Drop down menu button clicked");

                androidActions.scrollToTextAndClick2(scheduleType, driver);
                log.info("Scheduled type monthly selected");

                String monthlyFromDate = AppiumUtils.getFutureDateFormatted(daysToAdd);
                log.info("From Date to select: {}", monthlyFromDate);

                String monthlyToDate = AppiumUtils.getFutureDateFormatted(daysToAdd + 35);
                log.info("To Date to select: {}", monthlyToDate);

                driver.findElement(By.id("za.co.neolabs.bankzero:id/fromdate")).click();
                AppiumUtils.waitForElement(By.xpath("/hierarchy/android.widget.FrameLayout"), driver);

                if (!isDateVisible(monthlyFromDate, driver)) {
                    log.info("'To' date {} not visible — clicking 'Next'", monthlyFromDate);
                    driver.findElement(By.id("android:id/next")).click();
                } else {
                    log.info("'To' date {} is visible — no need to click 'Next'", monthlyFromDate);
                }

                androidActions.clickWithRetry((By.xpath("//android.view.View[@content-desc='" + monthlyFromDate + "']")), 2);
                log.info("Attempting to select date: {}", monthlyFromDate);
                driver.findElement(By.id("android:id/button1")).click();
                log.info("Select button clicked");

                driver.findElement(By.id("za.co.neolabs.bankzero:id/todate")).click();
                AppiumUtils.waitForElement(By.xpath("//android.widget.LinearLayout[@resource-id=\"android:id/date_picker_header\"]/android.widget.LinearLayout"), driver);

//                boolean isMonthlyToDateVisible = !driver.findElements(By.xpath("//*[contains(@text, '" + monthlyToDate + "')]")).isEmpty();

                while (!isDateVisible(monthlyToDate, driver)) {
                    log.info("'To' date {} not visible — clicking 'Next'", monthlyToDate);
                    driver.findElement(By.id("android:id/next")).click();

                }
                log.info("'To' date {} is now visible — selecting it", monthlyToDate);

                driver.findElement(By.xpath("//android.view.View[@content-desc='" + monthlyToDate + "']")).click();
                log.info("Attempting to select date: {}", monthlyToDate);
                driver.findElement(By.id("android:id/button1")).click();
                log.info("Select button clicked");


                driver.findElement(By.id("za.co.neolabs.bankzero:id/schedule_when_dd_arrow")).click();
                androidActions.scrollToTextAndClick2("2nd day", driver);
                log.info("Day selected");

                refInputField.clear();
                log.info("Reference input field cleared");
                refInputField.sendKeys(ref);

                amountInputField.clear();
                log.info("Amount input field cleared");
                amountInputField.sendKeys(amount);
                log.info("Amount entered");

                break;

        }
        updateButtn.click();
    }

    public boolean isDateVisible(String date, AppiumDriver driver) {
        try {
            driver.findElement(By.xpath("//android.view.View[@content-desc='" + date + "']"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickBack()
    {
        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Thabj", driver);
        backButton.click();
        log.info("Back button clicked");
    }

    public void addAttachments()
    {

    }

}
