package ru.levelp.at.hw6;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

        List<String> challengerNames = driver.findElements(By.xpath("//div[contains(@class,'d-none')][1]//th"))
            .stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toList());
        assertThat(challengerNames).containsExactly(
            IntStream.range(0, 8)
                     .mapToObj(i -> USERS.get(11 - i).getLogin())
                     .toArray(String[]::new)
        );
        driver.findElement(
            By.xpath("//div[contains(@class,'d-none')]//button[text()='View all incoming challenges']")
        ).click();
        assertThat(driver.findElements(By.tagName("th")).stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toSet())
        ).containsExactlyInAnyOrder(challengerNames.toArray(String[]::new));
        driver.findElement(By.xpath("//button[text()='Next page']")).click();
        challengerNames.addAll(driver.findElements(By.tagName("th")).stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toList())
        );
        assertThat(challengerNames).containsExactly(
            IntStream.range(0, 10)
                     .mapToObj(i -> USERS.get(11 - i).getLogin())
                     .toArray(String[]::new)
        );

        driver.findElement(By.name("search")).sendKeys(USERS.get(11).getLogin());
        driver.findElement(By.cssSelector("nav > div button")).click();
        challengerNames = driver.findElements(By.tagName("th"))
            .stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toList());
        assertThat(challengerNames).containsOnly(USERS.get(11).getLogin());

        driver.findElement(By.cssSelector("td button")).click();
        WebElement successAlert = driver.findElement(By.className("alert-success"));
        assertThat(successAlert).hasFieldOrPropertyWithValue("text", "Challenge accepted.");

        driver.findElement(By.linkText("Main page")).click();
        List<String> opponentNames = driver.findElements(By.xpath("//div[contains(@class,'d-none')][2]//th"))
            .stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toList());
        assertThat(opponentNames).containsOnly(USERS.get(11).getLogin());

        driver.findElements(By.xpath("//div[contains(@class,'d-none')][2]//button[text()='Open']")).get(0).click();
        WebElement opponentToMove = driver.findElement(By.className("alert-info"));
        assertThat(opponentToMove).hasFieldOrPropertyWithValue("text", "Opponent to move.");
        assertThat(driver.findElements(By.cssSelector("#container > div"))).hasSize(1);
        List<WebElement> chessBoardSquares = driver.findElements(By.cssSelector("#chess-board td"));
        List<WebElement> finalChessBoardSquares = chessBoardSquares;
        String chessBoard = IntStream.range(0, 72)
                                     .filter(i -> i % 9 != 0)
                                     .mapToObj(i -> finalChessBoardSquares.get(i).getText())
                                     .collect(Collectors.joining(";"));
        assertThat(chessBoard).isEqualTo(
            "♖;♘;♗;♔;♕;♗;♘;♖;♙;♙;♙;♙;♙;♙;♙;♙;"
            + ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"
            + "♟;♟;♟;♟;♟;♟;♟;♟;♜;♞;♝;♚;♛;♝;♞;♜"
        );

        driver.findElement(By.linkText("Main page")).click();
        challengerNames = driver.findElements(By.xpath("//div[contains(@class,'d-none')][1]//th"))
            .stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toList());
        assertThat(challengerNames).containsExactly(
            IntStream.range(1, 9)
                     .mapToObj(i -> USERS.get(11 - i).getLogin())
                     .toArray(String[]::new)
        );
        driver.findElements(By.xpath("//div[contains(@class,'d-none')][1]//button[text()='Accept']")).get(0).click();
        successAlert = driver.findElement(By.className("alert-success"));
        assertThat(successAlert).hasFieldOrPropertyWithValue("text", "Challenge accepted.");
        challengerNames = driver.findElements(By.xpath("//div[contains(@class,'d-none')][1]//th"))
                                .stream()
                                .map(th -> th.getText().split("\\s")[1])
                                .collect(Collectors.toList());
        assertThat(challengerNames).containsExactly(
            IntStream.range(2, 10)
                     .mapToObj(i -> USERS.get(11 - i).getLogin())
                     .toArray(String[]::new)
        );
        opponentNames = driver.findElements(By.xpath("//div[contains(@class,'d-none')][2]//th"))
            .stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toList());
        assertThat(opponentNames).containsExactly(
            IntStream.range(0, 2)
                     .mapToObj(i -> USERS.get(10 + i).getLogin())
                     .toArray(String[]::new)
        );

        driver.findElements(By.xpath("//div[contains(@class,'d-none')][2]//button[text()='Open']")).get(0).click();
        assertThat(driver.findElements(By.cssSelector("#container > div"))).hasSize(2);
        chessBoardSquares = driver.findElements(By.cssSelector("#chess-board td"));
        List<WebElement> finalChessBoardSquares1 = chessBoardSquares;
        chessBoard = IntStream.range(0, 72)
                              .filter(i -> i % 9 != 0)
                              .mapToObj(i -> finalChessBoardSquares1.get(i).getText())
                              .collect(Collectors.joining(";"));
        assertThat(chessBoard).isEqualTo(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
                + ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"
                + "♙;♙;♙;♙;♙;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        driver.findElement(By.linkText("Main page")).click();
        driver.findElement(By.linkText("Logout")).click();
    }

}
