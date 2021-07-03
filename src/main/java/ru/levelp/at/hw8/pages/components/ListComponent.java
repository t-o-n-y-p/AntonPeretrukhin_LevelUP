package ru.levelp.at.hw8.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.levelp.at.hw8.pages.AbstractPageComponent;

public class ListComponent extends AbstractPageComponent {

    public ListComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public String getTitle() {
        return root.findElement(By.tagName("th")).getText();
    }

    public void clickComponentButton() {
        root.findElement(By.tagName("button")).click();
    }
}
