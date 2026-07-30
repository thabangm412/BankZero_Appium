package pageObjects.app.business;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.time.Duration;
import java.util.List;

public class BusinessAuthChainPage {

    private static final Logger log = LoggerFactory.getLogger(BusinessAuthChainPage.class);

    protected AndroidDriver driver;
    protected AndroidActions androidActions;

    private final Duration WAIT = Duration.ofSeconds(10);
    public BusinessAuthChainPage(AndroidDriver driver) {
        this.driver = driver;
        this.androidActions = new AndroidActions(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Authorisation chain\"]")
    private WebElement menuAuthChainButtn;
    @AndroidFindBy(accessibility = "Navigate up")
    private WebElement backButton;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/account_name")
    private WebElement entityName;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/account_balance")
    private WebElement accountBalance;
    @AndroidFindBy(id = "android:id/button2")
    private WebElement dontSaveButton;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement clickUpdateButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement clickConfirmButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement clickFinishButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/levelA_intput")
    private WebElement levelAInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/levelB_intput")
    private WebElement levelBInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/levelC_intput")
    private WebElement levelCInputField;

    private final By ownersRecyclerView =
            By.id("za.co.neolabs.bankzero:id/owners_and_officials");

    // Owner cards
    private final By ownerCards =
            By.xpath("//androidx.recyclerview.widget.RecyclerView[@resource-id='za.co.neolabs.bankzero:id/owners_and_officials']/android.view.ViewGroup");

    private final By deleteButton =
            By.id("za.co.neolabs.bankzero:id/imgButton");

    public void dragOwnerToDelete(AuthLevelSlot sourceSlot) {

        WebElement owner = driver.findElement(sourceSlot.locator());

        Rectangle source = owner.getRect();

        int startX = source.getX() + source.getWidth() / 2;
        int startY = source.getY() + source.getHeight() / 2;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence drag = new Sequence(finger, 1);

        // Move to owner
        drag.addAction(finger.createPointerMove(
                Duration.ZERO,
                PointerInput.Origin.viewport(),
                startX,
                startY));

        // Finger down
        drag.addAction(finger.createPointerDown(
                PointerInput.MouseButton.LEFT.asArg()));

        // Long press so the delete button appears
        drag.addAction(new Pause(finger, Duration.ofMillis(1200)));

        // Execute only the long press
        driver.perform(List.of(drag));

        // Wait for delete button
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement delete = wait.until(
                ExpectedConditions.visibilityOfElementLocated(deleteButton));

        Rectangle target = delete.getRect();

        int endX = target.getX() + target.getWidth() / 2;
        int endY = target.getY() + target.getHeight() / 2;

        // Continue the same finger gesture
        Sequence finishDrag = new Sequence(finger, 2);

        finishDrag.addAction(finger.createPointerMove(
                Duration.ofMillis(800),
                PointerInput.Origin.viewport(),
                endX,
                endY));

        finishDrag.addAction(new Pause(finger, Duration.ofMillis(200)));

        finishDrag.addAction(finger.createPointerUp(
                PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(finishDrag));
    }
    public boolean isLevelAmountCleared(AuthLevelInput level) {

        return "0.00".equals(getLevelAmount(level));
    }

    public boolean isSlotOccupied(AuthLevelSlot slot) {

        WebElement slotElement = driver.findElement(slot.locator());

        List<WebElement> dropNotes = slotElement.findElements(
                By.id("za.co.neolabs.bankzero:id/drop_note"));

        // If drop_note exists, the slot is empty.
        return dropNotes.isEmpty();
    }
    public void confirmRemoveAuthorizer() {
        AppiumUtils.waitForElement(By.id("za.co.neolabs.bankzero:id/action_bar_root"), driver);
        WebElement confirmButton = driver.findElement(By.id("android:id/button1"));
        confirmButton.click();
        log.info("Confirmed removal of authorizer by clicking 'Confirm' button");
    }
    public enum AuthLevelInput {

        LEVEL_A("za.co.neolabs.bankzero:id/levelA_intput"),
        LEVEL_B("za.co.neolabs.bankzero:id/levelB_intput"),
        LEVEL_C("za.co.neolabs.bankzero:id/levelC_intput");

        private final String id;

        AuthLevelInput(String id) {
            this.id = id;
        }

        public By locator() {
            return By.id(id);
        }
    }

    public String getLevelAmount(AuthLevelInput level) {

        WebElement input = driver.findElement(level.locator());

        return input.getText().trim();
    }
    public void clearLevelAmount(AuthLevelInput level) {

        WebElement input = driver.findElement(level.locator());

        input.clear();
    }
    public void inputLevelAmount(String level, String amount) {

        switch (level.toUpperCase()) {
            case "A":
                levelAInputField.clear();
                levelAInputField.sendKeys(amount);
                break;
            case "B":
                levelBInputField.clear();
                levelBInputField.sendKeys(amount);
                break;
            case "C":
                levelCInputField.clear();
                levelCInputField.sendKeys(amount);
                break;
            default:
                throw new IllegalArgumentException("Unknown level: " + level + " (expected A, B, or C)");
        }
    }
    public record AuthSlotState(
            AuthLevelSlot slot,
            String authoriser,
            String role) {}
    public AuthSlotState getSlotState(AuthLevelSlot slot) {

        WebElement element = driver.findElement(slot.locator());

        String authoriser = element.findElement(
                        By.id("za.co.neolabs.bankzero:id/tile_title"))
                .getText();

        return new AuthSlotState(slot, authoriser, null);
    }
    public void dragSlotToSlot(AuthLevelSlot sourceSlot, AuthLevelSlot targetSlot) {

        WebElement source = driver.findElement(sourceSlot.locator());
        WebElement target = driver.findElement(targetSlot.locator());

        dragOwnerToSlot(source, target);
    }
    public enum AuthLevelSlot {

        A1("za.co.neolabs.bankzero:id/a_one"),
        A2("za.co.neolabs.bankzero:id/a_two"),
        A3("za.co.neolabs.bankzero:id/a_three"),
        //slot A delete button
        A4("za.co.neolabs.bankzero:id/imgButton"),


        B1("za.co.neolabs.bankzero:id/b_one"),
        B2("za.co.neolabs.bankzero:id/b_two"),
        B3("za.co.neolabs.bankzero:id/b_three"),

        C1("za.co.neolabs.bankzero:id/c_one"),
        C2("za.co.neolabs.bankzero:id/c_two"),
        C3("za.co.neolabs.bankzero:id/c_three");

        private final String id;

        AuthLevelSlot(String id) {
            this.id = id;
        }

        public By locator() {
            return By.id(id);
        }
    }
    public void dragOwnerToSlot(WebElement owner, WebElement slot) {

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence drag = new Sequence(finger, 1);

        Rectangle source = owner.getRect();
        Rectangle target = slot.getRect();

        int startX = source.getX() + source.getWidth() / 2;
        int startY = source.getY() + source.getHeight() / 2;

        int endX = target.getX() + target.getWidth() / 2;
        int endY = target.getY() + target.getHeight() / 2;

        drag.addAction(finger.createPointerMove(
                Duration.ZERO,
                PointerInput.Origin.viewport(),
                startX,
                startY));

        drag.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // VERY IMPORTANT
        drag.addAction(new Pause(finger, Duration.ofMillis(1200)));

        drag.addAction(finger.createPointerMove(
                Duration.ofMillis(1000),
                PointerInput.Origin.viewport(),
                endX,
                endY));

        // Optional pause while over the drop target
        drag.addAction(new Pause(finger, Duration.ofMillis(300)));

        drag.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(drag));
    }
    public WebElement getLastOwnerCard() {

        List<WebElement> owners = driver.findElements(ownerCards);

        return owners.get(owners.size() - 2);
    }
    public void dragLastOwner(By slotLocator) {

        WebElement owner = getLastOwnerCard();

        WebElement slot = driver.findElement(slotLocator);

        dragOwnerToSlot(owner, slot);
    }


    public void confirmUnsavedChanges() {
        AppiumUtils.waitForElement(By.id("za.co.neolabs.bankzero:id/action_bar_root"), driver);
        dontSaveButton.click();
        log.info("Confirmed unsaved changes by clicking 'Don't Save' button");
    }
    public WebElement returnAccountNameElement() {
        return entityName;
    }
    public WebElement returnAccountBalanceElement() {
        return accountBalance;
    }
    public void clickBackButton() {
            backButton.click();
            log.info("Back button clicked");
    }
    public void clickUpdate() {
        AppiumUtils.waitForElementToBeClickable(clickUpdateButton, driver);
        clickUpdateButton.click();
        log.info("Update button clicked");
    }
    public void clickConfirm() {
        AppiumUtils.waitForElementToBeClickable(clickConfirmButton, driver);
        clickConfirmButton.click();
        log.info("Confirm button clicked");
    }

    public void clickFinish() {
        AppiumUtils.waitForElementToBeClickable(clickFinishButton, driver);
        clickFinishButton.click();
        log.info("Finish button clicked");
    }
    public void clickAuthorisationChain()
    {
        try {
            menuAuthChainButtn.click();
            log.info("Authorisation menu action button clicked");

        } catch (Exception e) {
            log.error("Authorisation menu action button not clickable", e);
            throw e;
        }

    }
    public void scrollRightToTile() {

        try {
            String containerResourceId = "za.co.neolabs.bankzero:id/owners_and_officials";
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().resourceId(\"" + containerResourceId + "\"))" +
                            ".setAsHorizontalList()" +
                            ".scrollToEnd(5)" // '5' is the max number of swipes to perform until the end is found
            ));
            log.info("✅ Scrolled right and clicked tile with text");

        } catch (Exception e) {
            log.error("❌ Could not scroll", e);
            throw new NoSuchElementException("Element not found after scrolling: " + e.getMessage());
        }
    }

    public void confirmAbort()
    {
        AppiumUtils.waitForElement(By.xpath("//androidx.appcompat.widget.LinearLayoutCompat[@resource-id=\"za.co.neolabs.bankzero:id/parentPanel\"]"),driver);
        WebElement dontSaveButtn = driver.findElement(By.id("android:id/button2"));
        dontSaveButtn.click();
        log.info("✅ Confirmed dont save channges");
    }

    public void addAuthoriserLevel(WebElement sourceElement,WebElement target, String level)
    {
        log.info("Authoriser level selected: {}",level);
    }

    public void addLevelAmount(String amount,String level)
    {
         try {
             switch (level.toUpperCase())
             {
                 case "LEVEL A":
                     WebElement levelAInputField = driver.findElement(By.id("za.co.neolabs.bankzero:id/levelA_intput"));
                     androidActions.safeClear(levelAInputField,"level A amount input field");
                     levelAInputField.sendKeys(amount);
                     log.info("Amount for level A entered: {}",amount);
                     break;
                 case "LEVEL B":
                     WebElement levelBInputField = driver.findElement(By.id("za.co.neolabs.bankzero:id/levelB_intput"));
                     androidActions.safeClear(levelBInputField,"level B amount input field");
                     levelBInputField.sendKeys(amount);
                     log.info("Amount for level B entered: {}",amount);
                     break;
                 case "LEVEL C":
                     WebElement levelCInputField = driver.findElement(By.id("za.co.neolabs.bankzero:id/levelC_intput"));
                     androidActions.safeClear(levelCInputField,"level C amount input field");
                     levelCInputField.sendKeys(amount);
                     log.info("Amount for level C entered: {}",amount);
                     break;
                 default:
                     log.error("Invalid level provided: {}",level);
                     throw new IllegalArgumentException("Invalid level: " + level);
             }

         }catch (Exception e) {
             log.error("Amount for level {} not entered",level);
             throw e;
         }
    }

    public void removeAuthoriserLevelOwner(String level)
    {
        try {
            log.info("Authoriser level sele ted for removal: {}",level);
            switch (level.toUpperCase())
            {
                case "LEVEL A":
                    WebElement sourceELA = driver.findElement(By.xpath("(//android.widget.ImageView[@resource-id=\"za.co.neolabs.bankzero:id/tile_image\"])[9]"));
                    break;
                case "LEVEL B":
                    WebElement sourceElB = driver.findElement(By.xpath("(//android.widget.ImageView[@resource-id=\"za.co.neolabs.bankzero:id/tile_image\"])[9]"));
                    WebElement targetElB = driver.findElement(By.xpath("//android.widget.ImageButton[@resource-id=\"za.co.neolabs.bankzero:id/imgButton\"]"));
                   // androidActions.performDragAndDrop(driver,sourceElB,targetElB);
                    androidActions.performLongPressAndDragDrop(driver,sourceElB,targetElB);
                    break;
                case "LEVEL C":
                    WebElement sourceElC = driver.findElement(By.xpath("(//android.widget.ImageView[@resource-id=\"za.co.neolabs.bankzero:id/tile_image\"])[11]"));
                    break;
                default:
                    log.error("Invalid level provided for removal: {}",level);
                    throw new IllegalArgumentException("Invalid level for removal: " + level);
            }
            //androidActions.performDragAndDrop(driver,sourceElement,target);
            log.info("Drag and drop performed for removal of level: {}",level);
        } catch (Exception e) {
            log.error("Drag and drop failed for removal of level: {}",level);
            throw e;
        }


    }
}
