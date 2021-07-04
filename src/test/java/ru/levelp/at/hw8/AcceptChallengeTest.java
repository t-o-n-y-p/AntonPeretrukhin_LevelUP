package ru.levelp.at.hw8;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
        actionStep.login(USERS.get(0));
        assertionStep.assertThatLoginIsSuccessful(USERS.get(0));
        List<User> expectedChallengers = new ArrayList<>(USERS.subList(4, 12));
        Collections.reverse(expectedChallengers);
        assertionStep.assertChallengesListContainsExactly(
            actionStep.getChallengeNamesFromMainPage(), expectedChallengers
        );

        actionStep.viewAllIncomingChallenges();
        List<String> challengeNames = actionStep.getChallengeNames();
        assertionStep.assertNumberOfChallengesOnPage(8);
        actionStep.openNextChallengePage();
        challengeNames.addAll(actionStep.getChallengeNames());
        assertionStep.assertNumberOfChallengesOnPage(2);
        assertionStep.assertThatChallengeNextPageButtonIsDisabled();
        expectedChallengers = new ArrayList<>(USERS.subList(2, 12));
        Collections.reverse(expectedChallengers);
        assertionStep.assertChallengesListContainsExactly(challengeNames, expectedChallengers);

        actionStep.searchChallengeByName(USERS.get(11).getLogin());
        assertionStep.assertChallengesListContainsExactly(actionStep.getChallengeNames(), USERS.subList(11, 12));

        actionStep.acceptChallenge(0);
        assertionStep.assertChallengeAcceptedAlert();

        actionStep.clickMainPageFromChallengeList();
        assertionStep.assertGameListContainsExactly(actionStep.getGameNamesFromMainPage(), USERS.subList(11, 12));

        actionStep.openGameFromMainPage(0);
        assertionStep.assertOpponentToMoveAlert();
        assertionStep.assertGameMoveFormIsEmpty();
        assertionStep.assertChessBoardStateEquals(
            "♖;♘;♗;♔;♕;♗;♘;♖;♙;♙;♙;♙;♙;♙;♙;♙;"
                + ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"
                + "♟;♟;♟;♟;♟;♟;♟;♟;♜;♞;♝;♚;♛;♝;♞;♜"
        );

        actionStep.clickMainPageFromGame();
        expectedChallengers = new ArrayList<>(USERS.subList(3, 11));
        Collections.reverse(expectedChallengers);
        assertionStep.assertChallengesListContainsExactly(
            actionStep.getChallengeNamesFromMainPage(), expectedChallengers
        );

        actionStep.acceptChallengeFromMainPage(0);
        assertionStep.assertChallengeAcceptedAlertOnMainPage();
        expectedChallengers = new ArrayList<>(USERS.subList(2, 10));
        Collections.reverse(expectedChallengers);
        assertionStep.assertChallengesListContainsExactly(
            actionStep.getChallengeNamesFromMainPage(), expectedChallengers
        );
        assertionStep.assertGameListContainsExactly(actionStep.getGameNamesFromMainPage(), USERS.subList(10, 12));


        actionStep.openGameFromMainPage(0);
        assertionStep.assertGameMoveFormIsNotEmpty();
        assertionStep.assertChessBoardStateEquals(
            "♜;♞;♝;♛;♚;♝;♞;♜;♟;♟;♟;♟;♟;♟;♟;♟;"
                + ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"
                + "♙;♙;♙;♙;♙;♙;♙;♙;♖;♘;♗;♕;♔;♗;♘;♖"
        );

        actionStep.logoutFromGamePage();
    }

}
