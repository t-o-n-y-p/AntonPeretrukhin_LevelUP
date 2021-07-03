package ru.levelp.at.hw8.pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.levelp.at.hw8.pages.AbstractPage;
import ru.levelp.at.hw8.pages.MainPage;

public class ChallengesListPage extends AbstractPage {

    @FindBy(className = "alert-success")
    private WebElement successAlert;
    @FindBy(tagName = "th")
    private List<WebElement> challengers;
    @FindBy(xpath = "//button[text()='Next page']")
    private WebElement nextPageButton;
    @FindBy(name = "search")
    private WebElement searchField;
    @FindBy(css = "nav > div button")
    private WebElement searchButton;
    @FindBy(css = "td button")
    private List<WebElement> acceptButtons;
    @FindBy(linkText = "Main page")
    private WebElement mainPageButton;

    public ChallengesListPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getChallengerNames() {
        return challengers.stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toList());
    }

    public ChallengesListPage openNextPage() {
        nextPageButton.click();
        return new ChallengesListPage(driver);
    }

    public String getNextButtonDisabledState() {
        return nextPageButton.getAttribute("disabled");
    }

    public ChallengesListPage fillSearchField(String searchString) {
        searchField.sendKeys(searchString);
        return this;
    }

    public ChallengesListPage clickSearch() {
        searchButton.click();
        return new ChallengesListPage(driver);
    }

    public String getSuccessAlertText() {
        return successAlert.getText();
    }

    public ChallengesListPage acceptOpponent(int index) {
        acceptButtons.get(index).click();
        return new ChallengesListPage(driver);
    }

    public MainPage clickMainPage() {
        mainPageButton.click();
        return new MainPage(driver);
    }
}
