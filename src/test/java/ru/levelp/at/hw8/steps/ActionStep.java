package ru.levelp.at.hw8.steps;

import io.qameta.allure.Step;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.levelp.at.hw8.pages.components.ChessGameMoveFormComponent;
import ru.levelp.at.hw8.pages.components.ListComponent;
import ru.levelp.at.hw8.utils.User;

public class ActionStep extends AbstractStep {

    public ActionStep(WebDriver driver) {
        super(driver);
    }

    @Step("Log in to user: '{0}'")
    public void login(User user) {
        loginPage
            .open()
            .enterLogin(user.getLogin())
            .enterPassword(user.getPassword())
            .clickLogin();
    }

    @Step("Sign up user: '{0}' and logout straight after")
    public void signupAndLogout(User user) {
        loginPage
            .open()
            .clickSignup()
            .enterLogin(user.getLogin())
            .enterPassword(user.getPassword())
            .enterRepeatPassword(user.getPassword())
            .clickSignup();

        logoutFromMainPage();
    }

    @Step("Make move: '{0}'-'{1}'")
    public void makeMove(String from, String to) {
        ChessGameMoveFormComponent component = gamePage.getMoveForm();
        component.selectFromSquare(from);
        component.selectToSquare(to);
        component.clickMakeMove();
    }

    @Step("Logout from main page")
    public void logoutFromMainPage() {
        mainPage.getNavigationBar().clickLinkByName("Logout");
    }

    @Step("Logout from game page")
    public void logoutFromGamePage() {
        gamePage.getNavigationBar().clickLinkByName("Main page");
        mainPage.getNavigationBar().clickLinkByName("Logout");
    }

    @Step("Open the root page and logout if user is logged in")
    public void openIndexAndLogoutIfNecessary() {
        rootPage.openAndLogout();
    }

    @Step("Click \"Create challenge\" button")
    public void clickCreateChallenge() {
        mainPage.getNavigationBar().clickLinkByName("Create challenge");
    }

    @Step("Get names of users in challenges")
    public List<String> getChallengeNames() {
        return challengesListPage.getResultListBlock().getItems().stream()
            .map(ListComponent::getTitle)
            .collect(Collectors.toList());
    }

    @Step("Get opponent names")
    public Set<String> getOpponentNames() {
        return createChallengeStep1Page.getResultListBlock().getItems().stream()
            .map(ListComponent::getTitle)
            .collect(Collectors.toSet());
    }

    @Step("Click \"Next page\"")
    public void openNextOpponentPage() {
        createChallengeStep1Page.getResultListBlock().clickNextPage();
    }

    @Step("Click \"Next page\"")
    public void openNextChallengePage() {
        challengesListPage.getResultListBlock().clickNextPage();
    }

    @Step("Search results by string: '{0}'")
    public void searchOpponentByName(String name) {
        createChallengeStep1Page.getNavigationBar().fillSearchField(name);
        createChallengeStep1Page.getNavigationBar().clickSearch();
    }

    @Step("Search results by string: '{0}'")
    public void searchChallengeByName(String name) {
        challengesListPage.getNavigationBar().fillSearchField(name);
        challengesListPage.getNavigationBar().clickSearch();
    }

    @Step("Select opponent with index '{0}' and challenge him")
    public void selectOpponentAndCreateChallengeWithWhite(int opponentIndex) {
        createChallengeStep1Page.getResultListBlock().getItems().get(opponentIndex).clickComponentButton();
        createChallengeStep2Page.clickCreateChallenge();
    }

    @Step("Get names of users in challenges")
    public List<String> getChallengeNamesFromMainPage() {
        return mainPage.getChallengeBlock().getItems().stream()
            .map(ListComponent::getTitle)
            .collect(Collectors.toList());
    }

    @Step("Get names of users in games")
    public List<String> getGameNamesFromMainPage() {
        return mainPage.getGameBlock().getItems().stream()
            .map(ListComponent::getTitle)
            .collect(Collectors.toList());
    }

    @Step("Click \"View all incoming challenges\"")
    public void viewAllIncomingChallenges() {
        mainPage.getChallengeBlock().clickViewAll();
    }

    @Step("Accept challenge with index '{0}'")
    public void acceptChallenge(int index) {
        challengesListPage.getResultListBlock().getItems().get(index).clickComponentButton();
    }

    @Step("Accept challenge with index '{0}'")
    public void acceptChallengeFromMainPage(int index) {
        mainPage.getChallengeBlock().getItems().get(index).clickComponentButton();
    }

    @Step("Open game with index '{0}'")
    public void openGameFromMainPage(int index) {
        mainPage.getGameBlock().getItems().get(index).clickComponentButton();
    }

    @Step("Return to main page")
    public void clickMainPageFromChallengeList() {
        challengesListPage.getNavigationBar().clickMainPage();
    }

    @Step("Return to main page")
    public void clickMainPageFromGame() {
        gamePage.getNavigationBar().clickLinkByName("Main page");
    }

    @Step("Open previous move")
    public void clickPreviousMoveOnLastMove() {
        gamePage.getChessBoardNavigation().clickPreviousMoveOnLastMove();
    }

    @Step("Open previous move")
    public void clickPreviousMoveOnSecondMove() {
        gamePage.getChessBoardNavigation().clickPreviousMoveOnSecondMove();
    }

    @Step("Open previous move")
    public void clickPreviousMove() {
        gamePage.getChessBoardNavigation().clickPreviousMove();
    }

    @Step("Open next move")
    public void clickNextMoveOnFirstMove() {
        gamePage.getChessBoardNavigation().clickNextMoveOnFirstMove();
    }

    @Step("Open next move")
    public void clickNextMoveOnSecondToLastMove() {
        gamePage.getChessBoardNavigation().clickNextMoveOnSecondToLastMove();
    }

    @Step("Open next move")
    public void clickNextMove() {
        gamePage.getChessBoardNavigation().clickNextMove();
    }

    @Step("Resign the game")
    public void resignTheGame() {
        gamePage.getMoveForm().clickResign();
        gamePage.getMoveForm().getConfirmResignationAlert().confirmResignation();
    }
}
