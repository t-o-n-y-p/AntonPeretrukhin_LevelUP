package ru.levelp.at.hw8.pages.components;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.levelp.at.hw8.pages.AbstractPageComponent;

public class MainPageBlockComponent extends AbstractPageComponent {

    private static final String VIEW_ALL_BUTTON_LOCATOR = ":root > form button";

    public MainPageBlockComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public List<ListComponent> getItems() {
        return root.findElements(By.tagName("tr")).stream()
            .map(e -> new ListComponent(driver, e))
            .collect(Collectors.toList());
    }

    public void clickViewAll() {
        root.findElement(By.cssSelector(VIEW_ALL_BUTTON_LOCATOR)).click();
    }
}
