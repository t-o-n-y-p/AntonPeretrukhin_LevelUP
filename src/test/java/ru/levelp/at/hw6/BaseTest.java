package ru.levelp.at.hw6;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.levelp.at.hw6.utils.PostgresqlConnectionUtil;
import ru.levelp.at.hw6.utils.User;

public abstract class BaseTest {

    private static final Faker FAKER = new Faker();

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final List<User> USERS = new ArrayList<>();

    @BeforeSuite
    public void beforeSuite() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        PostgresqlConnectionUtil.clearData();
        driver.navigate().to("http://localhost:8080");
        while (USERS.size() < 12) {
            User currentUser = new User(
                FAKER.internet().password(4, 9),
                FAKER.internet().password()
            );
            if (!USERS.contains(currentUser)) {
                driver.findElement(By.id("no_account")).click();
                driver.findElement(By.id("login")).sendKeys(currentUser.getLogin());
                driver.findElement(By.id("password")).sendKeys(currentUser.getPassword());
                driver.findElement(By.id("repeat_password")).sendKeys(currentUser.getPassword());
                driver.findElement(By.id("signup-button")).click();
                driver.findElement(By.linkText("Logout")).click();
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
        driver.navigate().to("http://localhost:8080");
        driver.findElements(By.linkText("Logout")).stream()
              .findFirst()
              .ifPresent(WebElement::click);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
        PostgresqlConnectionUtil.clearUserActivity();
    }

    protected void checkChessBoard(String expectedBoard) {
        List<WebElement> chessBoardSquares = driver.findElements(By.cssSelector("#chess-board td"));
        String chessBoard = IntStream.range(0, 72)
            .filter(i -> i % 9 != 0)
            .mapToObj(i -> chessBoardSquares.get(i).getText())
            .collect(Collectors.joining(";"));
        assertThat(chessBoard).isEqualTo(expectedBoard);
    }

    protected void login(User currentUser) {
        // hack to avoid test fails due to login button not clicked properly
        // wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Log in']"))) doesn't help
        driver.navigate().refresh();
        driver.findElement(By.id("login")).sendKeys(currentUser.getLogin());
        driver.findElement(By.id("password")).sendKeys(currentUser.getPassword());
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        assertThat(driver.findElement(By.cssSelector("nav > a")).getText())
            .startsWith(currentUser.getLogin() + " (");
    }

}
