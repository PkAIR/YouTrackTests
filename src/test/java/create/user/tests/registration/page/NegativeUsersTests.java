package create.user.tests.registration.page;

import base.tests.BaseDdtTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.UserRegistrationPage;
import pages.UsersPage;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.refresh;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class NegativeUsersTests extends BaseDdtTest {
    @DisplayName("Negative scenario for user creation (user duplication)")
    @Tag("Smoke")
    @ParameterizedTest
    @MethodSource("testDataForUserDuplicateProvider")
    public void rootUserDuplicateCreation(String userType, String message) {
        UsersPage up = page(UsersPage.class);

        UserRegistrationPage urp = up.openUserRegistrationPage();
        urp.fillTheForm(rootUser, false);
        urp.confirmForm();

        try {
            assertEquals(message, urp.getErrorTooltipText(), "Error message mismatch");
        } finally {
            refresh();
        }
    }

    private static Stream<Arguments> testDataForUserDuplicateProvider() {
        return Stream.of(
                arguments("root", "Sorry, this login name is already taken")
        );
    }
}