package ru.levelp.at.hw8;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.levelp.at.hw8.pages.LoginPage;
import ru.levelp.at.hw8.pages.RootPage;
import ru.levelp.at.hw8.steps.ActionStep;
import ru.levelp.at.hw8.steps.AssertionStep;
import ru.levelp.at.hw8.utils.PostgresqlConnectionUtil;
import ru.levelp.at.hw8.utils.User;

public abstract class BaseTest {

    private static final Faker FAKER = new Faker();

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ActionStep actionStep;
    protected AssertionStep assertionStep;
    protected static final List<User> USERS = new ArrayList<>();

    @BeforeSuite
    public void beforeSuite() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        actionStep = new ActionStep(driver);

        PostgresqlConnectionUtil.clearData();
        actionStep.logout();
        while (USERS.size() < 12) {
            User currentUser = new User(
                FAKER.internet().password(4, 9),
                FAKER.internet().password()
            );
            if (!USERS.contains(currentUser)) {
                actionStep.signupAndLogout(currentUser);
                USERS.add(currentUser);
            }
        }
        driver.quit();
    }

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();
        actionStep = new ActionStep(driver);
        assertionStep = new AssertionStep(driver);
        actionStep.logout();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
        PostgresqlConnectionUtil.clearUserActivity();
    }

}
