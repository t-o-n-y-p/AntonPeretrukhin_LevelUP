package ru.levelp.at.hw8.pages.components;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.levelp.at.hw8.pages.utils.AttributeNotEqualToCondition;

public class ChessBoardNavigationComponent extends RootElementPageComponent {

    private final WebElement previousMoveButton;
    private final WebElement nextMoveButton;

    public ChessBoardNavigationComponent(WebDriver driver, WebElement root) {
        super(driver, root);
        previousMoveButton = root.findElement(By.id("previous-move"));
        nextMoveButton = root.findElement(By.id("next-move"));
    }

    public String getNotation() {
        return root.findElement(By.id("move-notation")).getText();
    }

    public void clickPreviousMoveOnLastMove() {
        String oldPreviousMoveId = previousMoveButton.getAttribute("previousMoveId");
        previousMoveButton.click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId));
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "disabled", "true"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void clickPreviousMoveOnSecondMove() {
        String oldNextMoveId = nextMoveButton.getAttribute("nextMoveId");
        previousMoveButton.click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        wait.until(ExpectedConditions.attributeToBe(previousMoveButton, "disabled", "true"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void clickPreviousMove() {
        String oldNextMoveId = nextMoveButton.getAttribute("nextMoveId");
        String oldPreviousMoveId = previousMoveButton.getAttribute("previousMoveId");
        previousMoveButton.click();
        gamePageNavigationWait(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        gamePageNavigationWait(
            new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId)
        );
    }

    public void clickNextMoveOnFirstMove() {
        String oldNextMoveId = nextMoveButton.getAttribute("nextMoveId");
        nextMoveButton.click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "disabled", "true"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void clickNextMoveOnSecondToLastMove() {
        String oldPreviousMoveId = previousMoveButton.getAttribute("previousMoveId");
        nextMoveButton.click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId));
        wait.until(ExpectedConditions.attributeToBe(nextMoveButton, "disabled", "true"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void clickNextMove() {
        String oldNextMoveId = nextMoveButton.getAttribute("nextMoveId");
        String oldPreviousMoveId = previousMoveButton.getAttribute("previousMoveId");
        nextMoveButton.click();
        gamePageNavigationWait(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        gamePageNavigationWait(
            new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId)
        );
    }

    private void gamePageNavigationWait(ExpectedCondition<Boolean> expectedCondition) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(expectedCondition);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

}
