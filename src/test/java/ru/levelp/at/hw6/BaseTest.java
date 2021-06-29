package ru.levelp.at.hw6;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;
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
        for (int i = 0; i < 12; i++) {
            User currentUser = new User(
                FAKER.internet().password(4, 9),
                FAKER.internet().password()
            );
            driver.findElement(By.id("no_account")).click();
            driver.findElement(By.id("login")).sendKeys(currentUser.getLogin());
            driver.findElement(By.id("password")).sendKeys(currentUser.getPassword());
            driver.findElement(By.id("repeat_password")).sendKeys(currentUser.getPassword());
            driver.findElement(By.id("signup-button")).click();
            driver.findElement(By.linkText("Logout")).click();
            USERS.add(currentUser);
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
    }

}
