package ru.levelp.at.hw8;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.IntStream;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.levelp.at.hw8.pages.ChallengesListPage;
import ru.levelp.at.hw8.pages.GamePage;
import ru.levelp.at.hw8.pages.LoginPage;
import ru.levelp.at.hw8.pages.MainPage;
import ru.levelp.at.hw8.utils.PostgresqlConnectionUtil;
import ru.levelp.at.hw8.utils.User;

public class AcceptChallengeTest extends BaseTest {

    @BeforeTest
    public void beforeTest() {
        PostgresqlConnectionUtil.createTestChallenges();
    }

    @Test
    public void testAcceptChallenge() {
    }

}
