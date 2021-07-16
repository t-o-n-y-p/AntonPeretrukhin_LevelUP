package ru.levelp.at.hw8.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import ru.levelp.at.hw8.pages.components.ChessBoardComponent;
import ru.levelp.at.hw8.pages.components.ChessBoardNavigationComponent;
import ru.levelp.at.hw8.pages.components.ChessGameMoveFormComponent;
import ru.levelp.at.hw8.pages.components.RegularNavigationBarComponent;

public class GamePage extends AbstractPage {

    @FindBy(className = "alert-success")
    private WebElement successAlert;
    @FindBy(className = "alert-warning")
    private WebElement warningAlert;
    @FindBy(className = "alert-info")
    private WebElement infoAlert;
    @FindBy(id = "chess-board")
    private WebElement chessBoard;
    @FindBy(id = "container")
    private WebElement moveForm;
    @FindBy(className = "btn-group")
    private WebElement navigation;
    @FindBy(tagName = "nav")
    private WebElement navigationBar;

    public GamePage(WebDriver driver) {
        super(driver);
    }

    public RegularNavigationBarComponent getNavigationBar() {
        return new RegularNavigationBarComponent(driver, navigationBar);
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

    public ChessBoardComponent getChessBoard() {
        return new ChessBoardComponent(driver, chessBoard);
    }

    public ChessBoardNavigationComponent getChessBoardNavigation() {
        return new ChessBoardNavigationComponent(driver, navigation);
    }

    public ChessGameMoveFormComponent getMoveForm() {
        return new ChessGameMoveFormComponent(driver, moveForm);
    }
}
