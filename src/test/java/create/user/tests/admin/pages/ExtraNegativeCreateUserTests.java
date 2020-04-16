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
import static com.codeborne.selenide.Selenide.refresh;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ExtraNegativeCreateUserTests extends BaseDdtTest {
    @BeforeAll
    public static void DeleteAllUsers() {
        deleteAllUsers();
    }

    @ParameterizedTest
    @CsvSource({"&specialTest,a,'','',''",
                "\\nnewLineUser,a,'','',''",
                "%root%,a,'','',''",
                "%%%root%%%,a,'','',''",
                "!@#$%^&^%$#@!,a,'','',''",
                "EmailWithout@,a,'','testmail',''",
                "EmailWithoutDomain,a,'','testmail@abc',''",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa,a,'','',''",
    })
    public void fullName_ShouldGenerateTheExpectedFullName(
            @AggregateWith(UserAggregator.class) User user) {

        UsersPage up = page(UsersPage.class);
        DashboardPage dp = page(DashboardPage.class);

        UserDetailPage udp = up.createUser(user, false);
        try {
            assertFalse(udp.isUserCreated(user),
                    String.format("User %s was created", user.getUsername()));
        } finally {
            refresh();
            dp.Header.openUsersPage();
        }
    }
}
