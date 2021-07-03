package ru.levelp.at.hw8.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.levelp.at.hw8.pages.AbstractPage;
import ru.levelp.at.hw8.pages.MainPage;

public class SignupPage extends AbstractPage {

    @FindBy(id = "login")
    private WebElement loginField;
    @FindBy(id = "password")
    private WebElement passwordField;
    @FindBy(id = "repeat_password")
    private WebElement repeatPasswordField;
    @FindBy(id = "signup-button")
    private WebElement signupButton;

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    public SignupPage enterLogin(String login) {
        loginField.sendKeys(login);
        return this;
    }

    public SignupPage enterPassword(String password) {
        passwordField.sendKeys(password);
        return this;
    }

    public SignupPage enterRepeatPassword(String password) {
        repeatPasswordField.sendKeys(password);
        return this;
    }

    public MainPage clickSignup() {
        signupButton.click();
        return new MainPage(driver);
    }


}
