package ru.levelp.at.hw7.pages;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GamePage extends AbstractPage {

    @FindBy(className = "alert-info")
    private WebElement infoAlert;
    @FindBy(css = "#chess-board td")
    private List<WebElement> chessBoardSquares;
    @FindBy(linkText = "Main page")
    private WebElement mainPageButton;
    @FindBy(className = "form-row")
    private List<WebElement> makeMoveForms;

    protected GamePage(WebDriver driver) {
        super(driver);
    }

    public String getInfoAlertText() {
        return infoAlert.getText();
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
}
