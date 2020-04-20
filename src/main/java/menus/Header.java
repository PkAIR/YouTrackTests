package menus;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import model.User;
import org.openqa.selenium.By;
import pages.LoginPage;
import pages.ProfilePage;
import pages.UsersPage;

import static com.codeborne.selenide.Selenide.*;

public class Header {
    private SelenideElement userNameLink = $(By.xpath("//a[contains(@data-ring-dropdown, 'Log out')]"));
    private SelenideElement logOutBtn = $(By.xpath("//a[text()='Log out']"));
    private SelenideElement gearIcon = $(By.xpath("//span[@class='ring-menu__item__i ring-font-icon ring-font-icon_cog']"));
    private SelenideElement usersLink = $(By.xpath("//a[@class='ring-dropdown__item ring-link'][text()='Users']"));
    private SelenideElement profileLink = $(By.xpath("//a[text()='Profile']"));
    private SelenideElement questionMarkIcon = $(By.xpath("//span[@class='ring-menu__item__i ring-font-icon ring-font-icon_help']"));

    public LoginPage logOutUser(User user) {
        if (isSmbLoggedIn()) {
            $(By.xpath(String.format("//span[@class='ring-menu__item__i'][text()=\"%s\"]",
                user.getFullName().equals("") ? user.getUsername() : user.getFullName())))
                    .shouldBe(Condition.enabled)
                    .click();
            logOutBtn.should(Condition.appear)
                    .hover()
                    .click();
        }

        return page(LoginPage.class);
    }

    public LoginPage logOutUser() {
        if (isSmbLoggedIn()) {
            userNameLink.shouldBe(Condition.enabled).click();
            logOutBtn
                    .hover()
                    .click();
        }

        return page(LoginPage.class);
    }

    public UsersPage openUsersPage() {
        gearIcon.shouldBe(Condition.enabled).click();
        usersLink
                .hover()
                .click();

        return page(UsersPage.class);
    }

    public ProfilePage openProfilePage() {
        if (isSmbLoggedIn()) {
            userNameLink.shouldBe(Condition.enabled).click();
            profileLink
                    .hover()
                    .click();
        }

        return page(ProfilePage.class);
    }

    public boolean isSmbLoggedIn() {
        try {
            return questionMarkIcon.waitUntil(Condition.enabled, 2000).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
