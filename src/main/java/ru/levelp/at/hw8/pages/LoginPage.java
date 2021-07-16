package ru.levelp.at.hw8.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    @FindBy(id = "login")
    private WebElement loginField;
    @FindBy(id = "password")
    private WebElement passwordField;
    @FindBy(xpath = "//button[text()='Log in']")
    private WebElement loginButton;
    @FindBy(id = "no_account")
    private WebElement signupButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        open("login");
        return this;
    }

    public LoginPage enterLogin(String login) {
        loginField.sendKeys(login);
        return this;
    }

    public LoginPage enterPassword(String password) {
        passwordField.sendKeys(password);
        return this;
    }

    public MainPage clickLogin() {
        loginButton.click();
        return new MainPage(driver);
    }

    public SignupPage clickSignup() {
        signupButton.click();
        return new SignupPage(driver);
    }
}
