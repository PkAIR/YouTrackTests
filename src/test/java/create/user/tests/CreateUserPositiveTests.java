package create.user.tests;

import base.tests.BaseNotDdtTest;
import model.User;
import model.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import overlays.ChangePasswordOverlay;
import pages.DashboardPage;
import pages.LoginPage;
import pages.UserDetailPage;
import pages.UsersPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserPositiveTests extends BaseNotDdtTest {
    @DisplayName("General positive scenario for user creation")
    @Test
    public void generalPositiveScenario() {
        User testUser = UserFactory.getUserAllFlds();

        LoginPage lp = page(LoginPage.class);
        DashboardPage dp = page(DashboardPage.class);
        UsersPage up = page(UsersPage.class);
        createUser(testUser, false);
        assertTrue(up.isUserInTheTable(testUser));

        open(getDashboardUrl(), DashboardPage.class);
        up.Header.logOutUser(rootUser);
        lp.loginAs(testUser);
        dp.Header.logOutUser(testUser);
    }

    @DisplayName("General positive scenario with force password change")
    @Test
    public void generalPositiveScenarioWithForcePass() {
        User testUser = UserFactory.getUserAllFlds();
        LoginPage lp = page(LoginPage.class);
        DashboardPage dp = page(DashboardPage.class);
        UsersPage up = page(UsersPage.class);
        createUser(testUser, true);
        assertTrue(up.isUserInTheTable(testUser));

        open(getDashboardUrl(), DashboardPage.class);
        up.Header.logOutUser(rootUser);
        lp.loginAs(testUser);

        testUser.setNewPassword("asd");
        testUser.setNewPasswordConfirmation("asd");

        ChangePasswordOverlay cpo = page(ChangePasswordOverlay.class);
        cpo.changeThePassword(testUser);

        open(getDashboardUrl(), DashboardPage.class);
        dp.Header.logOutUser(testUser);
    }

    private void createUser(User testUser, boolean forcePasswordChange) {
        UsersPage up = page(UsersPage.class);
        DashboardPage dp = page(DashboardPage.class);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        UserDetailPage udp = up.createUser(testUser, forcePasswordChange);
        assertTrue(udp.isUserCreated(testUser),
                String.format("User %s wasn't created", testUser.getUsername()));
        assertTrue(up.CommonMenu.getUserNumber() > curNumOfUsers,
                "Number of users doesn't change");

        dp.Header.openUsersPage();
    }

    private String getDashboardUrl() {
        return String.format("%s/Dashboard", baseUrl);
    }
}
