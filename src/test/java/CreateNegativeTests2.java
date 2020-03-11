import basetests.BaseTestOneUser;
import model.User;
import model.UserFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import overlays.CreateUserOverlay;
import pages.DashboardPage;
import pages.UsersPage;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.refresh;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CreateNegativeTests2 extends BaseTestOneUser {
    private static User testUser;

    @BeforeAll
    public static void createUser() {
        testUser = UserFactory.getUserAllFlds();
    }

    @ParameterizedTest
    @MethodSource("stringIntAndListProvider")
    public void wrongUserTest(String username, String password, String passConfirmation,
                              String message) {
        UsersPage up = page(UsersPage.class);
        testUser.setUsername(username);
        testUser.setPassword(password);
        testUser.setPasswordConfirmation(passConfirmation);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        CreateUserOverlay ov = up.clickCreateUserBtn();
        ov.createNewUser(testUser, false);

        assertEquals(message, ov.getErrorTooltipText());
        ov.cancelOverlay();
        assertEquals(curNumOfUsers, up.CommonMenu.getUserNumber(), "Number of users increased!");
        refresh();
    }

    static Stream<Arguments> stringIntAndListProvider() {
        return Stream.of(
                arguments("", "", "", "Login is required!"),
                arguments("test", "", "", "Password is required!"),
                arguments("test", "test", "", "Password doesn't match!")
        );
    }

    @AfterAll
    public static void tearDown() {
        DashboardPage dp = page(DashboardPage.class);
        dp.Header.logOutUser();
    }
}
