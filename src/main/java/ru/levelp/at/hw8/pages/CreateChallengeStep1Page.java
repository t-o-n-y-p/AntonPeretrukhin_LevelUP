package ru.levelp.at.hw8.pages;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.levelp.at.hw8.pages.components.ListComponent;
import ru.levelp.at.hw8.pages.components.ResultListBlockComponent;
import ru.levelp.at.hw8.pages.components.SearchNavigationBarComponent;

public class CreateChallengeStep1Page extends AbstractPage {

    @FindBy(css = ".container")
    private WebElement resultListBlock;
    @FindBy(tagName = "nav")
    private WebElement navigationBar;

    public CreateChallengeStep1Page(WebDriver driver) {
        super(driver);
    }

    public SearchNavigationBarComponent getNavigationBar() {
        return new SearchNavigationBarComponent(driver, navigationBar);
    }

    public ResultListBlockComponent getResultListBlock() {
        return new ResultListBlockComponent(driver, resultListBlock);
    }
}
