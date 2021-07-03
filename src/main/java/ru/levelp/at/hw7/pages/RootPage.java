package ru.levelp.at.hw7.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RootPage extends AbstractPage {
    public RootPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage openAndLogout() {
        open("");
        driver.findElements(By.linkText("Logout")).stream()
              .findFirst()
              .ifPresent(WebElement::click);
        return new LoginPage(driver);
    }
}
