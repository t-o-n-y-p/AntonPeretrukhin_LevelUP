package ru.levelp.at.hw8.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ListComponent extends RootElementPageComponent {

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
