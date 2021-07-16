package ru.levelp.at.hw8.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RegularNavigationBarComponent extends RootElementPageComponent {

    public RegularNavigationBarComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public String getTitle() {
        return root.findElement(By.className("navbar-brand")).getText();
    }

    public void clickLinkByName(String name) {
        root.findElement(By.linkText(name)).click();
    }

}
