package basetests;

import model.User;
import org.junit.jupiter.api.BeforeAll;
import pages.DashboardPage;
import pages.LoginPage;

import java.io.IOException;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.open;

public class BaseTestOneUser {
    protected static String baseUrl;
    protected static User rootUser;

    @BeforeAll
    public static void setUp() throws IOException {
        Properties prop = new Properties();
        prop.load(BaseTestOneUser.class.getClassLoader().getResourceAsStream("application.properties"));
        baseUrl = prop.getProperty("base.url");

        rootUser = new User(prop.getProperty("rootuser.login"), prop.getProperty("rootuser.password"),
                "", "", "");

        LoginPage lp = open(baseUrl, LoginPage.class);
        DashboardPage dp = lp.loginAs(rootUser);
        dp.Header.openUsersPage();
    }
}
