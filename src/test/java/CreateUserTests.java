import basetests.BaseNotDdtTest;
import model.User;
import model.UserFactory;
import org.junit.jupiter.api.Test;
import overlays.ChangePasswordOverlay;
import overlays.CreateUserOverlay;
import pages.DashboardPage;
import pages.LoginPage;
import pages.UserDetailPage;
import pages.UsersPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserTests extends BaseNotDdtTest {
    @Test
    public void generalPositiveScenario() {
        User testUser = UserFactory.getUserAllFlds();

        LoginPage lp = page(LoginPage.class);
        DashboardPage dp = page(DashboardPage.class);
        UsersPage up = page(UsersPage.class);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        CreateUserOverlay ov = up.clickCreateUserBtn();

        UserDetailPage udp = ov.createNewUser(testUser, false);

        assertTrue(udp.isUserCreated(testUser),
                String.format("User %s wasn't created", testUser.getUsername()));

        dp.Header.openUsersPage();
        assertTrue(up.CommonMenu.getUserNumber() > curNumOfUsers,
                "Number of users doesn't change");

        assertTrue(up.isUserInTheTable(testUser));
        refresh();

        open(baseUrl, DashboardPage.class);
        up.Header.logOutUser(rootUser);
        lp.loginAs(testUser);
        refresh();
        open(baseUrl, DashboardPage.class);
        dp.Header.logOutUser(testUser);
    }

    @Test
    public void generalPositiveScenarioWithForcePass() {
        User testUser = UserFactory.getUserAllFlds();

        LoginPage lp = page(LoginPage.class);
        DashboardPage dp = page(DashboardPage.class);
        UsersPage up = page(UsersPage.class);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        CreateUserOverlay ov = up.clickCreateUserBtn();

        UserDetailPage udp = ov.createNewUser(testUser, true);

        assertTrue(udp.isUserCreated(testUser),
                String.format("User %s wasn't created", testUser.getUsername()));

        dp.Header.openUsersPage();
        assertTrue(up.CommonMenu.getUserNumber() > curNumOfUsers,
                "Number of users doesn't change");

        assertTrue(up.isUserInTheTable(testUser));

        open(baseUrl, DashboardPage.class);
        up.Header.logOutUser(rootUser);
        lp.loginAs(testUser);

        testUser.setNewPassword("asd");
        testUser.setNewPasswordConfirmation("asd");

        ChangePasswordOverlay cpo = page(ChangePasswordOverlay.class);
        cpo.changeThePassword(testUser);

        open(baseUrl, DashboardPage.class);
        dp.Header.logOutUser(testUser);
    }
}
