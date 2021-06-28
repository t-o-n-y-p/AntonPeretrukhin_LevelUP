package ru.levelp.at.hw6;

import org.testng.annotations.BeforeTest;
import ru.levelp.at.hw6.utils.PostgresqlConnectionUtil;

public class AcceptChallengeTest extends BaseTest {

    @BeforeTest
    public void beforeTest() {
        PostgresqlConnectionUtil.createTestChallenges();
    }

}
