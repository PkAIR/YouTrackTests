package create.user.tests.admin.pages;

import base.tests.BaseDdtTest;
import model.User;
import model.UserFactory;
import model.UserGroup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import overlays.BaseOverlay;
import overlays.SelectGroupOverlay;
import pages.*;

import java.util.ArrayList;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.WebDriverRunner.source;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRoleTests extends BaseDdtTest {
    private static User testUser;

    @BeforeAll
    public static void setUp() {
        testUser = UserFactory.getUserMandatoryFldsOnly();
        UsersPage up = page(UsersPage.class);
        UserDetailPage udp = up.createUser(testUser, false);
        assertTrue(udp.isUserCreated(testUser),
                String.format("User '%s' wasn't created", testUser.getUsername()));
        up.Header.openUsersPage();
    }

    @DisplayName("Add 'Reporter' role to user test")
    @Tag("Smoke")
    @Test
    @Order(1)
    public void addUserRoleTest() {
        UsersPage up = page(UsersPage.class);
        UserDetailPage upd = up.openUserDetailPage(testUser);
        upd.addGroups(new ArrayList<UserGroup>() {{
            add(UserGroup.Reporters);
        }});

        testUser.getGroups().add(UserGroup.Reporters);
        assertTrue(upd.allGroupsAssigned(testUser), "Not all groups were found for user on User Detail page");
        upd.Header.openUsersPage();
        assertTrue(up.isUserInTheTable(testUser), "Not all groups were found for user on Users page");
        checkUserRoles(testUser);
    }

    @DisplayName("Delete assigned 'Reporter' role from user test")
    @Tag("Smoke")
    @Test
    @Order(2)
    public void deleteUserRoleTest() {
        UsersPage up = page(UsersPage.class);
        UserDetailPage upd = up.openUserDetailPage(testUser);
        upd.deleteGroups(new ArrayList<UserGroup>() {{
            add(UserGroup.Reporters);
        }});

        testUser.getGroups().remove(UserGroup.Reporters);
        assertTrue(upd.allGroupsAssigned(testUser), "Reporters group wasn't deleted on User Detail page");
        upd.Header.openUsersPage();
        assertTrue(up.isUserInTheTable(testUser), "Reporters group wasn't deleted for user on Users page");
        checkUserRoles(testUser);
    }

    @DisplayName("Closing / canceling add group overlay won't add groups test")
    @Tag("Regression")
    @ParameterizedTest
    @MethodSource("testDataForOverlayClosingProvider")
    @Order(3)
    public void rolesNotAddedTests(BaseOverlay.OverlayActions action) {
        UsersPage up = page(UsersPage.class);
        UserDetailPage upd = up.openUserDetailPage(testUser);
        ArrayList<UserGroup> testGroups = new ArrayList<UserGroup>() {{
            add(UserGroup.Reporters);
        }};

        SelectGroupOverlay sgo = upd.openSelectGroupOverlay();
        sgo.checkGroups(testGroups);
        switch (action) {
            case cancel: {
                sgo.cancelOverlay();
                break;
            }
            case close: {
                sgo.closeOverlay();
                break;
            }
        }

        assertTrue(upd.allGroupsAssigned(testUser),
                String.format("Reporters group was added anyway on User Detail page after '%smethod'", action));
        upd.Header.openUsersPage();
        assertTrue(up.isUserInTheTable(testUser),
            String.format("Reporters group was added anyway for user on Users page after '%smethod'", action));
        checkUserRoles(testUser);
    }

    @DisplayName("Delete all assigned groups from user test")
    @Tag("Regression")
    @Test
    @Order(4)
    public void deleteAllUserRolesTest() {
        UsersPage up = page(UsersPage.class);
        UserDetailPage udp = up.openUserDetailPage(testUser);
        udp.deleteGroups(new ArrayList<UserGroup>() {{
            add(UserGroup.NewUsers);
        }});

        assertEquals("Add to groups", udp.getAddRemoveGroupsLnkText(),
                String.format("User '%s' has groups in the table on detail page", testUser.getUsername()));
        testUser.getGroups().remove(UserGroup.NewUsers);
        udp.Header.openUsersPage();
        assertTrue(up.isUserInTheTable(testUser), "New Users group wasn't deleted for user on Users page");

        openProfilePageForUser(testUser);
        assertTrue(source().contains("You have no permissions to view this page"),
                String.format("User page can be seen by user '%s'", testUser.getUsername()));
    }

    private static Stream<Arguments> testDataForOverlayClosingProvider() {
        return Stream.of(
                arguments(BaseOverlay.OverlayActions.cancel),
                arguments(BaseOverlay.OverlayActions.close)
        );
    }

    private void checkUserRoles(User testUser) {
        ProfilePage pp = openProfilePageForUser(testUser);
        assertTrue(pp.allGroupsAssigned(testUser), "Not all groups are assigned for user");
    }

    private ProfilePage openProfilePageForUser(User testUser) {
        LoginPage.openLoginPageLink()
                .loginAs(testUser);

        return ProfilePage.openProfilePageLink();
    }

    @AfterEach
    public void afterEach() {
        LoginPage lp = openLoginPage();
        DashboardPage dp = lp.loginAs(rootUser);
        dp.Header.openUsersPage();
    }
}
