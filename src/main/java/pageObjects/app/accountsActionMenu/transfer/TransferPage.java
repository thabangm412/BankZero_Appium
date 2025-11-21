package pageObjects.app.accountsActionMenu.transfer;

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

public class TransferPage {

    private static final Logger log = LoggerFactory.getLogger(TransferPage.class);

    protected AndroidDriver driver;

    public TransferPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Transfer\"]")
    private WebElement transferAccountButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/_arrow")
    private WebElement dropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/amount")
    private WebElement amountInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/reference")
    private WebElement refInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement transferButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement confirmButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement finishButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Transfer success\"]")
    private WebElement transferStatus;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/edit_note")
    private WebElement scheduleButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/schedule_type_dd_arrow")
    private WebElement scheduleDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement addButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/fromdate")
    private WebElement onDateInputField;

    @AndroidFindBy(accessibility = "Navigate up")
    private WebElement backButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement updateButton;




    public void clickTransferButton()
    {
        transferAccountButtn.click();
        log.info("Transfer button clicked");
    }

    public void selectExistingAccount(String name)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Transfer", driver);
        AndroidActions androidActions = new AndroidActions(driver);
        dropDownButtn.click();
        log.info("Drop down button clicked");
        androidActions.scrollToTextAndClick2(name,driver);
    }

    public void transferMoney(String amount,String ref)
    {
        amountInputField.clear();
        log.info("Amount input field cleared");
        amountInputField.sendKeys(amount);

        refInputField.clear();
        log.info("Reference input field cleared");
        refInputField.sendKeys(ref);

    }

    public void clickTransfer()
    {
        transferButton.click();
        log.info("Transfer button clicked");
    }

    public void clickConfrim()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Transfer", driver);
        confirmButton.click();
        log.info("Confirm button clicked");

    }

    public void clickFinish()
    {
        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Thabj", driver);
        finishButton.click();
        log.info("Finish button clicked");
    }

    public void clickBack()
    {
        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Thabj", driver);
        backButton.click();
        log.info("Back button clicked");
    }

    public String getTransferStatus()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Thank you", driver);
        log.info("Retrieving transfer status");
        return transferStatus.getText();

    }

    public void clickSchedule()
    {
        scheduleButton.click();
        log.info("Schedule button clicked");
    }

    public void selectTransferSchedule(String schedule)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Schedule transfer", driver);
        AndroidActions androidActions = new AndroidActions(driver);
        scheduleDropDownButtn.click();
        log.info("Drop down button clicked");

        androidActions.scrollToTextAndClick2(schedule,driver);
        log.info("Schedule choosen: {}",schedule);
        addButtn.click();
        log.info("Add schedule button clicked");

    }

    public void chooseTransferSchedule(String scheduleType,int daysToAdd, String ref,String amount)
    {
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
            case "NEVER":

                scheduleDropDownButtn.click();
                log.info("Drop down menu button clicked");
                androidActions.scrollToTextAndClick2(scheduleType,driver);
                log.info("Scheduled type selected");
                addButtn.click();
                log.info("Add schedule button clicked");

                break;

            case "ONCE-OFF":
//                scheduleButton.click();
                scheduleDropDownButtn.click();
                log.info("Drop down menu button clicked");

                androidActions.scrollToTextAndClick2(scheduleType,driver);
                log.info("Scheduled type once-off selected");

                String onceOffDate = AppiumUtils.getFutureDateFormatted(daysToAdd);
                log.info("Once-off Date to select: {}", onceOffDate);

                onDateInputField.click();
                AppiumUtils.waitForElement(By.xpath("//android.widget.LinearLayout[@resource-id=\"android:id/date_picker_header\"]/android.widget.LinearLayout"), driver);

                if (isTodayLastDayOfMonth) {
                    log.info("Today is the last day of the month — clicking 'Next' button in calendar");
                    driver.findElement(By.id("android:id/next")).click();
                } else {
                    log.info("Today is not the last day of the month — skipping 'Next' button click");
                }

                log.info("Attempting to select date: {}", onceOffDate);
                //driver.findElement(By.xpath("//android.view.View[@content-desc='" + onceOffDate + "']")).click();

                driver.findElement(By.xpath("//android.view.View[@content-desc='" + onceOffDate + "']")).click();
                driver.findElement(By.id("android:id/button1")).click();
                log.info("Once-off Date selected: {}",onceOffDate);

                refInputField.clear();
                log.info("Reference input field cleared");
                refInputField.sendKeys(ref);

                amountInputField.clear();
                log.info("Amount input field cleared");
                amountInputField.sendKeys(amount);
                log.info("Amount entered");

                addButtn.click();
                log.info("Add schedule button clicked");
                break;

            case "WEEKLY":

                scheduleDropDownButtn.click();
                log.info("Schedule drop down clicked");

                androidActions.scrollToTextAndClick2("Never",driver);
                log.info("Updating Scheduling to Never");

                updateButton.click();
                log.info("Update botton clicked");

                scheduleButton.click();
                log.info("Schedule button clicked");

                scheduleDropDownButtn.click();
                log.info("Drop down menu button clicked");

                androidActions.scrollToTextAndClick2(scheduleType,driver);
                log.info("Scheduled type once-off selected");

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
                androidActions.scrollToTextAndClick2("Monday",driver);
                log.info("Day selected");

                refInputField.clear();
                log.info("Reference input field cleared");
                refInputField.sendKeys(ref);

                amountInputField.clear();
                log.info("Amount input field cleared");
                amountInputField.sendKeys(amount);
                log.info("Amount entered");

                addButtn.click();
                log.info("Add schedule button clicked");

                break;

            case "MONTHLY":

                scheduleDropDownButtn.click();
                log.info("Schedule drop down clicked");

                androidActions.scrollToTextAndClick2("Never",driver);
                log.info("Updating Scheduling to Never");

                updateButton.click();
                log.info("Update botton clicked");

                scheduleButton.click();

                scheduleDropDownButtn.click();
                log.info("Drop down menu button clicked");

                androidActions.scrollToTextAndClick2(scheduleType,driver);
                log.info("Scheduled type once-off selected");

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

                while (!isDateVisible(monthlyToDate, driver)) {
                    log.info("'To' date {} not visible — clicking 'Next'", monthlyToDate);
                    driver.findElement(By.id("android:id/next")).click();

                }
                log.info("'To' date {} is now visible — selecting it", monthlyToDate);

                driver.findElement(By.xpath("//android.view.View[@content-desc='" + monthlyToDate + "']")).click();
                log.info("Attempting to select date: {}", monthlyToDate);
                driver.findElement(By.id("android:id/button1")).click();
                log.info("Select button clicked");

                log.info("Attempting to select date: {}", monthlyToDate);
                driver.findElement(By.xpath("//android.view.View[@content-desc='" + monthlyToDate + "']")).click();
                driver.findElement(By.id("android:id/button1")).click();


                driver.findElement(By.id("za.co.neolabs.bankzero:id/schedule_when_dd_arrow")).click();
                androidActions.scrollToTextAndClick2("2nd day",driver);
                log.info("Day selected");

                refInputField.clear();
                log.info("Reference input field cleared");
                refInputField.sendKeys(ref);

                amountInputField.clear();
                log.info("Amount input field cleared");
                amountInputField.sendKeys(amount);
                log.info("Amount entered");

                addButtn.click();
                log.info("Add schedule button clicked");

                break;
        }
    }

    public boolean isDateVisible(String date, AppiumDriver driver) {
        try {
            driver.findElement(By.xpath("//android.view.View[@content-desc='" + date + "']"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void WeeklyOffresetScheduleType()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        //String currentSchedule = driver.findElement(By.id("android:id/text1")).getText();
        scheduleDropDownButtn.click();
        log.info("Schedule drop down clicked");

        androidActions.scrollToTextAndClick2("Never",driver);
        log.info("Updating Scheduling to Never");

        updateButton.click();
        log.info("Update botton clicked");


//        if(currentSchedule.equalsIgnoreCase("Never"))
//        {
//            log.info("Current schedule status is Never");
//            driver.findElement(By.id("za.co.neolabs.bankzero:id/home_button")).click();
//            log.info("Clicked back button");
//        }else {
//            log.info("Cuurent schedule does not match Never");
//            scheduleDropDownButtn.click();
//            log.info("Schedule drop down clicked");
//
//            androidActions.scrollToTextAndClick2("Never",driver);
//            log.info("Updating Scheduling to Never");
//
//            updateButton.click();
//            log.info("Update botton clicked");
//        }
    }


}
