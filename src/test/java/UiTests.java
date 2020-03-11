import basetests.BaseTest;
import basetests.BaseTestOneUser;
import model.User;
import model.UserFactory;
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

public class UiTests extends BaseTestOneUser {

    @ParameterizedTest
    @MethodSource("stringIntAndListProvider")
    public void overlayClosingTest(String method, String message) {
        User testUser = UserFactory.getUserAllFlds();
        DashboardPage dp = page(DashboardPage.class);
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

    static Stream<Arguments> stringIntAndListProvider() {
        return Stream.of(
                arguments("cancel", "Number of users changed"),
                arguments("close", "Number of users changed")
        );
    }

    @Test
    public void movingCreateUserOverlay() {
        DashboardPage dp = page(DashboardPage.class);
        UsersPage up = page(UsersPage.class);

        CreateUserOverlay ov = up.clickCreateUserBtn();
        assertTrue(ov.wasOverlayMoved(), "Create user overlay can't be moved");
        ov.cancelOverlay();
    }
}
