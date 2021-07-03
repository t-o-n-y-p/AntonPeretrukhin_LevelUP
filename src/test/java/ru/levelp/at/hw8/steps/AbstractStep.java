package ru.levelp.at.hw8.steps;

import org.openqa.selenium.WebDriver;
import ru.levelp.at.hw8.pages.ChallengesListPage;
import ru.levelp.at.hw8.pages.CreateChallengeStep1Page;
import ru.levelp.at.hw8.pages.CreateChallengeStep2Page;
import ru.levelp.at.hw8.pages.GamePage;
import ru.levelp.at.hw8.pages.LoginPage;
import ru.levelp.at.hw8.pages.MainPage;
import ru.levelp.at.hw8.pages.RootPage;
import ru.levelp.at.hw8.pages.SignupPage;

public abstract class AbstractStep {

    private final WebDriver driver;

    protected ChallengesListPage challengesListPage;
    protected CreateChallengeStep1Page createChallengeStep1Page;
    protected CreateChallengeStep2Page createChallengeStep2Page;
    protected GamePage gamePage;
    protected LoginPage loginPage;
    protected MainPage mainPage;
    protected RootPage rootPage;
    protected SignupPage signupPage;

    protected AbstractStep(WebDriver driver) {
        this.driver = driver;
        challengesListPage = new ChallengesListPage(driver);
        createChallengeStep1Page = new CreateChallengeStep1Page(driver);
        createChallengeStep2Page = new CreateChallengeStep2Page(driver);
        gamePage = new GamePage(driver);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        rootPage = new RootPage(driver);
        signupPage = new SignupPage(driver);
    }
}
