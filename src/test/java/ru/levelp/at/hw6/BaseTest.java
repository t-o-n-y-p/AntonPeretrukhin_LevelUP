package ru.levelp.at.hw6;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.levelp.at.hw6.utils.PostgresqlConnectionUtil;
import ru.levelp.at.hw6.utils.User;

public abstract class BaseTest {

    private static final Faker FAKER = new Faker();

    protected WebDriver driver;
    protected final List<User> users = new ArrayList<>();

    @BeforeSuite
    public void beforeSuite() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

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
            users.add(currentUser);
        }
    }

    @BeforeMethod
    public void setUp() {
        driver.navigate().to("http://localhost:8080");
        driver.findElements(By.linkText("Logout")).stream()
              .findFirst()
              .ifPresent(WebElement::click);
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        driver.quit();
        PostgresqlConnectionUtil.clearData();
    }

}
