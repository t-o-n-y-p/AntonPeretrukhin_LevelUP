package ru.levelp.at.hw8.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractPage {

    private static final String BASE_URL = "http://localhost:8080/";

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected AbstractPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
        PageFactory.initElements(this.driver, this);
    }

    protected void open(String url) {
        driver.navigate().to(BASE_URL + url);
    }

}
