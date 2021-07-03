package ru.levelp.at.hw8.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.levelp.at.hw8.pages.AbstractPageComponent;

public class SearchNavigationBarComponent extends AbstractPageComponent {

    public SearchNavigationBarComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public void fillSearchField(String searchString) {
        root.findElement(By.name("search")).sendKeys(searchString);
    }

    public void clickSearch() {
        root.findElement(By.tagName("button")).click();
    }
}
