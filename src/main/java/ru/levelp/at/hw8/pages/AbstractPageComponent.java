package ru.levelp.at.hw8.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class AbstractPageComponent extends AbstractPage {

    protected final WebElement root;

    public AbstractPageComponent(WebDriver driver, WebElement root) {
        super(driver);
        this.root = root;
    }
}
