package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import model.User;
import org.openqa.selenium.By;
import overlays.BaseOverlay;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.*;

public class UserRegistrationPage extends BaseOverlay {
    private SelenideElement logInLnk = $(By.xpath("//a[@href='/login']"));

    private SelenideElement fullNameFld = $(By.id("id_l.R.user_fullName"));
    private SelenideElement emailFld = $(By.id("id_l.R.user_email"));
    private SelenideElement loginFld = $(By.id("id_l.R.user_login"));
    private SelenideElement passwordFld = $(By.id("id_l.R.password"));
    private SelenideElement confirmPasswordFld = $(By.id("id_l.R.confirmPassword"));
    private SelenideElement rememberMeLabel = $(By.id("id_l.R.rememberMe_Label"));

    private SelenideElement registerBtn = $(By.id("id_l.R.register"));

    public static UserRegistrationPage openUserRegistrationPageLink() {
        open("/registerUserForm");

        return page(UserRegistrationPage.class);
    }

    public boolean isPageOpened() {
        return registerBtn.has(Condition.enabled);
    }

    public LoginPage openLoginPage() {
        loginFld.shouldBe(Condition.enabled)
                .click();
        return page(LoginPage.class);
    }

    public void fillTheForm(User user, boolean rememberMeFlag) {
        fullNameFld.shouldBe(Condition.enabled)
            .setValue(user.getFullName());
        emailFld.setValue(user.getEmail());
        loginFld.setValue(user.getUsername());
        passwordFld.setValue(user.getPassword());
        confirmPasswordFld.setValue(user.getPasswordConfirmation());

        setRememberMeFlag(rememberMeFlag);
    }

    private void setRememberMeFlag(boolean rememberMeFlag) {
        SelenideElement rememberMeCheckbox = $(By.id("id_l.R.rememberMe_"))
                .closest("div")
                .find(By.className("jt-custom-checkbox"));
        final String isCheckedClassName = "jt-custom-checkbox-checked";

        if (rememberMeFlag) {
            if (!rememberMeCheckbox
                    .has(cssClass(isCheckedClassName))) {
                rememberMeLabel.hover().click();
            }
        } else {
            if (rememberMeCheckbox
                .has(cssClass(isCheckedClassName))) {
                    rememberMeLabel.hover().click();
            }
        }
    }

    public void confirmForm() {
        registerBtn.click();
    }

    public DashboardPage createNewUser(User user, boolean rememberMeFlag) {
        fillTheForm(user, rememberMeFlag);
        confirmForm();

        return page(DashboardPage.class);
    }
}
