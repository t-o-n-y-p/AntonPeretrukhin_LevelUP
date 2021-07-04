package ru.levelp.at.hw8.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractPage extends AbstractPageComponent {

    private static final String BASE_URL = "http://localhost:8080/";

    protected AbstractPage(WebDriver driver) {
        super(driver);
    }

    protected void open(String url) {
        driver.navigate().to(BASE_URL + url);
    }

}
