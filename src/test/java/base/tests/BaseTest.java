package base.tests;

import model.User;
import model.UserGroup;
import pages.DashboardPage;
import pages.LoginPage;
import pages.UsersPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;

public class BaseTest {
    protected static String baseUrl;
    protected static User rootUser;

    public static void commonSetup() throws IOException {
        Properties prop = new Properties();
        prop.load(BaseTest.class.getClassLoader().getResourceAsStream("application.properties"));
        baseUrl = prop.getProperty("base.url");

        ArrayList<UserGroup> rootUserGroups = new ArrayList<UserGroup>() {
            {
                add(UserGroup.AllUsers);
            }
        };

        rootUser = new User(prop.getProperty("rootuser.login"), prop.getProperty("rootuser.password"),
                "root", "root", "", rootUserGroups);
    }

    public static LoginPage openLoginPage() {
        open(baseUrl);

        return LoginPage.openLoginPageLink();
    }

    public static UsersPage openUsersPageAsRootUser() {
        LoginPage lp = openLoginPage();
        DashboardPage dp = lp.loginAs(rootUser);

        return dp.Header.openUsersPage();
    }

    public static void logOutUser() {
        DashboardPage dp = page(DashboardPage.class);
        dp.Header.logOutUser();
    }

    public static void deleteAllUsers() {
        UsersPage up = page(UsersPage.class);
        up.deleteAllUsers();
    }
}
