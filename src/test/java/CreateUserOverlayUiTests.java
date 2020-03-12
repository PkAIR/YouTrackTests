import basetests.BaseDdtTest;
import model.User;
import model.UserFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import overlays.CreateUserOverlay;
import pages.DashboardPage;
import pages.UsersPage;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CreateUserOverlayUiTests extends BaseDdtTest {
    @DisplayName("Test for Create user overlay being closed")
    @ParameterizedTest
    @MethodSource("testDataForOverlayClosingProvider")
    public void overlayClosingTest(String method, String message) {
        User testUser = UserFactory.getUserAllFlds();
        UsersPage up = page(UsersPage.class);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        CreateUserOverlay ov = up.clickCreateUserBtn();

        ov.fillTheForm(testUser, false);
        switch (method) {
            case "cancel":{
                ov.cancelOverlay();
                break;
            }
            case "close" : {
                ov.closeOverlay();
            }
        }

        assertTrue(up.pageIsVisible());
        assertFalse(up.isUserInTheTable(testUser));
        assertEquals(up.CommonMenu.getUserNumber(), curNumOfUsers, message);
    }

    private static Stream<Arguments> testDataForOverlayClosingProvider() {
        return Stream.of(
                arguments("cancel", "Number of users changed"),
                arguments("close", "Number of users changed")
        );
    }

    @DisplayName("Test for Create user overlay is movable")
    @Test
    public void movingCreateUserOverlay() {
        UsersPage up = page(UsersPage.class);

        CreateUserOverlay ov = up.clickCreateUserBtn();
        assertTrue(ov.wasOverlayMoved(), "Create user overlay can't be moved");
        ov.cancelOverlay();
    }

    @AfterAll
    public static void tearDown() {
        DashboardPage dp = page(DashboardPage.class);
        dp.Header.logOutUser();
    }
}
