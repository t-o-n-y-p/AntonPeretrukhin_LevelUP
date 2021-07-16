package ru.levelp.at.hw8.pages.components;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ChessBoardComponent extends RootElementPageComponent {

    public ChessBoardComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public String getChessBoardState() {
        List<WebElement> squares = root.findElements(By.tagName("td"));
        return IntStream.range(0, 72)
            .filter(i -> i % 9 != 0)
            .mapToObj(i -> squares.get(i).getText())
            .collect(Collectors.joining(";"));
    }
}
