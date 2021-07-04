package ru.levelp.at.hw8.pages.components;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ResultListBlockComponent extends RootElementPageComponent {

    private static final String PAGE_BUTTONS_LOCATOR = ".btn-group button";

    public ResultListBlockComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public List<ListComponent> getItems() {
        return root.findElements(By.tagName("tr")).stream()
            .map(e -> new ListComponent(driver, e))
            .collect(Collectors.toList());
    }

    public void clickNextPage() {
        root.findElements(By.cssSelector(PAGE_BUTTONS_LOCATOR)).get(1).click();
    }

    public String getNextPageButtonStatus() {
        return root.findElements(By.cssSelector(PAGE_BUTTONS_LOCATOR)).get(1).getAttribute("disabled");
    }
}
