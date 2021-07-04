package ru.levelp.at.hw8.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchNavigationBarComponent extends RootElementPageComponent {

    public SearchNavigationBarComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public void fillSearchField(String searchString) {
        root.findElement(By.name("search")).sendKeys(searchString);
    }

    public void clickSearch() {
        root.findElement(By.tagName("button")).click();
    }

    public void clickMainPage() {
        root.findElement(By.linkText("Main page")).click();
    }
}
