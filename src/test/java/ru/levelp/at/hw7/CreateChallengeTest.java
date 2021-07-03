package ru.levelp.at.hw7;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.testng.annotations.Test;
import ru.levelp.at.hw7.pages.CreateChallengeStep1Page;
import ru.levelp.at.hw7.pages.LoginPage;
import ru.levelp.at.hw7.pages.MainPage;
import ru.levelp.at.hw7.utils.User;

public class CreateChallengeTest extends BaseTest {

    @Test
    public void testCreateChallenge() {
        User currentUser = USERS.get(1);
        CreateChallengeStep1Page stepOnePage = new LoginPage(driver)
            .enterLogin(currentUser.getLogin())
            .enterPassword(currentUser.getPassword())
            .clickLogin()
            .clickCreateChallenge();

        Set<String> actualUsernames = stepOnePage.getOpponentUsernames();
        assertThat(actualUsernames).hasSize(8);

        stepOnePage = stepOnePage.openNextPage();
        actualUsernames.addAll(stepOnePage.getOpponentUsernames());
        User finalCurrentUser = currentUser;
        assertThat(actualUsernames).containsExactlyInAnyOrder(
            USERS.stream().filter(u -> !u.equals(finalCurrentUser)).map(User::getLogin).toArray(String[]::new)
        );
        assertThat(stepOnePage.getNextButtonDisabledState()).isEqualTo("true");

        stepOnePage = stepOnePage.fillSearchField(USERS.get(0).getLogin()).clickSearch();
        assertThat(stepOnePage.getOpponentUsernames()).containsOnly(USERS.get(0).getLogin());

        MainPage mainPage = stepOnePage.selectOpponent(0).clickCreateChallenge();
        assertThat(mainPage.getSuccessAlertText()).isEqualTo("Challenge created.");

        currentUser = USERS.get(0);
        mainPage = mainPage.clickLogout()
            .enterLogin(currentUser.getLogin())
            .enterPassword(currentUser.getPassword())
            .clickLogin();

        assertThat(mainPage.getChallengerNames()).containsOnly(USERS.get(1).getLogin());

        mainPage.clickLogout();
    }

}
