package create.user.tests.admin.pages;

import base.tests.BaseNotDdtTest;
import model.User;
import model.UserActions;
import model.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import overlays.ChangePasswordOverlay;
import pages.DashboardPage;
import pages.LoginPage;
import pages.UserDetailPage;
import pages.UsersPage;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserPositiveTests extends BaseNotDdtTest {
    @DisplayName("General positive scenario for user creation")
    @Tag("Smoke")
    @Test
    public void generalPositiveScenario() {
        User testUser = UserFactory.getUserAllFlds();

        UsersPage up = page(UsersPage.class);
        DashboardPage dp = page(DashboardPage.class);

        createUser(testUser, false);
        assertTrue(up.isUserInTheTableWithActions(testUser, new ArrayList<UserActions>() {
            {
                add(UserActions.Delete);
                add(UserActions.Merge);
                add(UserActions.Ban);
            }
        }), String.format("User '%s' wasn't created", testUser.getUsername()));

        checkUserViaLogIn(rootUser, testUser);
        dp.Header.logOutUser(testUser);
    }

    @DisplayName("General positive scenario for user with mandatory fields only creation")
    @Tag("Regression")
    @Test
    public void generalPositiveScenarioMandatoryFldsOnly() {
        User testUser = UserFactory.getUserMandatoryFldsOnly();

        UsersPage up = page(UsersPage.class);
        DashboardPage dp = page(DashboardPage.class);

        createUser(testUser, false);
        assertTrue(up.isUserInTheTable(testUser), String.format("User '%s' wasn't created", testUser.getUsername()));

        checkUserViaLogIn(rootUser, testUser);
        dp.Header.logOutUser(testUser);
    }

    @DisplayName("General positive scenario for user (different case for username)")
    @Tag("Regression")
    @Test
    public void positiveScenarioCamelCase() {
        User testUser = UserFactory.getUserAllFlds();
        testUser.setUsername(String.format("\\u00a9User%s", LocalDateTime.now().toString()));

        UsersPage up = page(UsersPage.class);
        DashboardPage dp = page(DashboardPage.class);

        createUser(testUser, false);
        assertTrue(up.isUserInTheTable(testUser),
                String.format("User '%s' wasn't created", testUser.getUsername()));

        checkUserViaLogIn(rootUser, testUser);
        dp.Header.logOutUser(testUser);
    }

    @DisplayName("General positive scenario with force password change")
    @Tag("Smoke")
    @Test
    public void generalPositiveScenarioWithForcePass() {
        User testUser = UserFactory.getUserAllFlds();
        DashboardPage dp = page(DashboardPage.class);
        UsersPage up = page(UsersPage.class);
        createUser(testUser, true);
        assertTrue(up.isUserInTheTable(testUser),
                String.format("User '%s' wasn't created", testUser.getUsername()));

        checkUserViaLogIn(rootUser, testUser);

        testUser.setNewPassword("asd");
        testUser.setNewPasswordConfirmation("asd");

        ChangePasswordOverlay cpo = page(ChangePasswordOverlay.class);
        cpo.changeThePassword(testUser);

        checkUserViaLogIn(testUser, testUser);
        dp.Header.logOutUser(testUser);
    }

    @DisplayName("Check for direct link")
    @Tag("Smoke")
    @Test
    public void directLinkWorksTest() {
        UsersPage up = open(String.format("%s/Users", baseUrl), UsersPage.class);
        assertTrue(up.isPageOpened(), "Direct link doesn't work");

        up.Header.logOutUser();
    }

    private void createUser(User testUser, boolean forcePasswordChange) {
        UsersPage up = page(UsersPage.class);
        DashboardPage dp = page(DashboardPage.class);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        UserDetailPage udp = up.createUser(testUser, forcePasswordChange);
        assertTrue(udp.isUserCreated(testUser),
                String.format("User %s wasn't created", testUser.getUsername()));
        assertTrue(udp.allGroupsAssigned(testUser),
                String.format("User %s wasn't assigned all groups. Expected groups: %s",
                        testUser.getUsername(), testUser.getGroups()));
        assertTrue(up.CommonMenu.getUserNumber() > curNumOfUsers,
                "Number of users doesn't change");

        dp.Header.openUsersPage();
    }

    private void checkUserViaLogIn(User testUser, User testUser2) {
        LoginPage lp = page(LoginPage.class);
        DashboardPage dp = page(DashboardPage.class);

        open(getDashboardUrl(), DashboardPage.class);
        dp.Header.logOutUser(testUser);
        lp.loginAs(testUser2);
    }

    private String getDashboardUrl() {
        return String.format("%s/Dashboard", baseUrl);
    }
}
