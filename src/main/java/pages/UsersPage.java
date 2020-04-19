package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import menus.CommonMenu;
import menus.Header;
import model.User;
import model.UserActions;
import model.UserGroup;
import org.openqa.selenium.By;
import overlays.CreateUserOverlay;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.*;

public class UsersPage {
    public menus.Header Header = Selenide.page(Header.class);
    public menus.CommonMenu CommonMenu = Selenide.page(CommonMenu.class);

    private SelenideElement redirectToRegistrationLnk = $(By.id("id_l.U.redirectToRegistration"));
    private SelenideElement createUserBtn = $(By.id("id_l.U.createNewUser"));
    private SelenideElement usersTable = $(By.xpath("//table[@class='table users-table']"));
    private ElementsCollection deleteLinks = $$(By.xpath("//td[count(span)=1]/ancestor::tr//a[@cn='l.U.usersList.deleteUser']"));
    private SelenideElement userCounterSpan = $(By.xpath("//th[normalize-space(text())='Login']/span"));
    private String  UserEditLinkXpathTemplate = "//a[@cn='l.U.usersList.UserLogin.editUser'][@title='%s']";

    public static UsersPage openUsersPageLink() {
        open("/users");

        return page(UsersPage.class);
    }

    public boolean isPageOpened() {
        return createUserBtn.has(Condition.enabled);
    }

    public CreateUserOverlay clickCreateUserBtn() {
        createUserBtn.click();

        return page(CreateUserOverlay.class);
    }

    public void deleteAllUsers() {
        for (int i = 0; i < deleteLinks.size(); i++)
        {
            SelenideElement deleteLink = $(By.xpath("//td[count(span)=1]/span[@class='user-status ']"))
                    .shouldBe(Condition.visible)
                    .closest("tr")
                    .shouldBe(Condition.visible)
                    .find(By.linkText("Delete"));

            deleteLink
                    .hover()
                    .click();
            try {
                confirm();
            }
            catch (Exception e) {
                deleteLink
                        .hover()
                        .click();
                confirm();
            }
        }
    }

    public void deleteUser(User user) {
        SelenideElement userEditLnk = $(By.xpath(String.format(UserEditLinkXpathTemplate, user.getUsername())));
        userEditLnk.shouldBe(Condition.visible);

        userEditLnk.closest("tr")
                .find(By.linkText("Delete"))
                .hover()
                .click();
        confirm();
    }

    public int getUserNumber() {
        refresh();
        userCounterSpan.shouldBe(Condition.appear);
        String numberOfUsers = userCounterSpan.getText();

        return Integer.parseInt(numberOfUsers.replaceAll("\\D+",""));
    }

    public boolean isUserInTheTable(User user) {
        refresh();
        usersTable.shouldBe(Condition.appear);

        return $(By.xpath(buildUserRowXpath(user, null))).isDisplayed();
    }

    public boolean isUserInTheTableWithActions(User user, ArrayList<UserActions> actions) {
        refresh();
        usersTable.shouldBe(Condition.appear);

        return $(By.xpath(buildUserRowXpath(user, actions))).isDisplayed();
    }

    private String buildUserRowXpath(User user, ArrayList<UserActions> actions) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("//tr[td[a[text()=\"%s\"]] ", user.getUsername()));
        if (!user.getFullName().equals("")) {
            sb.append(String.format("and td[div[@title=\"%s\"]] ", user.getFullName()));
        } else {
            sb.append(String.format("and td[div[@title=\"%s\"]] ", user.getUsername()));
        }
        if (!user.getEmail().equals("")) {
            sb.append(String.format("and td[div[@title=\"%s\"]] ", user.getEmail()));
        }
        if (!user.getJabber().equals("")) {
            sb.append(String.format("and td[div[@title=\"%s\"]] ", user.getJabber()));
        }

        if (user.getGroups() != null) {
            for (UserGroup group: user.getGroups()) {
                sb.append(String.format("and td[a[normalize-space(text())=\"%s\"]] ", group.getGroupName()));
            }
        }

        if (actions != null) {
            for (UserActions action : actions) {
                sb.append(String.format("and td[a[normalize-space(text())=\"%s\"]] ", action));
            }
        }

        sb.append("]");

        return sb.toString();
    }

    public UserDetailPage createUser(User user, boolean forcePasswordChange) {
        UsersPage up = page(UsersPage.class);

        CreateUserOverlay ov = up.clickCreateUserBtn();
        ov.createNewUser(user, forcePasswordChange);

        return page(UserDetailPage.class);
    }

    public UserDetailPage openUserDetailPage(User user) {
        SelenideElement userEditLnk = $(By.xpath(String.format(UserEditLinkXpathTemplate, user.getUsername())));
        userEditLnk.shouldBe(Condition.visible).click();

        return  page(UserDetailPage.class);
    }

    public UserRegistrationPage openUserRegistrationPage() {
        redirectToRegistrationLnk.shouldBe(Condition.enabled).click();

        return page(UserRegistrationPage.class);
    }
}

