package ru.levelp.at.hw8.pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.levelp.at.hw8.pages.AbstractPageComponent;

public abstract class RootElementPageComponent extends AbstractPageComponent {

    protected final WebElement root;

    protected RootElementPageComponent(WebDriver driver, WebElement root) {
        super(driver);
        this.root = root;
    }

}
