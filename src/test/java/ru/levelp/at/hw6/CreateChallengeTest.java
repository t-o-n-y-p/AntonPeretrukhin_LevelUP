package ru.levelp.at.hw6;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import ru.levelp.at.hw6.utils.PostgresqlConnectionUtil;
import ru.levelp.at.hw6.utils.User;

public class CreateChallengeTest extends BaseTest {

    @Test
    public void testCreateChallenge() {
        User currentUser = USERS.get(1);
        login(currentUser);

        driver.findElement(By.linkText("Create challenge")).click();
        Set<String> actualUsernames = driver.findElements(By.tagName("th"))
            .stream()
            .map(th -> th.getText().split("\\s")[0])
            .collect(Collectors.toSet());
        assertThat(actualUsernames).hasSize(8);
        driver.findElement(By.xpath("//button[text()='Next page']")).click();
        actualUsernames.addAll(driver.findElements(By.tagName("th"))
            .stream()
            .map(th -> th.getText().split("\\s")[0])
            .collect(Collectors.toSet())
        );
        User finalCurrentUser = currentUser;
        assertThat(actualUsernames).containsExactlyInAnyOrder(
            USERS.stream().filter(u -> !u.equals(finalCurrentUser)).map(User::getLogin).toArray(String[]::new)
        );
        assertThat(driver.findElement(By.xpath("//button[text()='Next page']")).getAttribute("disabled"))
            .isEqualTo("true");

        driver.findElement(By.name("search")).sendKeys(USERS.get(0).getLogin());
        driver.findElement(By.cssSelector("nav > div button")).click();
        actualUsernames = driver.findElements(By.tagName("th"))
            .stream()
            .map(th -> th.getText().split("\\s")[0])
            .collect(Collectors.toSet());
        assertThat(actualUsernames).containsOnly(USERS.get(0).getLogin());

        driver.findElement(By.cssSelector("td button")).click();
        driver.findElement(By.xpath("//button[text()='Create challenge']")).click();
        WebElement successAlert = driver.findElement(By.className("alert-success"));
        assertThat(successAlert).hasFieldOrPropertyWithValue("text", "Challenge created.");

        driver.findElement(By.linkText("Logout")).click();
        currentUser = USERS.get(0);
        login(currentUser);
        Set<String> challengerNames = driver.findElements(By.xpath("//div[contains(@class,'d-none')][1]//th"))
            .stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toSet());
        assertThat(challengerNames).containsOnly(USERS.get(1).getLogin());

        driver.findElement(By.linkText("Logout")).click();
    }
}
