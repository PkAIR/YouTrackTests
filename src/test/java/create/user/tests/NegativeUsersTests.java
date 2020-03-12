package create.user.tests;

import base.tests.BaseNotDdtTest;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import overlays.CreateUserOverlay;
import overlays.ErrorOverlay;
import pages.DashboardPage;
import pages.LoginPage;
import pages.UserDetailPage;
import pages.UsersPage;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.*;

public class NegativeUsersTests extends BaseNotDdtTest {
    @DisplayName("Negative scenario for user creation")
    @Test
    public void generalPositiveScenario() {
        UsersPage up = page(UsersPage.class);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        up.createUser(rootUser, true);
        CreateUserOverlay ov = page(CreateUserOverlay.class);
        ov.cancelOverlay();

        assertEquals(curNumOfUsers, up.CommonMenu.getUserNumber(),
                "User with the same username was created (number of users increased)");

        ErrorOverlay eo = page(ErrorOverlay.class);
        assertEquals("Removing null is prohibited", eo.getErrorSeverityText());
    }
}
