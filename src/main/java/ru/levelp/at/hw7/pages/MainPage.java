package ru.levelp.at.hw7.pages;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainPage extends AbstractPage {

    @FindBy(linkText = "Logout")
    private WebElement logoutButton;
    @FindBy(linkText = "Create challenge")
    private WebElement createChallengeButton;
    @FindBy(className = "alert-success")
    private WebElement successAlert;
    @FindBy(xpath = "//div[contains(@class,'d-none')][1]//th")
    private List<WebElement> challengers;
    @FindBy(xpath = "//div[contains(@class,'d-none')][2]//th")
    private List<WebElement> opponents;
    @FindBy(xpath = "//div[contains(@class,'d-none')]//button[text()='View all incoming challenges']")
    private WebElement viewAllIncomingChallengesButton;
    @FindBy(xpath = "//div[contains(@class,'d-none')][1]//button[text()='Accept']")
    private List<WebElement> acceptChallengeButtons;
    @FindBy(xpath = "//div[contains(@class,'d-none')][2]//button[text()='Open']")
    private List<WebElement> openGameButtons;
    @FindBy(css = "nav > a")
    private WebElement navigationTitle;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage clickLogout() {
        logoutButton.click();
        // hack to avoid test fails due to login button not clicked properly
        // wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Log in']"))) doesn't help
        driver.navigate().refresh();
        return new LoginPage(driver);
    }

    public CreateChallengeStep1Page clickCreateChallenge() {
        createChallengeButton.click();
        return new CreateChallengeStep1Page(driver);
    }

    public ChallengesListPage clickViewAllIncomingChallenges() {
        viewAllIncomingChallengesButton.click();
        return new ChallengesListPage(driver);
    }

    public String getSuccessAlertText() {
        return successAlert.getText();
    }

    public List<String> getChallengerNames() {
        return challengers.stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toList());
    }

    public List<String> getOpponentNames() {
        return opponents.stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toList());
    }

    public GamePage openGame(int index) {
        openGameButtons.get(index).click();
        return new GamePage(driver);
    }

    public MainPage acceptChallenge(int index) {
        acceptChallengeButtons.get(index).click();
        return new MainPage(driver);
    }

    public String getNavigationTitle() {
        return navigationTitle.getText();
    }
}
