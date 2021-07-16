package ru.levelp.at.hw8.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CreateChallengeStep2Page extends AbstractPage {

    @FindBy(xpath = "//button[text()='Create challenge']")
    private WebElement createChallengeButton;

    public CreateChallengeStep2Page(WebDriver driver) {
        super(driver);
    }

    public MainPage clickCreateChallenge() {
        createChallengeButton.click();
        return new MainPage(driver);
    }
}
