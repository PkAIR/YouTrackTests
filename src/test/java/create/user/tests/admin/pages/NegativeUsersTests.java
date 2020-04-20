package create.user.tests.admin.pages;

import base.tests.BaseDdtTest;
import model.User;
import model.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import overlays.ErrorOverlay;
import pages.UsersPage;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.refresh;
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
            assertEquals(up.CommonMenu.getUserNumber(), curNumOfUsers + 1,
                    "Initial test user wasn't created (number of users not increased)");
            curNumOfUsers = up.CommonMenu.getUserNumber();
            up.createUser(userUnderTest, false);
        }

        try {
            ErrorOverlay eo = page(ErrorOverlay.class);
            assertEquals(message, eo.getErrorSeverityText(), "Error message mismatch");
            refresh();
            assertEquals(curNumOfUsers, up.CommonMenu.getUserNumber(),
                    String.format("User with the same username was created (number of users increased). Username: '%s'",
                            userUnderTest.getUsername()));
        } finally {
            refresh();
        }
    }

    private static Stream<Arguments> testDataForUserDuplicateProvider() {
        return Stream.of(
                arguments("regular", "Value should be unique: login"),
                arguments("root", "Removing null is prohibited")
        );
    }
}
