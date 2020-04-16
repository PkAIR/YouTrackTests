package create.user.tests.admin.pages;

import base.tests.BaseDdtTest;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import pages.DashboardPage;
import pages.UserDetailPage;
import pages.UsersPage;
import test.aggregators.UserAggregator;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtraPositiveCreateUserTests extends BaseDdtTest {
    @BeforeAll
    public static void DeleteAllUsers() {
        deleteAllUsers();
    }

    @ParameterizedTest
    @CsvSource({"&specialTest,a,'','',''",
                "\\nnewLineUser,a,'','',''",
                "%root%,a,'','',''",
                "%%%root%%%,a,'','',''"})
    public void fullName_ShouldGenerateTheExpectedFullName(
            @AggregateWith(UserAggregator.class) User user) {
       createUser(user, false);
    }

    private void createUser(User testUser, boolean forcePasswordChange) {
        UsersPage up = page(UsersPage.class);
        DashboardPage dp = page(DashboardPage.class);

        int curNumOfUsers = up.CommonMenu.getUserNumber();
        UserDetailPage udp = up.createUser(testUser, forcePasswordChange);
        assertTrue(udp.isUserCreated(testUser),
                String.format("User %s wasn't created", testUser.getUsername()));
        assertTrue(up.CommonMenu.getUserNumber() > curNumOfUsers,
                "Number of users doesn't change");

        dp.Header.openUsersPage();
    }
}
