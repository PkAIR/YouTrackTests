package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import menus.CommonMenu;
import menus.Header;
import model.User;
import org.openqa.selenium.By;
import overlays.CreateUserOverlay;

import static com.codeborne.selenide.Selenide.*;

public class UsersPage {
    public menus.Header Header = Selenide.page(Header.class);
    public menus.CommonMenu CommonMenu = Selenide.page(CommonMenu.class);

    private SelenideElement createUserBtn = $(By.id("id_l.U.createNewUser"));
    private SelenideElement firstDeleteLink = $(By.xpath("(//td[count(span)=1]/ancestor::tr//a[@cn='l.U.usersList.deleteUser'])[1]"));
    private ElementsCollection deleteLinks = $$(By.xpath("//td[count(span)=1]/ancestor::tr//a[@cn='l.U.usersList.deleteUser']"));

    public CreateUserOverlay clickCreateUserBtn() {
        createUserBtn.click();

        return page(CreateUserOverlay.class);
    }

    public void deleteAllUsers() {
        for (int i = 0; i < deleteLinks.size(); i++)
        {
            firstDeleteLink.click();
            try {
                confirm();
            } catch (Exception ignored) {
            }
        }
    }

    public boolean isUserInTheTable(User user) {
        return $(By.xpath(buildUserRowXpath(user))).isDisplayed();
    }

    private String buildUserRowXpath(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("//tr[td[a[text()='%s']] ", user.getUsername()));
        if (!user.getFullName().equals("")) {
            sb.append(String.format("and td[div[@title='%s']] ", user.getFullName()));
        }
        if (!user.getEmail().equals("")) {
            sb.append(String.format("and td[div[@title='%s']] ", user.getEmail()));
        }
        if (!user.getJabber().equals("")) {
            sb.append(String.format("and td[div[@title='%s']] ", user.getJabber()));
        }

        sb.append("and td[a[normalize-space(text())='All Users'] ");
        sb.append("and a[normalize-space(text())='New Users']] ");
        sb.append("and td[a[normalize-space(text())='Delete'] ");
        sb.append("and a[normalize-space(text())='Merge'] ");
        sb.append("and a[normalize-space(text())='Ban']]]");

        return sb.toString();
    }

    public boolean pageIsVisible() {
        return createUserBtn.isDisplayed();
    }

    public UserDetailPage createUser(User user, boolean forcePasswordChange) {
        UsersPage up = page(UsersPage.class);

        CreateUserOverlay ov = up.clickCreateUserBtn();
        ov.createNewUser(user, forcePasswordChange);

        return page(UserDetailPage.class);
    }
}

