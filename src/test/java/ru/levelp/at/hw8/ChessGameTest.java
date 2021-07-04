package ru.levelp.at.hw8;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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
        actionStep.login(USERS.get(0));
        assertionStep.assertThatLoginIsSuccessful(USERS.get(0));
        assertionStep.assertGameListContainsExactly(actionStep.getGameNamesFromMainPage(), List.of(USERS.get(1)));

        actionStep.openGameFromMainPage(0);
        assertionStep.assertGameMoveFormIsNotEmpty();
        assertionStep.assertChessBoardStateEquals(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
                + ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"
                + "♙;♙;♙;♙;♙;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        actionStep.makeMove("a1", "a1");
        assertionStep.assertIllegalMoveAlert();
        actionStep.makeMove("e2", "e4");
        assertionStep.assertLegalMoveAlert();
        assertionStep.assertOpponentToMoveAlert();
        assertionStep.assertChessBoardStateEquals(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
                + ";;;;;;;;;;;;;;;;;;;;♙;;;;;;;;;;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        actionStep.logoutFromGamePage();
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

        actionStep.login(USERS.get(0));
        assertionStep.assertThatLoginIsSuccessful(USERS.get(0));
        actionStep.openGameFromMainPage(0);
        assertionStep.assertNotationEquals("2. ... b8c6");
        assertionStep.assertChessBoardStateEquals(
            "♜;;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;♞;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );
        actionStep.clickPreviousMoveOnLastMove();
        assertionStep.assertNotationEquals("2. g1f3");
        assertionStep.assertChessBoardStateEquals(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );
        actionStep.clickPreviousMove();
        assertionStep.assertNotationEquals("1. ... e7e5");
        assertionStep.assertChessBoardStateEquals(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );
        actionStep.clickPreviousMoveOnSecondMove();
        assertionStep.assertNotationEquals("1. e2e4");
        assertionStep.assertChessBoardStateEquals(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
                + ";;;;;;;;;;;;;;;;;;;;♙;;;;;;;;;;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );
        actionStep.clickNextMoveOnFirstMove();
        assertionStep.assertNotationEquals("1. ... e7e5");
        assertionStep.assertChessBoardStateEquals(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );
        actionStep.clickNextMove();
        assertionStep.assertNotationEquals("2. g1f3");
        assertionStep.assertChessBoardStateEquals(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );
        actionStep.clickNextMoveOnSecondToLastMove();
        assertionStep.assertNotationEquals("2. ... b8c6");
        assertionStep.assertChessBoardStateEquals(
            "♜;;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;;♟;♟;♟;"
                + ";;♞;;;;;;;;;;♟;;;;;;;;♙;;;;;;;;;♘;;;"
                + "♙;♙;♙;♙;;♙;♙;♙;♖;♘;♗;♕;♔;♗;;♖"
        );
        actionStep.resignTheGame();
        assertionStep.assertGameResultAlert("Black won by resignation");
        actionStep.logoutFromGamePage();

        actionStep.login(USERS.get(1));
        actionStep.openGameFromMainPage(0);
        assertionStep.assertGameResultAlert("Black won by resignation");
        actionStep.logoutFromGamePage();
    }

    private void makeMove(User currentUser, String from, String to, String chessBoardBefore, String chessBoardAfter) {
        actionStep.login(currentUser);
        actionStep.openGameFromMainPage(0);
        assertionStep.assertChessBoardStateEquals(chessBoardBefore);
        actionStep.makeMove(from, to);
        assertionStep.assertLegalMoveAlert();
        assertionStep.assertOpponentToMoveAlert();
        assertionStep.assertChessBoardStateEquals(chessBoardAfter);
        actionStep.logoutFromGamePage();
    }

}
