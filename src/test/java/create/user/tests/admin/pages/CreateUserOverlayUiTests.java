package create.user.tests.admin.pages;

import base.tests.BaseDdtTest;
import model.User;
import model.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import overlays.BaseOverlay;
import overlays.CreateUserOverlay;
import pages.UsersPage;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.refresh;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CreateUserOverlayUiTests extends BaseDdtTest {
    @DisplayName("Test for 'Create User' overlay being closed and cancelled")
    @Tag("Regression")
    @ParameterizedTest
    @MethodSource("testDataForOverlayClosingProvider")
    public void overlayClosingTest(BaseOverlay.OverlayActions action, String message) {
        User testUser = UserFactory.getUserAllFlds();
        UsersPage up = page(UsersPage.class);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        CreateUserOverlay ov = up.clickCreateUserBtn();

        ov.fillTheForm(testUser, false);
        switch (action) {
            case cancel:{
                ov.cancelOverlay();
                break;
            }
            case close: {
                ov.closeOverlay();
                break;
            }
        }

        try {
            assertTrue(up.isPageOpened(), "Users page is not opened");
            assertFalse(up.isUserInTheTable(testUser), "User not found in the table");
            assertEquals(up.CommonMenu.getUserNumber(), curNumOfUsers, message);
        } finally {
            refresh();
            assertEquals(curNumOfUsers, up.CommonMenu.getUserNumber(), "Number of users increased!");
        }
    }

    @DisplayName("Test for 'Create User' overlay is movable")
    @Tag("Regression")
    @Test
    public void movingCreateUserOverlay() {
        UsersPage up = page(UsersPage.class);

        CreateUserOverlay ov = up.clickCreateUserBtn();
        assertTrue(ov.wasOverlayMoved(), "Create user overlay can't be moved");
        ov.cancelOverlay();
    }

    private static Stream<Arguments> testDataForOverlayClosingProvider() {
        return Stream.of(
                arguments(BaseOverlay.OverlayActions.cancel, "Number of users changed for 'cancel' action"),
                arguments(BaseOverlay.OverlayActions.close, "Number of users changed for 'close' action")
        );
    }
}
