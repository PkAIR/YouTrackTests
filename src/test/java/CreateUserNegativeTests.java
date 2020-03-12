import basetests.BaseDdtTest;
import model.User;
import model.UserFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

public class CreateUserNegativeTests extends BaseDdtTest {
    private static User testUser;

    @BeforeAll
    public static void createUser() {
        testUser = UserFactory.getUserAllFlds();
        LoginPage lp = page(LoginPage.class);
        DashboardPage dp = page(DashboardPage.class);
        UsersPage up = page(UsersPage.class);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        up.createUser(testUser, true);
        assertTrue(up.CommonMenu.getUserNumber() > curNumOfUsers,
                "Number of users doesn't change");
        assertTrue(up.isUserInTheTable(testUser));

        open(baseUrl, DashboardPage.class);
        up.Header.logOutUser(rootUser);
        lp.loginAs(testUser);
    }

    @ParameterizedTest
    @MethodSource("changePasswordNegativeDataProvider")
    public void changePasswordNegativeTest(String oldPassword, String newPassword, String newPassConfirmation,
                                           String message) {
        ChangePasswordOverlay cpo = page(ChangePasswordOverlay.class);

        testUser.setPassword(oldPassword);
        testUser.setNewPassword(newPassword);
        testUser.setNewPasswordConfirmation(newPassConfirmation);

        cpo.changeThePassword(testUser);
        assertEquals(message, cpo.getErrorTooltipText());
        refresh();
    }

    @AfterAll
    public static void tearDown() {
        open(baseUrl, DashboardPage.class);
        DashboardPage dp = page(DashboardPage.class);
        dp.Header.logOutUser(testUser);
    }

    private static Stream<Arguments> changePasswordNegativeDataProvider() {
        return Stream.of(
                arguments("ololo", "asd", "asd", "Wrong old password"),
                arguments("test", "", "", "Can't be empty"),
                arguments("test", "asd", "a", "Doesn't match New password")
        );
    }
}
