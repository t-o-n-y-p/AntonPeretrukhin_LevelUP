package ru.levelp.at.hw6.utils;

import java.util.Objects;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

@AllArgsConstructor
public class AttributeNotEqualToCondition implements ExpectedCondition<Boolean> {

    private final WebElement element;
    private final String attribute;
    private final String value;

    @NullableDecl
    @Override
    public Boolean apply(@NullableDecl WebDriver webDriver) {
        return !Objects.equals(element.getAttribute(attribute), value);
    }
}
