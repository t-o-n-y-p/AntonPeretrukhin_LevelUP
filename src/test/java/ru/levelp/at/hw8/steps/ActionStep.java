package ru.levelp.at.hw8.steps;

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

    public void login(User user) {
        loginPage
            .open()
            .enterLogin(user.getLogin())
            .enterPassword(user.getPassword())
            .clickLogin();
    }

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

    public void makeMove(User user, String from, String to) {
        login(user);
        mainPage.getGameBlock().getItems().get(0).clickComponentButton();
        ChessGameMoveFormComponent component = gamePage.getMoveForm();
        component.selectFromSquare(from);
        component.selectToSquare(to);
        component.clickMakeMove();
    }

    public void logoutFromMainPage() {
        mainPage.getNavigationBar().clickLinkByName("Logout");
    }

    public void logoutFromGamePage() {
        gamePage.getNavigationBar().clickLinkByName("Main page");
        mainPage.getNavigationBar().clickLinkByName("Logout");
    }

    public void openIndexAndLogoutIfNecessary() {
        rootPage.openAndLogout();
    }

    public void clickCreateChallenge() {
        mainPage.getNavigationBar().clickLinkByName("Create challenge");
    }

    public List<String> getChallengeNames() {
        return challengesListPage.getResultListBlock().getItems().stream()
            .map(ListComponent::getTitle)
            .collect(Collectors.toList());
    }

    public Set<String> getOpponentNames() {
        return createChallengeStep1Page.getResultListBlock().getItems().stream()
            .map(ListComponent::getTitle)
            .collect(Collectors.toSet());
    }

    public void openNextOpponentPage() {
        createChallengeStep1Page.getResultListBlock().clickNextPage();
    }

    public void openNextChallengePage() {
        challengesListPage.getResultListBlock().clickNextPage();
    }

    public void searchOpponentByName(String name) {
        createChallengeStep1Page.getNavigationBar().fillSearchField(name);
        createChallengeStep1Page.getNavigationBar().clickSearch();
    }

    public void searchChallengeByName(String name) {
        challengesListPage.getNavigationBar().fillSearchField(name);
        challengesListPage.getNavigationBar().clickSearch();
    }

    public void selectOpponentAndCreateChallengeWithWhite(int opponentIndex) {
        createChallengeStep1Page.getResultListBlock().getItems().get(opponentIndex).clickComponentButton();
        createChallengeStep2Page.clickCreateChallenge();
    }

    public List<String> getChallengeNamesFromMainPage() {
        return mainPage.getChallengeBlock().getItems().stream()
            .map(ListComponent::getTitle)
            .collect(Collectors.toList());
    }

    public List<String> getGameNamesFromMainPage() {
        return mainPage.getGameBlock().getItems().stream()
            .map(ListComponent::getTitle)
            .collect(Collectors.toList());
    }

    public void viewAllIncomingChallenges() {
        mainPage.getChallengeBlock().clickViewAll();
    }

    public void acceptChallenge(int index) {
        challengesListPage.getResultListBlock().getItems().get(index).clickComponentButton();
    }

    public void acceptChallengeFromMainPage(int index) {
        mainPage.getChallengeBlock().getItems().get(index).clickComponentButton();
    }

    public void openGameFromMainPage(int index) {
        mainPage.getGameBlock().getItems().get(index).clickComponentButton();
    }

    public void clickMainPageFromChallengeList() {
        challengesListPage.getNavigationBar().clickMainPage();
    }

    public void clickMainPageFromGame() {
        gamePage.getNavigationBar().clickLinkByName("Main page");
    }
}
