package ru.levelp.at.hw6;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.levelp.at.hw6.utils.PostgresqlConnectionUtil;
import ru.levelp.at.hw6.utils.User;

public class AcceptChallengeTest extends BaseTest {

    @BeforeTest
    public void beforeTest() {
        PostgresqlConnectionUtil.createTestChallenges();
    }

    @Test
    public void testAcceptChallenge() {
        User currentUser = USERS.get(0);
        driver.findElement(By.id("login")).sendKeys(currentUser.getLogin());
        driver.findElement(By.id("password")).sendKeys(currentUser.getPassword());
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        assertThat(driver.findElement(By.cssSelector("nav > a")).getText())
            .isEqualTo(String.join(" ", currentUser.getLogin(), "(1200.0)"));

        Set<String> challengerNames = driver.findElements(By.xpath("//div[contains(@class,'d-none')][1]//th"))
            .stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toSet());
        assertThat(challengerNames).containsOnly(
            USERS.stream()
                 .skip(4)
                 .map(User::getLogin)
                 .toArray(String[]::new)
        );

    }

}
