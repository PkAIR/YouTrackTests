package create.user.tests.admin.pages;

import base.tests.BaseDdtTest;
import model.User;
import model.UserFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import overlays.ChangePasswordOverlay;
import pages.DashboardPage;
import pages.LoginPage;
import pages.UsersPage;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class NegativeChangePasswordTests extends BaseDdtTest {
    private static User testUser;

    @BeforeAll
    public static void createUser() {
        testUser = UserFactory.getUserAllFlds();
        LoginPage lp = page(LoginPage.class);
        UsersPage up = page(UsersPage.class);
        DashboardPage dp = page(DashboardPage.class);

        up.createUser(testUser, true);
        dp.Header.openUsersPage();
        assertTrue(up.isUserInTheTable(testUser), String.format("User '%s' wasn't created", testUser.getUsername()));

        open(baseUrl, DashboardPage.class);
        up.Header.logOutUser(rootUser);
        lp.loginAs(testUser);
    }

    @ParameterizedTest
    @Tag("Smoke")
    @MethodSource("changePasswordNegativeDataProvider")
    public void changePasswordNegativeTest(String oldPassword, String newPassword, String newPassConfirmation,
                                           String message) {
        ChangePasswordOverlay cpo = page(ChangePasswordOverlay.class);

        testUser.setPassword(oldPassword);
        testUser.setNewPassword(newPassword);
        testUser.setNewPasswordConfirmation(newPassConfirmation);

        cpo.changeThePassword(testUser);
        try {
            assertEquals(message, cpo.getErrorTooltipText());
        } finally {
            refresh();
        }
    }

    private static Stream<Arguments> changePasswordNegativeDataProvider() {
        return Stream.of(
                arguments("", "asd", "asd", "Required"),
                arguments("test", "", "asd", "Can't be empty"),
                arguments("test", "asd", "", "Doesn't match New password"),
                arguments("ololo", "asd", "asd", "Wrong old password"),
                arguments("test", "asd", "a", "Doesn't match New password"),
                arguments("test", "test", "test", "Used same password")
        );
    }
}
