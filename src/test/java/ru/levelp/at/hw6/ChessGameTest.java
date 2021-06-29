package ru.levelp.at.hw6;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.levelp.at.hw6.utils.AttributeNotEqualToCondition;
import ru.levelp.at.hw6.utils.PostgresqlConnectionUtil;
import ru.levelp.at.hw6.utils.User;

public class ChessGameTest extends BaseTest {

    @BeforeTest
    public void beforeTest() {
        PostgresqlConnectionUtil.createTestGame();
    }

    @Test
    public void testChessGame() {
        User currentUser = USERS.get(0);
        driver.findElement(By.id("login")).sendKeys(currentUser.getLogin());
        driver.findElement(By.id("password")).sendKeys(currentUser.getPassword());
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        assertThat(driver.findElement(By.cssSelector("nav > a")).getText())
            .startsWith(currentUser.getLogin());

        List<String> opponentNames = driver.findElements(By.xpath("//div[contains(@class,'d-none')][2]//th"))
            .stream()
            .map(th -> th.getText().split("\\s")[1])
            .collect(Collectors.toList());
        assertThat(opponentNames).containsOnly(USERS.get(1).getLogin());
        driver.findElements(By.xpath("//div[contains(@class,'d-none')][2]//button[text()='Open']")).get(0).click();

        assertThat(driver.findElements(By.cssSelector("#container > div"))).hasSize(2);
        List<WebElement> chessBoardSquares = driver.findElements(By.cssSelector("#chess-board td"));
        List<WebElement> finalChessBoardSquares = chessBoardSquares;
        String chessBoard = IntStream.range(0, 72)
                                     .filter(i -> i % 9 != 0)
                                     .mapToObj(i -> finalChessBoardSquares.get(i).getText())
                                     .collect(Collectors.joining(";"));
        assertThat(chessBoard).isEqualTo(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
                + ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"
                + "♙;♙;♙;♙;♙;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        Select square1 = new Select(driver.findElement(By.id("square1")));
        square1.selectByVisibleText("a1");
        Select square2 = new Select(driver.findElement(By.id("square2")));
        square2.selectByVisibleText("a1");
        driver.findElement(By.cssSelector("#make-move button")).click();
        WebElement illegalMoveAlert = driver.findElement(By.className("alert-warning"));
        assertThat(illegalMoveAlert).hasFieldOrPropertyWithValue(
            "text", "The move you made is illegal. Please make a legal move."
        );

        square1 = new Select(driver.findElement(By.id("square1")));
        square1.selectByVisibleText("e2");
        square2 = new Select(driver.findElement(By.id("square2")));
        square2.selectByVisibleText("e4");
        driver.findElement(By.cssSelector("#make-move button")).click();
        WebElement legalMoveAlert = driver.findElement(By.className("alert-success"));
        assertThat(legalMoveAlert).hasFieldOrPropertyWithValue(
            "text", "The move has been made."
        );
        WebElement opponentToMoveAlert = driver.findElement(By.className("alert-info"));
        assertThat(opponentToMoveAlert).hasFieldOrPropertyWithValue(
            "text", "Opponent to move."
        );
        chessBoardSquares = driver.findElements(By.cssSelector("#chess-board td"));
        List<WebElement> finalChessBoardSquares1 = chessBoardSquares;
        chessBoard = IntStream.range(0, 72)
                              .filter(i -> i % 9 != 0)
                              .mapToObj(i -> finalChessBoardSquares1.get(i).getText())
                              .collect(Collectors.joining(";"));
        assertThat(chessBoard).isEqualTo(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
                + ";;;;;;;;;;;;;;;;;;;;♙;;;;;;;;;;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        driver.findElement(By.linkText("Main page")).click();
        driver.findElement(By.linkText("Logout")).click();
        makeMove(
            USERS.get(1), "e7", "e5",
            "♖;♘;♗;♔;♕;♗;♘;♖;♙;♙;♙;;♙;♙;♙;♙;"
                + ";;;;;;;;;;;♙;;;;;;;;;;;;;;;;;;;;;"
                + "♟;♟;♟;♟;♟;♟;♟;♟;♜;♞;♝;♚;♛;♝;♞;♜",
            "♖;♘;♗;♔;♕;♗;♘;♖;♙;♙;♙;;♙;♙;♙;♙;"
                + ";;;;;;;;;;;♙;;;;;;;;♟;;;;;;;;;;;;;"
                + "♟;♟;♟;;♟;♟;♟;♟;♜;♞;♝;♚;♛;♝;♞;♜"
        );
        makeMove(
            USERS.get(0), "g1", "f3",
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖",
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );
        makeMove(
            USERS.get(1), "b8", "c6",
            "♖;;♗;♔;♕;♗;♘;♖;♙;♙;♙;;♙;♙;♙;♙;"
                + ";;♘;;;;;;;;;♙;;;;;;;;♟;;;;;;;;;;;;;"
                + "♟;♟;♟;;♟;♟;♟;♟;♜;♞;♝;♚;♛;♝;♞;♜",
            "♖;;♗;♔;♕;♗;♘;♖;♙;♙;♙;;♙;♙;♙;♙;"
                + ";;♘;;;;;;;;;♙;;;;;;;;♟;;;;;;;;;;♞;;;"
                + "♟;♟;♟;;♟;♟;♟;♟;♜;♞;♝;♚;♛;♝;;♜"
        );

        // hack to avoid test fails due to login button not clicked properly
        // wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Log in']"))) doesn't help
        driver.navigate().to("http://localhost:8080");
        currentUser = USERS.get(0);
        driver.findElement(By.id("login")).sendKeys(currentUser.getLogin());
        driver.findElement(By.id("password")).sendKeys(currentUser.getPassword());
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        assertThat(driver.findElement(By.cssSelector("nav > a")).getText())
            .startsWith(currentUser.getLogin());

        driver.findElements(By.xpath("//div[contains(@class,'d-none')][2]//button[text()='Open']")).get(0).click();
        assertThat(driver.findElements(By.cssSelector("#container > div"))).hasSize(3);

        WebElement previousMoveButton = driver.findElement(By.id("previous-move"));
        WebElement nextMoveButton = driver.findElement(By.id("next-move"));
        WebElement notationFakeButton = driver.findElement(By.id("move-notation"));

        assertThat(previousMoveButton.getAttribute("disabled")).isEqualTo(null);
        assertThat(nextMoveButton.getAttribute("disabled")).isEqualTo("true");
        assertThat(notationFakeButton).hasFieldOrPropertyWithValue("text", "2. ... b8c6");
        checkChessBoard("♜;;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
            + ";;♞;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
            + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );

        String oldPreviousMoveId = previousMoveButton.getAttribute("previousMove");
        String oldNextMoveId = nextMoveButton.getAttribute("nextMove");
        previousMoveButton.click();
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId));
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        assertThat(previousMoveButton.getAttribute("disabled")).isEqualTo(null);
        assertThat(nextMoveButton.getAttribute("disabled")).isEqualTo(null);
        assertThat(notationFakeButton).hasFieldOrPropertyWithValue("text", "2. g1f3");
        checkChessBoard("♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
            + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
            + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );

        oldPreviousMoveId = previousMoveButton.getAttribute("previousMove");
        oldNextMoveId = nextMoveButton.getAttribute("nextMove");
        previousMoveButton.click();
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId));
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        assertThat(previousMoveButton.getAttribute("disabled")).isEqualTo(null);
        assertThat(nextMoveButton.getAttribute("disabled")).isEqualTo(null);
        assertThat(notationFakeButton).hasFieldOrPropertyWithValue("text", "1. ... e7e5");
        checkChessBoard("♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
            + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;;;;"
            + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        oldNextMoveId = nextMoveButton.getAttribute("nextMove");
        previousMoveButton.click();
        wait.until(ExpectedConditions.attributeToBe(previousMoveButton, "disabled", "true"));
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        assertThat(nextMoveButton.getAttribute("disabled")).isEqualTo(null);
        assertThat(notationFakeButton).hasFieldOrPropertyWithValue("text", "1. e2e4");
        checkChessBoard("♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
            + ";;;;;;;;;;;;;;;;;;;;♙;;;;;;;;;;;;"
            + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        oldNextMoveId = nextMoveButton.getAttribute("nextMove");
        nextMoveButton.click();
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "disabled", "true"));
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        assertThat(nextMoveButton.getAttribute("disabled")).isEqualTo(null);
        assertThat(notationFakeButton).hasFieldOrPropertyWithValue("text", "1. ... e7e5");
        checkChessBoard("♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
            + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;;;;"
            + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        oldPreviousMoveId = previousMoveButton.getAttribute("previousMove");
        oldNextMoveId = nextMoveButton.getAttribute("nextMove");
        nextMoveButton.click();
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId));
        wait.until(new AttributeNotEqualToCondition(nextMoveButton, "nextMoveId", oldNextMoveId));
        assertThat(previousMoveButton.getAttribute("disabled")).isEqualTo(null);
        assertThat(nextMoveButton.getAttribute("disabled")).isEqualTo(null);
        assertThat(notationFakeButton).hasFieldOrPropertyWithValue("text", "2. g1f3");
        checkChessBoard("♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
            + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
            + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );

        oldPreviousMoveId = previousMoveButton.getAttribute("previousMove");
        nextMoveButton.click();
        wait.until(new AttributeNotEqualToCondition(previousMoveButton, "previousMoveId", oldPreviousMoveId));
        wait.until(ExpectedConditions.attributeToBe(nextMoveButton, "disabled", "true"));
        assertThat(previousMoveButton.getAttribute("disabled")).isEqualTo(null);
        assertThat(notationFakeButton).hasFieldOrPropertyWithValue("text", "2. ... b8c6");
        checkChessBoard("♜;;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
            + ";;♞;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
            + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );

        driver.findElement(By.xpath("//button[text()='Resign']")).click();
        wait.until(ExpectedConditions.attributeToBe(By.id("resign-modal"), "aria-hidden", ""));
        driver.findElement(By.xpath("//button[text()='Resign the game']")).click();
        WebElement resignAlert = driver.findElement(By.className("alert-info"));
        assertThat(resignAlert).hasFieldOrPropertyWithValue(
            "text", "Black won by resignation"
        );
        driver.findElement(By.linkText("Main page")).click();
        driver.findElement(By.linkText("Logout")).click();

        currentUser = USERS.get(1);
        // hack to avoid test fails due to login button not clicked properly
        // wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Log in']"))) doesn't help
        driver.navigate().to("http://localhost:8080");
        driver.findElement(By.id("login")).sendKeys(currentUser.getLogin());
        driver.findElement(By.id("password")).sendKeys(currentUser.getPassword());
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        assertThat(driver.findElement(By.cssSelector("nav > a")).getText())
            .startsWith(currentUser.getLogin());
        driver.findElements(By.xpath("//div[contains(@class,'d-none')][2]//button[text()='Open']")).get(0).click();
        resignAlert = driver.findElement(By.className("alert-info"));
        assertThat(resignAlert).hasFieldOrPropertyWithValue(
            "text", "Black won by resignation"
        );

        driver.findElement(By.linkText("Main page")).click();
        driver.findElement(By.linkText("Logout")).click();
    }

    private void makeMove(User currentUser, String from, String to, String chessBoardBefore, String chessBoardAfter) {
        // hack to avoid test fails due to login button not clicked properly
        // wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Log in']"))) doesn't help
        driver.navigate().to("http://localhost:8080");
        driver.findElement(By.id("login")).sendKeys(currentUser.getLogin());
        driver.findElement(By.id("password")).sendKeys(currentUser.getPassword());
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        assertThat(driver.findElement(By.cssSelector("nav > a")).getText())
            .startsWith(currentUser.getLogin());
        driver.findElements(By.xpath("//div[contains(@class,'d-none')][2]//button[text()='Open']")).get(0).click();

        assertThat(driver.findElements(By.cssSelector("#container > div"))).hasSize(3);
        checkChessBoard(chessBoardBefore);

        Select square1 = new Select(driver.findElement(By.id("square1")));
        square1.selectByVisibleText(from);
        Select square2 = new Select(driver.findElement(By.id("square2")));
        square2.selectByVisibleText(to);
        driver.findElement(By.cssSelector("#make-move button")).click();
        WebElement legalMoveAlert = driver.findElement(By.className("alert-success"));
        assertThat(legalMoveAlert).hasFieldOrPropertyWithValue(
            "text", "The move has been made."
        );
        WebElement opponentToMoveAlert = driver.findElement(By.className("alert-info"));
        assertThat(opponentToMoveAlert).hasFieldOrPropertyWithValue(
            "text", "Opponent to move."
        );
        checkChessBoard(chessBoardAfter);

        driver.findElement(By.linkText("Main page")).click();
        driver.findElement(By.linkText("Logout")).click();
    }

    private void checkChessBoard(String expectedBoard) {
        List<WebElement> chessBoardSquares = driver.findElements(By.cssSelector("#chess-board td"));
        String chessBoard = IntStream.range(0, 72)
                                     .filter(i -> i % 9 != 0)
                                     .mapToObj(i -> chessBoardSquares.get(i).getText())
                                     .collect(Collectors.joining(";"));
        assertThat(chessBoard).isEqualTo(expectedBoard);
    }
}
