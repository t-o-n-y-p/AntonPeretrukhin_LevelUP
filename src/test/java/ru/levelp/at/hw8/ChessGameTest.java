package ru.levelp.at.hw8;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.levelp.at.hw8.pages.GamePage;
import ru.levelp.at.hw8.pages.LoginPage;
import ru.levelp.at.hw8.pages.MainPage;
import ru.levelp.at.hw8.utils.PostgresqlConnectionUtil;
import ru.levelp.at.hw8.utils.User;

public class ChessGameTest extends BaseTest {

    @BeforeTest
    public void beforeTest() {
        PostgresqlConnectionUtil.createTestGame();
    }

    @Test
    public void testChessGame() {
        User currentUser = USERS.get(0);
        MainPage mainPage = new LoginPage(driver)
            .enterLogin(currentUser.getLogin())
            .enterPassword(currentUser.getPassword())
            .clickLogin();
        assertThat(mainPage.getNavigationTitle()).startsWith(USERS.get(0).getLogin() + " (");
        assertThat(mainPage.getOpponentNames()).containsOnly(USERS.get(1).getLogin());

        GamePage gamePage = mainPage.openGame(0);
        assertThat(gamePage.getMakeMoveForms()).hasSize(2);
        assertThat(gamePage.getChessBoard()).isEqualTo(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
                + ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"
                + "♙;♙;♙;♙;♙;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        gamePage = gamePage
            .selectFromSquare("a1")
            .selectToSquare("a1")
            .clickMakeMove();
        assertThat(gamePage.getWarningAlertText()).isEqualTo("The move you made is illegal. Please make a legal move.");

        gamePage = gamePage
            .selectFromSquare("e2")
            .selectToSquare("e4")
            .clickMakeMove();
        assertThat(gamePage.getSuccessAlertText()).isEqualTo("The move has been made.");
        assertThat(gamePage.getInfoAlertText()).isEqualTo("Opponent to move.");
        assertThat(gamePage.getChessBoard()).isEqualTo(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
                + ";;;;;;;;;;;;;;;;;;;;♙;;;;;;;;;;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        gamePage.clickMainPage().clickLogout();
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

        currentUser = USERS.get(0);
        mainPage = new LoginPage(driver)
            .enterLogin(currentUser.getLogin())
            .enterPassword(currentUser.getPassword())
            .clickLogin();
        assertThat(mainPage.getNavigationTitle()).startsWith(USERS.get(0).getLogin() + " (");

        gamePage = mainPage
            .openGame(0);
        assertThat(gamePage.getNotation()).isEqualTo("2. ... b8c6");
        assertThat(gamePage.getChessBoard()).isEqualTo(
            "♜;;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;♞;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );

        gamePage = gamePage.clickPreviousMoveOnLastMove();
        assertThat(gamePage.getNotation()).isEqualTo("2. g1f3");
        assertThat(gamePage.getChessBoard()).isEqualTo(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );

        gamePage = gamePage.clickPreviousMove();
        assertThat(gamePage.getNotation()).isEqualTo("1. ... e7e5");
        assertThat(gamePage.getChessBoard()).isEqualTo(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        gamePage = gamePage.clickPreviousMoveOnSecondMove();
        assertThat(gamePage.getNotation()).isEqualTo("1. e2e4");
        assertThat(gamePage.getChessBoard()).isEqualTo(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
                + ";;;;;;;;;;;;;;;;;;;;♙;;;;;;;;;;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        gamePage = gamePage.clickNextMoveOnFirstMove();
        assertThat(gamePage.getNotation()).isEqualTo("1. ... e7e5");
        assertThat(gamePage.getChessBoard()).isEqualTo(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        gamePage = gamePage.clickNextMove();
        assertThat(gamePage.getNotation()).isEqualTo("2. g1f3");
        assertThat(gamePage.getChessBoard()).isEqualTo(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );

        gamePage = gamePage.clickNextMoveOnSecondToLastMove();
        assertThat(gamePage.getNotation()).isEqualTo("2. ... b8c6");
        assertThat(gamePage.getChessBoard()).isEqualTo(
            "♜;;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;♞;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );
        gamePage = gamePage.clickResign().confirmResignation();
        assertThat(gamePage.getInfoAlertText()).isEqualTo("Black won by resignation");
        gamePage.clickMainPage().clickLogout();

        currentUser = USERS.get(1);
        gamePage = new LoginPage(driver)
            .enterLogin(currentUser.getLogin())
            .enterPassword(currentUser.getPassword())
            .clickLogin()
            .openGame(0);
        assertThat(gamePage.getInfoAlertText()).isEqualTo("Black won by resignation");
        gamePage.clickMainPage().clickLogout();
    }

    private void makeMove(User currentUser, String from, String to, String chessBoardBefore, String chessBoardAfter) {
        GamePage gamePage = new LoginPage(driver)
            .enterLogin(currentUser.getLogin())
            .enterPassword(currentUser.getPassword())
            .clickLogin()
            .openGame(0);
        assertThat(gamePage.getChessBoard()).isEqualTo(chessBoardBefore);

        gamePage = gamePage.selectFromSquare(from)
            .selectToSquare(to)
            .clickMakeMove();
        assertThat(gamePage.getSuccessAlertText()).isEqualTo("The move has been made.");
        assertThat(gamePage.getInfoAlertText()).isEqualTo("Opponent to move.");
        assertThat(gamePage.getChessBoard()).isEqualTo(chessBoardAfter);

        gamePage.clickMainPage().clickLogout();
    }

}
