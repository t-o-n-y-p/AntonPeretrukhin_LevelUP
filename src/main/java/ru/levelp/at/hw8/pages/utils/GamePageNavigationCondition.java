package ru.levelp.at.hw8.pages.utils;

import java.util.Objects;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

@AllArgsConstructor
public class GamePageNavigationCondition implements ExpectedCondition<Boolean> {

    private final WebElement previousMoveButton;
    private final WebElement nextMoveButton;
    private final String oldPreviousMoveId;
    private final String oldNextMoveId;

    @NullableDecl
    @Override
    public Boolean apply(@NullableDecl WebDriver webDriver) {
        return !Objects.equals(previousMoveButton.getAttribute("previousMoveId"), oldPreviousMoveId)
            && !Objects.equals(nextMoveButton.getAttribute("nextMoveId"), oldNextMoveId);
    }
}
