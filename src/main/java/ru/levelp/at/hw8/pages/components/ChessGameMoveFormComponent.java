package ru.levelp.at.hw8.pages.components;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class ChessGameMoveFormComponent extends RootElementPageComponent {

    private static final String MAKE_MOVE_BUTTON_LOCATOR = "#make-move button";
    private static final String RESIGN_BUTTON_LOCATOR = "#resign > button";

    public ChessGameMoveFormComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public void selectFromSquare(String option) {
        new Select(root.findElement(By.id("square1"))).selectByVisibleText(option);
    }

    public void selectToSquare(String option) {
        new Select(root.findElement(By.id("square2"))).selectByVisibleText(option);
    }

    public void clickMakeMove() {
        root.findElement(By.cssSelector(MAKE_MOVE_BUTTON_LOCATOR)).click();
    }

    public void clickResign() {
        root.findElement(By.cssSelector(RESIGN_BUTTON_LOCATOR)).click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.attributeToBe(By.id("resign-modal"), "aria-hidden", ""));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public ConfirmResignationAlertComponent getConfirmResignationAlert() {
        return new ConfirmResignationAlertComponent(driver, root.findElement(By.id("resign-modal")));
    }

    public boolean isEmpty() {
        return root.findElements(By.className("form-row")).isEmpty();
    }
}
