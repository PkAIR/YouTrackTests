package create.user.tests.admin.pages;

import base.tests.BaseDdtTest;
import model.User;
import model.UserFactory;
import model.UserGroup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import overlays.SelectGroupOverlay;
import pages.*;

import java.util.ArrayList;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.page;
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
                String.format("User %s wasn't created", testUser.getUsername()));
        up.Header.openUsersPage();
    }

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
        up.openUsersPage();
        assertTrue(up.isUserInTheTable(testUser), "Not all groups were found for user on Users page");
        checkUserRoles(testUser);
    }

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
        up.openUsersPage();
        assertTrue(up.isUserInTheTable(testUser), "Reporters group wasn't deleted for user on Users page");
        checkUserRoles(testUser);
    }

    @ParameterizedTest
    @MethodSource("testDataForOverlayClosingProvider")
    @Order(3)
    public void rolesNotAddedTests(String method) {
        UsersPage up = page(UsersPage.class);
        UserDetailPage upd = up.openUserDetailPage(testUser);
        ArrayList<UserGroup> testGroups = new ArrayList<UserGroup>() {{
            add(UserGroup.Reporters);
        }};

        SelectGroupOverlay sgo = upd.openSelectGroupOverlay();
        sgo.checkGroups(testGroups);
        switch (method) {
            case "cancel":{
                sgo.cancelOverlay();
                break;
            }
            case "close" : {
                sgo.closeOverlay();
                break;
            }
        }

        assertTrue(upd.allGroupsAssigned(testUser),
                String.format("Reporters group was added anyway on User Detail page after '%smethod'", method));
        up.openUsersPage();
        assertTrue(up.isUserInTheTable(testUser),
            String.format("Reporters group was added anyway for user on Users page after '%smethod'", method));
        checkUserRoles(testUser);
    }

    private static Stream<Arguments> testDataForOverlayClosingProvider() {
        return Stream.of(
                arguments("cancel"),
                arguments("close")
        );
    }

    private void checkUserRoles(User testUser) {
        UsersPage up = page(UsersPage.class);
        ProfilePage pp = up.Header.openProfilePage();
        up.Header.logOutUser(rootUser);

        LoginPage lp = page(LoginPage.class);
        DashboardPage dp = lp.loginAs(testUser);
        dp.Header.openProfilePage();
        assertTrue(pp.allGroupsAssigned(testUser), "Not all groups are assigned for user");
    }

    @AfterEach
    public void afterEach() {
        LoginPage lp = openLoginPage();
        DashboardPage dp = lp.loginAs(rootUser);
        dp.Header.openUsersPage();
    }
}
