package base.tests;

import model.User;
import pages.DashboardPage;
import pages.LoginPage;

import java.io.IOException;
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

        rootUser = new User(prop.getProperty("rootuser.login"), prop.getProperty("rootuser.password"),
                "", "", "");
    }

    public static void openUsersPageAsRootUser() {
        LoginPage lp = open(baseUrl, LoginPage.class);
        DashboardPage dp = lp.loginAs(rootUser);
        dp.Header.openUsersPage();
    }

    public static void logOutUser() {
        DashboardPage dp = page(DashboardPage.class);
        dp.Header.logOutUser();
    }
}