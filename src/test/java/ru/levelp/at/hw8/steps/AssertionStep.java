package ru.levelp.at.hw8.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import ru.levelp.at.hw8.utils.User;

public class AssertionStep extends AbstractStep {
    public AssertionStep(WebDriver driver) {
        super(driver);
    }

    public void assertThatLoginIsSuccessful(User user) {
        assertThat(mainPage.getNavigationBar().getTitle()).startsWith(user.getLogin() + " (");
    }

    public void assertNumberOfOpponentsOnPage(int expectedSize) {
        assertThat(createChallengeStep1Page.getResultListBlock().getItems()).hasSize(expectedSize);
    }

    public void assertNumberOfChallengesOnPage(int expectedSize) {
        assertThat(challengesListPage.getResultListBlock().getItems()).hasSize(expectedSize);
    }

    public void assertOpponentSetContainsExactly(Set<String> names, List<User> users) {
        assertThat(
            names.stream()
                .map(name -> name.split("\\s")[0])
                .collect(Collectors.toSet())
        ).containsExactly(
            users.stream().map(User::getLogin).toArray(String[]::new)
        );
    }

    public void assertOpponentSetContainsExactlyInAnyOrder(Set<String> names, List<User> users) {
        assertThat(
            names.stream()
                .map(name -> name.split("\\s")[0])
                .collect(Collectors.toSet())
        ).containsExactlyInAnyOrder(
            users.stream().map(User::getLogin).toArray(String[]::new)
        );
    }

    public void assertThatOpponentNextPageButtonIsDisabled() {
        assertThat(createChallengeStep1Page.getResultListBlock().getNextPageButtonStatus()).isEqualTo("true");
    }

    public void assertThatChallengeNextPageButtonIsDisabled() {
        assertThat(challengesListPage.getResultListBlock().getNextPageButtonStatus()).isEqualTo("true");
    }

    public void assertChallengesListContainsExactly(List<String> names, List<User> users) {
        assertThat(
            names.stream()
                .map(name -> name.split("\\s")[1])
                .collect(Collectors.toList())
        ).containsExactly(
            users.stream().map(User::getLogin).toArray(String[]::new)
        );
    }

    public void assertChallengeCreatedAlertOnMainPage() {
        assertThat(mainPage.getSuccessAlertText()).isEqualTo("Challenge created.");
    }

    public void assertChallengeAcceptedAlert() {
        assertThat(challengesListPage.getSuccessAlertText()).isEqualTo("Challenge accepted.");
    }

    public void assertChallengeAcceptedAlertOnMainPage() {
        assertThat(mainPage.getSuccessAlertText()).isEqualTo("Challenge accepted.");
    }

    public void assertGameListContainsExactly(List<String> names, List<User> users) {
        assertThat(
            names.stream()
                 .map(name -> name.split("\\s")[1])
                 .collect(Collectors.toList())
        ).containsExactly(
            users.stream().map(User::getLogin).toArray(String[]::new)
        );
    }

    public void assertOpponentToMoveAlert() {
        assertThat(gamePage.getInfoAlertText()).isEqualTo("Opponent to move.");
    }

    public void assertGameMoveFormIsEmpty() {
        assertThat(gamePage.getMoveForm().isEmpty()).isEqualTo(true);
    }

    public void assertGameMoveFormIsNotEmpty() {
        assertThat(gamePage.getMoveForm().isEmpty()).isEqualTo(false);
    }

    public void assertChessBoardStateEquals(String board) {
        assertThat(gamePage.getChessBoard().getChessBoardState()).isEqualTo(board);
    }
}
