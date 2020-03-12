package pages;

import com.codeborne.selenide.SelenideElement;
import model.User;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    private SelenideElement loginFld = $(By.id("id_l.L.login"));
    private SelenideElement passwordFld = $(By.id("id_l.L.password"));
    private SelenideElement loginBtn = $(By.id("id_l.L.loginButton"));

    public DashboardPage loginAs(User user) {
        loginFld.clear();
        loginFld.sendKeys(user.getUsername());
        passwordFld.clear();
        passwordFld.sendKeys(user.getPassword());
        loginBtn.click();

        return page(DashboardPage.class);
    }

    public boolean pageIsVisible() {
        return loginFld.isDisplayed();
    }
}
