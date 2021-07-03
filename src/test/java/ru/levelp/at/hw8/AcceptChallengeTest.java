package ru.levelp.at.hw8;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.IntStream;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.levelp.at.hw8.pages.ChallengesListPage;
import ru.levelp.at.hw8.pages.GamePage;
import ru.levelp.at.hw8.pages.LoginPage;
import ru.levelp.at.hw8.pages.MainPage;
import ru.levelp.at.hw8.utils.PostgresqlConnectionUtil;
import ru.levelp.at.hw8.utils.User;

public class AcceptChallengeTest extends BaseTest {

    @BeforeTest
    public void beforeTest() {
        PostgresqlConnectionUtil.createTestChallenges();
    }

    @Test
    public void testAcceptChallenge() {
        User currentUser = USERS.get(0);
        MainPage mainPage = new LoginPage(driver)
            .enterLogin(currentUser.getLogin())
            .enterPassword(currentUser.getPassword())
            .clickLogin();
        assertThat(mainPage.getNavigationTitle()).startsWith(USERS.get(0).getLogin() + " (");
        assertThat(mainPage.getChallengerNames()).containsExactly(
            IntStream.range(0, 8)
                     .mapToObj(i -> USERS.get(11 - i).getLogin())
                     .toArray(String[]::new)
        );
        ChallengesListPage challengesListPage = mainPage.clickViewAllIncomingChallenges();
        List<String> challengerNames = challengesListPage.getChallengerNames();
        assertThat(challengerNames).hasSize(8);

        challengesListPage = challengesListPage.openNextPage();
        challengerNames.addAll(challengesListPage.getChallengerNames());
        assertThat(challengerNames).containsExactly(
            IntStream.range(0, 10)
                     .mapToObj(i -> USERS.get(11 - i).getLogin())
                     .toArray(String[]::new)
        );
        assertThat(challengesListPage.getNextButtonDisabledState()).isEqualTo("true");

        challengesListPage = challengesListPage.fillSearchField(USERS.get(11).getLogin()).clickSearch();
        assertThat(challengesListPage.getChallengerNames()).containsOnly(USERS.get(11).getLogin());

        challengesListPage = challengesListPage.acceptOpponent(0);
        assertThat(challengesListPage.getSuccessAlertText()).isEqualTo("Challenge accepted.");

        mainPage = challengesListPage.clickMainPage();
        assertThat(mainPage.getOpponentNames()).containsOnly(USERS.get(11).getLogin());

        GamePage gamePage = mainPage.openGame(0);
        assertThat(gamePage.getInfoAlertText()).isEqualTo("Opponent to move.");
        assertThat(gamePage.getMakeMoveForms()).hasSize(0);
        assertThat(gamePage.getChessBoard()).isEqualTo(
            "♖;♘;♗;♔;♕;♗;♘;♖;♙;♙;♙;♙;♙;♙;♙;♙;"
                + ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"
                + "♟;♟;♟;♟;♟;♟;♟;♟;♜;♞;♝;♚;♛;♝;♞;♜"
        );

        mainPage = gamePage.clickMainPage();
        assertThat(mainPage.getChallengerNames()).containsExactly(
            IntStream.range(1, 9)
                     .mapToObj(i -> USERS.get(11 - i).getLogin())
                     .toArray(String[]::new)
        );

        mainPage = mainPage.acceptChallenge(0);
        assertThat(mainPage.getSuccessAlertText()).isEqualTo("Challenge accepted.");
        assertThat(mainPage.getChallengerNames()).containsExactly(
            IntStream.range(2, 10)
                     .mapToObj(i -> USERS.get(11 - i).getLogin())
                     .toArray(String[]::new)
        );
        assertThat(mainPage.getOpponentNames()).containsExactly(
            IntStream.range(0, 2)
                     .mapToObj(i -> USERS.get(10 + i).getLogin())
                     .toArray(String[]::new)
        );

        gamePage = mainPage.openGame(0);
        assertThat(gamePage.getMakeMoveForms()).hasSize(2);
        assertThat(gamePage.getChessBoard()).isEqualTo(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
                + ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"
                + "♙;♙;♙;♙;♙;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        gamePage.clickMainPage().clickLogout();
    }

}
