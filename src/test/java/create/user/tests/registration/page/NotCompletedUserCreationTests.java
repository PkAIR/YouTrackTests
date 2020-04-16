package create.user.tests.registration.page;

import base.tests.BaseDdtTest;
import model.User;
import model.UserFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import overlays.CreateUserOverlay;
import overlays.ErrorOverlay;
import pages.LoginPage;
import pages.UserRegistrationPage;
import pages.UsersPage;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class NotCompletedUserCreationTests extends BaseDdtTest {
    private static User testUser;

    @BeforeAll
    public static void createUser() {
        testUser = UserFactory.getUserAllFlds();
        UsersPage up = page(UsersPage.class);
        UserRegistrationPage urp = up.openUserRegistrationPage();
    }

    @DisplayName("Negative tests for user creation")
    @Tag("Smoke")
    @ParameterizedTest
    @MethodSource("wrongUserDataProvider")
    public void wrongUserTest(String username, String password, String passConfirmation,
                              String type, String message) {
        testUser.setUsername(username);
        testUser.setPassword(password);
        testUser.setPasswordConfirmation(passConfirmation);

        UserRegistrationPage urp = page(UserRegistrationPage.class);
        urp.fillTheForm(testUser, false);
        urp.confirmForm();

        try {
            // TODO Errors should be displayed the same way!
            if (type.equals("major")) {
                ErrorOverlay eo = page(ErrorOverlay.class);
                assertEquals(message, eo.getErrorSeverityText());
            } else {
                assertEquals(message, urp.getErrorTooltipText());
            }
        }
        finally {
            refresh();
        }
    }

    private static Stream<Arguments> wrongUserDataProvider() {
        return Stream.of(
                arguments("", " ", " ", "minor", "Login can't be empty"),
                arguments("<script>alert(\"test\")</script>", "test", "test", "major", "login shouldn't contain characters \"<\", \"/\", \">\": login"),
                arguments("test test", "test", "test", "major", "Restricted character ' ' in the name"),
                arguments(" testtest", "test", "test", "major", "Restricted character ' ' in the name"),
                arguments("testtest ", "test", "test", "major", "Restricted character ' ' in the name"),
                arguments(" test test ", "test", "test", "major", "Restricted character ' ' in the name"),
                arguments("test", "", "", "minor", "Password can't be empty"),
                arguments("test", "test", "", "minor", "Passwords do not match")
        );
    }
}
