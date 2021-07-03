package ru.levelp.at.hw8.pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.levelp.at.hw8.pages.components.ListComponent;
import ru.levelp.at.hw8.pages.components.MainPageBlockComponent;
import ru.levelp.at.hw8.pages.components.RegularNavigationBarComponent;

public class MainPage extends AbstractPage {

    @FindBy(className = "alert-success")
    private WebElement successAlert;
    @FindBy(className = "d-none")
    private List<WebElement> blocks;
    @FindBy(tagName = "nav")
    private WebElement navigationBar;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public RegularNavigationBarComponent getNavigationBar() {
        return new RegularNavigationBarComponent(driver, navigationBar);
    }

    public MainPageBlockComponent getChallengeBlock() {
        return new MainPageBlockComponent(driver, blocks.get(0));
    }

    public MainPageBlockComponent getGameBlock() {
        return new MainPageBlockComponent(driver, blocks.get(1));
    }

    public String getSuccessAlertText() {
        return successAlert.getText();
    }
}
