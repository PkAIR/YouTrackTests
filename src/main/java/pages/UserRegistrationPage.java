package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import model.User;
import org.openqa.selenium.By;
import overlays.BaseOverlay;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class UserRegistrationPage extends BaseOverlay {
    private SelenideElement logInLnk = $(By.xpath("//a[@href='/login']"));

    private SelenideElement fullNameFld = $(By.id("id_l.R.user_fullName"));
    private SelenideElement emailFld = $(By.id("id_l.R.user_email"));
    private SelenideElement loginFld = $(By.id("id_l.R.user_login"));
    private SelenideElement passwordFld = $(By.id("id_l.R.password"));
    private SelenideElement confirmPasswordFld = $(By.id("id_l.R.confirmPassword"));
    private SelenideElement rememberMeCLabel = $(By.id("id_l.R.rememberMe_Label"));

    // class checked = jt-custom-checkbox login-input jt-custom-checkbox-checked
    // class unchecked = jt-custom-checkbox login-input

    private SelenideElement registerBtn = $(By.id("id_l.R.register"));

    public LoginPage openLoginPage() {
        loginFld.shouldBe(Condition.visible)
                .click();
        return page(LoginPage.class);
    }

    public void fillTheForm(User user, boolean rememberMeFlag) {
        fullNameFld.shouldBe(Condition.enabled);
        fullNameFld.setValue(user.getFullName());
        emailFld.setValue(user.getEmail());
        loginFld.setValue(user.getUsername());
        passwordFld.setValue(user.getPassword());
        confirmPasswordFld.setValue(user.getPasswordConfirmation());

        setRememberMeFlag(rememberMeFlag);
    }

    private void setRememberMeFlag(boolean rememberMeFlag) {
        if (rememberMeFlag) {
            if (!$(By.id("id_l.R.rememberMe_"))
                    .closest("div")
                    .find(By.className("jt-custom-checkbox"))
                    .has(cssClass("jt-custom-checkbox-checked"))) {
                rememberMeCLabel.hover().click();
            }
        } else {
            if ($(By.id("id_l.R.rememberMe_"))
                .closest("div")
                .find(By.className("jt-custom-checkbox"))
                .has(cssClass("jt-custom-checkbox-checked"))) {
                    rememberMeCLabel.hover().click();
            }
        }
    }

    public void confirmForm() {
        registerBtn.shouldBe(Condition.enabled)
                .click();
    }

    public DashboardPage createNewUser(User user, boolean rememberMeFlag) {
        fillTheForm(user, rememberMeFlag);
        confirmForm();

        return page(DashboardPage.class);
    }

    public boolean isPageOpened() {
        return registerBtn.has(Condition.enabled);
    }
}
