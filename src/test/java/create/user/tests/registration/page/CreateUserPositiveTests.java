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

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserPositiveTests extends BaseNotDdtTest {
    private static int curNumOfUsers;

    @DisplayName("General positive scenario for user registration page 'Remember Me' off")
    @Tag("Smoke")
    @Test
    public void generalPositiveScenarioRememberMeOff() {
        User testUser = UserFactory.getSelfRegistrationUser();

        UsersPage up = page(UsersPage.class);
        DashboardPage dp = page(DashboardPage.class);

        createUser(testUser, false);
        checkUserViaLogIn(rootUser, testUser);
        dp.Header.logOutUser(rootUser);
    }

    @DisplayName("General positive scenario for user registration page 'Remember Me' on")
    @Tag("Smoke")
    @Test
    public void generalPositiveScenarioRememberMeOn() {
        User testUser = UserFactory.getSelfRegistrationUser();

        UsersPage up = page(UsersPage.class);
        DashboardPage dp = page(DashboardPage.class);

        createUser(testUser, true);
        assertNotNull(WebDriverRunner.getWebDriver().manage().getCookieNamed("jetbrains.charisma.main.security.PRINCIPAL"),
                "Cookie was found for 'Remember Me' flag");

        checkUserViaLogIn(rootUser, testUser);
        dp.Header.logOutUser(rootUser);
    }

    @DisplayName("Check for direct link")
    @Tag("Smoke")
    @Test
    public void directLinkWorksTest() {
        UserRegistrationPage urp = open(String.format("%s/registerUserForm", baseUrl), UserRegistrationPage.class);
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

    private void checkUserViaLogIn(User testUser, User testUser2) {
        LoginPage lp = page(LoginPage.class);
        DashboardPage dp = page(DashboardPage.class);

        open(getDashboardUrl(), DashboardPage.class);
        dp.Header.logOutUser(testUser2);
        lp.loginAs(testUser);

        UsersPage up = dp.Header.openUsersPage();
        assertTrue(up.CommonMenu.getUserNumber() > curNumOfUsers,
                "Number of users doesn't change");
        assertTrue(up.isUserInTheTableWithActions(testUser2, new ArrayList<UserActions>() {
            {
                add(UserActions.Delete);
                add(UserActions.Merge);
                add(UserActions.Ban);
            }
        }), String.format("User '%s' wasn't created", testUser2.getUsername()));

        UserDetailPage udp = up.openUserDetailPage(testUser2);
        assertTrue(udp.isUserCreated(testUser2),
                String.format("User %s wasn't created", testUser.getUsername()));
        assertTrue(udp.allGroupsAssigned(testUser2),
                String.format("User %s wasn't assigned all groups. Expected groups: %s",
                        testUser2.getUsername(), testUser2.getGroups()));

        dp.Header.openUsersPage();
    }

    private String getDashboardUrl() {
        return String.format("%s/Dashboard", baseUrl);
    }
}

