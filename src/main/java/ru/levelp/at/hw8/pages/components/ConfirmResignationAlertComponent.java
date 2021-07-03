package ru.levelp.at.hw8.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.levelp.at.hw8.pages.AbstractPageComponent;

public class ConfirmResignationAlertComponent extends AbstractPageComponent {

    private static final String CONFIRM_BUTTON_LOCATOR = "button[type=submit]";

    public ConfirmResignationAlertComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public void confirmResignation() {
        root.findElement(By.cssSelector(CONFIRM_BUTTON_LOCATOR)).click();
    }
}
