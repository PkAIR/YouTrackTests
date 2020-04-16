package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import model.User;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    private SelenideElement registerLnk = $(By.xpath("//a[@href='/registerUserForm']"));

    private SelenideElement loginFld = $(By.id("id_l.L.login"));
    private SelenideElement passwordFld = $(By.id("id_l.L.password"));
    private SelenideElement loginBtn = $(By.id("id_l.L.loginButton"));


    public boolean isPageOpened() {
        return loginFld.has(Condition.enabled);
    }

    public static LoginPage openLoginPage() {
        open("/login");

        return page(LoginPage.class);
    }

    public DashboardPage loginAs(User user) {
        loginFld.setValue(user.getUsername());
        passwordFld.setValue(user.getPassword());
        loginBtn.click();

        return page(DashboardPage.class);
    }

    public UserRegistrationPage openUserRegistrationPage() {
        registerLnk.shouldBe(Condition.enabled).click();

        return page(UserRegistrationPage.class);
    }
}
