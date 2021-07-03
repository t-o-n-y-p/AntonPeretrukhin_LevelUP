package ru.levelp.at.hw7.pages;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import ru.levelp.at.hw7.pages.utils.AttributeNotEqualToCondition;

public class GamePage extends AbstractPage {

    @FindBy(className = "alert-success")
    private WebElement successAlert;
    @FindBy(className = "alert-warning")
    private WebElement warningAlert;
    @FindBy(className = "alert-info")
    private WebElement infoAlert;
    @FindBy(css = "#chess-board td")
    private List<WebElement> chessBoardSquares;
    @FindBy(linkText = "Main page")
    private WebElement mainPageButton;
    @FindBy(className = "form-row")
    private List<WebElement> makeMoveForms;
    @FindBy(id = "square1")
    private WebElement squareFromDropdown;
    @FindBy(id = "square2")
    private WebElement squareToDropdown;
    @FindBy(css = "#make-move button")
    private WebElement makeMoveButton;
    @FindBy(xpath = "//button[text()='Resign']")
    private WebElement resignButton;
    @FindBy(xpath = "//button[text()='Resign the game']")
    private WebElement confirmResignationButton;
    @FindBy(id = "previous-move")
    private WebElement previousMoveButton;
    @FindBy(id = "next-move")
    private WebElement nextMoveButton;
    @FindBy(id = "move-notation")
    private WebElement notation;

    protected GamePage(WebDriver driver) {
        super(driver);
    }

    public String getInfoAlertText() {
        return infoAlert.getText();
    }

    public String getWarningAlertText() {
        return warningAlert.getText();
    }

    public String getSuccessAlertText() {
        return successAlert.getText();
    }

    public String getChessBoard() {
        return IntStream.range(0, 72)
            .filter(i -> i % 9 != 0)
            .mapToObj(i -> chessBoardSquares.get(i).getText())
            .collect(Collectors.joining(";"));
    }

    public MainPage clickMainPage() {
        mainPageButton.click();
        return new MainPage(driver);
    }

    public List<WebElement> getMakeMoveForms() {
        return makeMoveForms;
    }

    public GamePage selectFromSquare(String option) {
        new Select(squareFromDropdown).selectByVisibleText(option);
        return this;
    }

    public GamePage selectToSquare(String option) {
        new Select(squareToDropdown).selectByVisibleText(option);
        return this;
    }

    public GamePage clickMakeMove() {
        makeMoveButton.click();
        return new GamePage(driver);
    }

    public String getNotation() {
        return notation.getText();
    }

    public GamePage clickPreviousMoveOnLastMove() {
        String oldPreviousMoveId = previousMoveButton.getAttribute("previousMove");
        previousMoveButton.click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId));
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "disabled", "true"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return new GamePage(driver);
    }

    public GamePage clickPreviousMoveOnSecondMove() {
        String oldNextMoveId = nextMoveButton.getAttribute("nextMove");
        previousMoveButton.click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        wait.until(ExpectedConditions.attributeToBe(previousMoveButton, "disabled", "true"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return new GamePage(driver);
    }

    public GamePage clickPreviousMove() {
        String oldNextMoveId = nextMoveButton.getAttribute("nextMove");
        String oldPreviousMoveId = previousMoveButton.getAttribute("previousMove");
        previousMoveButton.click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return new GamePage(driver);
    }

    public GamePage clickNextMoveOnFirstMove() {
        String oldNextMoveId = nextMoveButton.getAttribute("nextMove");
        nextMoveButton.click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "disabled", "true"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return new GamePage(driver);
    }

    public GamePage clickNextMoveOnSecondToLastMove() {
        String oldPreviousMoveId = previousMoveButton.getAttribute("previousMove");
        nextMoveButton.click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId));
        wait.until(ExpectedConditions.attributeToBe(nextMoveButton, "disabled", "true"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return new GamePage(driver);
    }

    public GamePage clickNextMove() {
        String oldNextMoveId = nextMoveButton.getAttribute("nextMove");
        String oldPreviousMoveId = previousMoveButton.getAttribute("previousMove");
        nextMoveButton.click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return new GamePage(driver);
    }

    public GamePage clickResign() {
        resignButton.click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.attributeToBe(By.id("resign-modal"), "aria-hidden", ""));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return this;
    }

    public GamePage confirmResignation() {
        confirmResignationButton.click();
        return new GamePage(driver);
    }
}
