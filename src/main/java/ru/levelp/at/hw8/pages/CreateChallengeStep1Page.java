package ru.levelp.at.hw8.pages;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.levelp.at.hw8.pages.AbstractPage;
import ru.levelp.at.hw8.pages.CreateChallengeStep2Page;

public class CreateChallengeStep1Page extends AbstractPage {

    @FindBy(xpath = "//button[text()='Next page']")
    private WebElement nextPageButton;
    @FindBy(tagName = "th")
    private List<WebElement> opponents;
    @FindBy(name = "search")
    private WebElement searchField;
    @FindBy(css = "nav > div button")
    private WebElement searchButton;
    @FindBy(css = "td button")
    private List<WebElement> selectOpponentButtons;

    public CreateChallengeStep1Page(WebDriver driver) {
        super(driver);
    }

    public CreateChallengeStep1Page openNextPage() {
        nextPageButton.click();
        return new CreateChallengeStep1Page(driver);
    }

    public Set<String> getOpponentUsernames() {
        return opponents.stream()
            .map(th -> th.getText().split("\\s")[0])
            .collect(Collectors.toSet());
    }

    public String getNextButtonDisabledState() {
        return nextPageButton.getAttribute("disabled");
    }

    public CreateChallengeStep2Page selectOpponent(int index) {
        selectOpponentButtons.get(index).click();
        return new CreateChallengeStep2Page(driver);
    }

    public CreateChallengeStep1Page fillSearchField(String searchString) {
        searchField.sendKeys(searchString);
        return this;
    }

    public CreateChallengeStep1Page clickSearch() {
        searchButton.click();
        return new CreateChallengeStep1Page(driver);
    }
}
