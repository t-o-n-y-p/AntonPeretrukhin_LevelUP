package ru.levelp.at.hw8.pages.components;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MainPageBlockComponent extends RootElementPageComponent {

    private static final String VIEW_ALL_BUTTON_LOCATOR = "./form/button";

    public MainPageBlockComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public List<ListComponent> getItems() {
        return root.findElements(By.tagName("tr")).stream()
            .map(e -> new ListComponent(driver, e))
            .collect(Collectors.toList());
    }

    public void clickViewAll() {
        root.findElement(By.xpath(VIEW_ALL_BUTTON_LOCATOR)).click();
    }
}
