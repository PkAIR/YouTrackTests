package commonmenus;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import model.User;
import org.openqa.selenium.By;
import pages.LoginPage;
import pages.UsersPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class Header {
    private SelenideElement userNameLink = $(By.xpath("//a[contains(@data-ring-dropdown, 'Log out')]"));
    private SelenideElement logOutBtn = $(By.xpath("//a[text()='Log out']"));
    private SelenideElement gearIcon = $(By.xpath("//span[@class='ring-menu__item__i ring-font-icon ring-font-icon_cog']"));
    private SelenideElement usersLink = $(By.xpath("//a[@class='ring-dropdown__item ring-link'][text()='Users']"));
    private SelenideElement questionMarkIcon = $(By.xpath("//span[@class='ring-menu__item__i ring-font-icon ring-font-icon_help']"));

    public LoginPage logOutUser(User user) {
        $(By.xpath(String.format("//span[@class='ring-menu__item__i'][text()='%s']",
                user.getFullName().equals("") ? user.getUsername() : user.getFullName()))).hover().click();
        logOutBtn.should(Condition.appear).click();

        return page(LoginPage.class);
    }

    public LoginPage logOutUser() {
        if (isSmbLoggedIn()) {
            userNameLink.click();
            logOutBtn.click();
        }

        return page(LoginPage.class);
    }

    public UsersPage openUsersPage() {
        gearIcon.click();
        usersLink.click();

        return page(UsersPage.class);
    }

    public boolean isSmbLoggedIn() {
        return questionMarkIcon.isDisplayed();
    }
}
