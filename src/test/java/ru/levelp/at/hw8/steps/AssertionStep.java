package ru.levelp.at.hw8.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Step;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import ru.levelp.at.hw8.utils.User;

public class AssertionStep extends AbstractStep {
    public AssertionStep(WebDriver driver) {
        super(driver);
    }

    @Step("Verify successful login to user: '{0}'")
    public void assertThatLoginIsSuccessful(User user) {
        assertThat(mainPage.getNavigationBar().getTitle()).startsWith(user.getLogin() + " (");
    }

    @Step("Verify there are '{0}' opponents on the page")
    public void assertNumberOfOpponentsOnPage(int expectedSize) {
        assertThat(createChallengeStep1Page.getResultListBlock().getItems()).hasSize(expectedSize);
    }

    @Step("Verify there are '{0}' challenges on the page")
    public void assertNumberOfChallengesOnPage(int expectedSize) {
        assertThat(challengesListPage.getResultListBlock().getItems()).hasSize(expectedSize);
    }

    @Step("Verify the opponents are: '{users}'")
    public void assertOpponentSetContainsExactly(Set<String> names, List<User> users) {
        assertThat(
            names.stream()
                .map(name -> name.split("\\s")[0])
                .collect(Collectors.toSet())
        ).containsExactly(
            users.stream().map(User::getLogin).toArray(String[]::new)
        );
    }

    @Step("Verify the opponents are (in any order): '{users}'")
    public void assertOpponentSetContainsExactlyInAnyOrder(Set<String> names, List<User> users) {
        assertThat(
            names.stream()
                .map(name -> name.split("\\s")[0])
                .collect(Collectors.toSet())
        ).containsExactlyInAnyOrder(
            users.stream().map(User::getLogin).toArray(String[]::new)
        );
    }

    @Step("Verify \"Next page\" button is disabled")
    public void assertThatOpponentNextPageButtonIsDisabled() {
        assertThat(createChallengeStep1Page.getResultListBlock().getNextPageButtonStatus()).isEqualTo("true");
    }

    @Step("Verify \"Next page\" button is disabled")
    public void assertThatChallengeNextPageButtonIsDisabled() {
        assertThat(challengesListPage.getResultListBlock().getNextPageButtonStatus()).isEqualTo("true");
    }

    @Step("Verify the challenges are from: '{users}'")
    public void assertChallengesListContainsExactly(List<String> names, List<User> users) {
        assertThat(
            names.stream()
                .map(name -> name.split("\\s")[1])
                .collect(Collectors.toList())
        ).containsExactly(
            users.stream().map(User::getLogin).toArray(String[]::new)
        );
    }

    @Step("Verify the \"Challenge created.\" alert is there")
    public void assertChallengeCreatedAlertOnMainPage() {
        assertThat(mainPage.getSuccessAlertText()).isEqualTo("Challenge created.");
    }

    @Step("Verify the \"Challenge accepted.\" alert is there")
    public void assertChallengeAcceptedAlert() {
        assertThat(challengesListPage.getSuccessAlertText()).isEqualTo("Challenge accepted.");
    }

    @Step("Verify the \"Challenge accepted.\" alert is there")
    public void assertChallengeAcceptedAlertOnMainPage() {
        assertThat(mainPage.getSuccessAlertText()).isEqualTo("Challenge accepted.");
    }

    @Step("Verify the games are with: '{users}'")
    public void assertGameListContainsExactly(List<String> names, List<User> users) {
        assertThat(
            names.stream()
                 .map(name -> name.split("\\s")[1])
                 .collect(Collectors.toList())
        ).containsExactly(
            users.stream().map(User::getLogin).toArray(String[]::new)
        );
    }

    @Step("Verify the \"Opponent to move.\" alert is there")
    public void assertOpponentToMoveAlert() {
        assertThat(gamePage.getInfoAlertText()).isEqualTo("Opponent to move.");
    }

    @Step("Verify the \"'{0}'\" alert is there")
    public void assertGameResultAlert(String outcome) {
        assertThat(gamePage.getInfoAlertText()).isEqualTo(outcome);
    }

    @Step("Verify the \"The move you made is illegal. Please make a legal move.\" alert is there")
    public void assertIllegalMoveAlert() {
        assertThat(gamePage.getWarningAlertText()).isEqualTo("The move you made is illegal. Please make a legal move.");
    }

    @Step("Verify the \"The move has been made.\" alert is there")
    public void assertLegalMoveAlert() {
        assertThat(gamePage.getSuccessAlertText()).isEqualTo("The move has been made.");
    }

    @Step("Verify the move forms are not shown on the game page")
    public void assertGameMoveFormIsEmpty() {
        assertThat(gamePage.getMoveForm().isEmpty()).isEqualTo(true);
    }

    @Step("Verify the move forms are shown on the game page")
    public void assertGameMoveFormIsNotEmpty() {
        assertThat(gamePage.getMoveForm().isEmpty()).isEqualTo(false);
    }

    @Step("Verify the move notation is '{0}'")
    public void assertNotationEquals(String notation) {
        assertThat(gamePage.getChessBoardNavigation().getNotation()).isEqualTo(notation);
    }

    @Step("Verify the position on the chess board")
    public void assertChessBoardStateEquals(String board) {
        assertThat(gamePage.getChessBoard().getChessBoardState()).isEqualTo(board);
    }
}
