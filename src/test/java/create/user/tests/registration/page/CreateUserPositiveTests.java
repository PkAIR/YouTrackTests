package create.user.tests.registration.page;

import base.tests.BaseNotDdtTest;
import com.codeborne.selenide.WebDriverRunner;
import model.User;
import model.UserActions;
import model.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.*;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.*;

public class CreateUserPositiveTests extends BaseNotDdtTest {
    private static int curNumOfUsers;
    private static final String COOKIE_NAME = "jetbrains.charisma.main.security.PRINCIPAL";

    @DisplayName("General positive scenario for user registration page 'Remember Me' off")
    @Tag("Smoke")
    @Test
    public void generalPositiveScenarioRememberMeOff() {
        User testUser = UserFactory.getSelfRegistrationUser();
        DashboardPage dp = page(DashboardPage.class);

        createUser(testUser, false);
        assertNull(WebDriverRunner.getWebDriver().manage().getCookieNamed(COOKIE_NAME),
                "Cookie was found for 'Remember Me' flag");

        checkUserViaLogIn(testUser);
        dp.Header.logOutUser(rootUser);
    }

    @DisplayName("General positive scenario for user registration page 'Remember Me' on")
    @Tag("Regression")
    @Test
    public void generalPositiveScenarioRememberMeOn() {
        User testUser = UserFactory.getSelfRegistrationUser();
        DashboardPage dp = page(DashboardPage.class);

        createUser(testUser, true);
        assertNotNull(WebDriverRunner.getWebDriver().manage().getCookieNamed(COOKIE_NAME),
                "Cookie was found for 'Remember Me' flag");

        checkUserViaLogIn(testUser);
        dp.Header.logOutUser(rootUser);
    }

    @DisplayName("Check for direct link")
    @Tag("Smoke")
    @Test
    public void directLinkWorksTest() {
        UserRegistrationPage urp = UserRegistrationPage.openUserRegistrationPageLink();
        assertTrue(urp.isPageOpened(), "Direct link doesn't work");
        openLoginPage();
    }

    private void createUser(User testUser, boolean rememberMeFlag) {
        UsersPage up = page(UsersPage.class);

        curNumOfUsers = up.CommonMenu.getUserNumber();
        UserRegistrationPage urp = up.openUserRegistrationPage();
        DashboardPage dp  = urp.createNewUser(testUser, rememberMeFlag);

        ProfilePage pp = dp.Header.openProfilePage();
        assertTrue(pp.allGroupsAssigned(testUser));
    }

    private void checkUserViaLogIn(User testUser) {
        LoginPage lp = page(LoginPage.class);
        DashboardPage dp = DashboardPage.openDashboardPageLink();

        dp.Header.logOutUser(testUser);
        lp.loginAs(rootUser);

        UsersPage up = dp.Header.openUsersPage();
        assertTrue(up.CommonMenu.getUserNumber() > curNumOfUsers,
                "Number of users doesn't change");
        assertTrue(up.isUserInTheTableWithActions(testUser, new ArrayList<UserActions>() {
            {
                add(UserActions.Delete);
                add(UserActions.Merge);
                add(UserActions.Ban);
            }
        }), String.format("User '%s' wasn't created", testUser.getUsername()));

        UserDetailPage udp = up.openUserDetailPage(testUser);
        assertTrue(udp.isUserCreated(testUser),
                String.format("User %s wasn't created", rootUser.getUsername()));
        assertTrue(udp.allGroupsAssigned(testUser),
                String.format("User %s wasn't assigned all groups. Expected groups: %s",
                        testUser.getUsername(), testUser.getGroups()));

        dp.Header.openProfilePage();
    }
}

