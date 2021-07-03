package ru.levelp.at.hw8;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;
import ru.levelp.at.hw8.pages.CreateChallengeStep1Page;
import ru.levelp.at.hw8.pages.LoginPage;
import ru.levelp.at.hw8.pages.MainPage;
import ru.levelp.at.hw8.utils.User;

public class CreateChallengeTest extends BaseTest {

    @Test
    public void testCreateChallenge() {
        actionStep.login(USERS.get(1));
        assertionStep.assertThatLoginIsSuccessful(USERS.get(1));
        actionStep.clickCreateChallenge();

        Set<String> actualUsernames = actionStep.getOpponentNames();
        assertionStep.assertNumberOfOpponentsOnPage(8);
        actionStep.openNextOpponentPage();
        actualUsernames.addAll(actionStep.getOpponentNames());
        assertionStep.assertNumberOfOpponentsOnPage(3);
        assertionStep.assertThatOpponentNextPageButtonIsDisabled();
        List<User> expectedUsers = new ArrayList<>(USERS);
        expectedUsers.remove(1);
        assertionStep.assertOpponentSetContainsExactlyInAnyOrder(actualUsernames, expectedUsers);

        actionStep.searchOpponentByName(USERS.get(0).getLogin());
        assertionStep.assertOpponentSetContainsExactly(actionStep.getOpponentNames(), USERS.subList(0, 1));

        actionStep.selectOpponentAndCreateChallengeWithWhite(0);
        assertionStep.assertChallengeCreatedAlertOnMainPage();

        actionStep.logoutFromMainPage();
        actionStep.login(USERS.get(0));
        assertionStep.assertThatLoginIsSuccessful(USERS.get(0));
        assertionStep.assertChallengesSetContainsExactly(
            actionStep.getChallengeNamesFromMainPage(), USERS.subList(1, 2)
        );

        actionStep.logoutFromMainPage();
    }

}
