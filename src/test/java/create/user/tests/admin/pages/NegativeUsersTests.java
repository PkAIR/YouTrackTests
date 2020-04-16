package create.user.tests.admin.pages;

import base.tests.BaseDdtTest;
import model.User;
import model.UserFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import overlays.CreateUserOverlay;
import overlays.ErrorOverlay;
import pages.UsersPage;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class NegativeUsersTests extends BaseDdtTest {
    @DisplayName("Negative scenario for user creation (user duplication)")
    @Tag("Smoke")
    @ParameterizedTest
    @MethodSource("testDataForUserDuplicateProvider")
    public void userDuplicateCreation(String userType, String message) {
        User userUnderTest = userType.equals("root") ? rootUser : UserFactory.getUserAllFlds();
        UsersPage up = page(UsersPage.class);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        up.createUser(userUnderTest, false);

        if (userType.equals("regular"))
        {
            up.Header.openUsersPage();
            assertTrue(up.isUserInTheTable(userUnderTest),
                    String.format("User '%s' wasn't created", userUnderTest.getUsername()));
            assertTrue(up.CommonMenu.getUserNumber() > curNumOfUsers,
                    "Initial test user wasn't created (number of users not increased)");
            up.createUser(userUnderTest, false);
            curNumOfUsers = up.CommonMenu.getUserNumber();
        }

        try {
            ErrorOverlay eo = page(ErrorOverlay.class);
            assertEquals(message, eo.getErrorSeverityText());
        } finally {
            CreateUserOverlay ov = page(CreateUserOverlay.class);
            ov.cancelOverlay();
            assertEquals(curNumOfUsers, up.CommonMenu.getUserNumber(),
                    "User with the same username was created (number of users increased)");
        }
    }

    private static Stream<Arguments> testDataForUserDuplicateProvider() {
        return Stream.of(
                arguments("root", "Removing null is prohibited"),
                arguments("regular", "Value should be unique: login")
        );
    }
}
