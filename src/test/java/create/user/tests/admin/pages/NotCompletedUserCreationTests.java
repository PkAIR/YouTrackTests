package create.user.tests.admin.pages;

import base.tests.BaseDdtTest;
import model.User;
import model.UserFactory;
import org.junit.jupiter.api.BeforeAll;
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
import static com.codeborne.selenide.Selenide.refresh;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class NotCompletedUserCreationTests extends BaseDdtTest {
    private static User testUser;

    @BeforeAll
    public static void createUser() {
        testUser = UserFactory.getUserAllFlds();
    }

    @DisplayName("Negative tests for user creation")
    @Tag("Smoke")
    @ParameterizedTest
    @MethodSource("wrongUserDataProvider")
    public void wrongUserTest(String username, String password, String passConfirmation,
                              String type, String message) {
        UsersPage up = page(UsersPage.class);
        testUser.setUsername(username);
        testUser.setPassword(password);
        testUser.setPasswordConfirmation(passConfirmation);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        CreateUserOverlay ov = up.clickCreateUserBtn();
        ov.createNewUser(testUser, false);

        try {
            // TODO Errors should be displayed the same way!
            if (type.equals("major")) {
                ErrorOverlay eo = page(ErrorOverlay.class);
                assertEquals(message, eo.getErrorSeverityText());
            } else {
                assertEquals(message, ov.getErrorTooltipText());
            }
        }
        finally {
            ov.cancelOverlay();
            assertEquals(curNumOfUsers, up.CommonMenu.getUserNumber(), "Number of users increased!");
            refresh();
        }
    }

    private static Stream<Arguments> wrongUserDataProvider() {
        return Stream.of(
                arguments("", "", "", "minor", "Login is required!"),
                arguments("<script>alert(\"test\")</script>", "test", "test", "major", "login shouldn't contain characters \"<\", \"/\", \">\": login"),
                arguments("test test", "test", "test", "major", "Restricted character ' ' in the name"),
                arguments(" testtest", "test", "test", "major", "Restricted character ' ' in the name"),
                arguments("testtest ", "test", "test", "major", "Restricted character ' ' in the name"),
                arguments(" test test ", "test", "test", "major", "Restricted character ' ' in the name"),
                arguments("test", "", "", "minor", "Password is required!"),
                arguments("test", "test", "", "minor", "Password doesn't match!")
        );
    }
}
