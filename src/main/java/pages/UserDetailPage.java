package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import menus.CommonMenu;
import menus.Header;
import model.User;
import model.UserGroup;
import org.openqa.selenium.By;
import overlays.SelectGroupOverlay;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.*;

public class UserDetailPage {
    public menus.Header Header = Selenide.page(Header.class);
    public menus.CommonMenu CommonMenu = Selenide.page(CommonMenu.class);

    private SelenideElement usernameSpan = $(By.xpath("//li[@class='breadcrumb-item'][last()]"));
    private SelenideElement groupsCounterSpan = $(By.xpath("//th[normalize-space(text())='Group name']/span"));
    private SelenideElement addUserGroupLnk = $(By.id("id_l.E.EditUserGroups.addUserToGroup"));

    public boolean isUserCreated(User user) {
        String breadCrumbUserName = usernameSpan.getText();

        return (breadCrumbUserName.contains(user.getUsername()) || breadCrumbUserName.contains(user.getFullName()));
    }

    public boolean allGroupsAssigned(User user) {
        refresh();
        for (UserGroup group : user.getGroups()) {
            $(By.xpath(String.format("//td/a[normalize-space(text())='%s']", group.getGroupName()))).shouldBe(Condition.appear);
        }

        return user.getGroups().size() == getNumberOfGroups();
    }

    public void addGroups(ArrayList<UserGroup> groups) {
        addUserGroupLnk.click();
        SelectGroupOverlay sg = page(SelectGroupOverlay.class);

        sg.addGroupsToUser(groups);
    }

    public void deleteGroups(ArrayList<UserGroup> groups) {
        addUserGroupLnk.click();
        SelectGroupOverlay sg = page(SelectGroupOverlay.class);

        sg.removeGroupsFromUser(groups);
    }

    public int getNumberOfGroups() {
        refresh();
        groupsCounterSpan.shouldBe(Condition.appear);
        String numberOfGroups = groupsCounterSpan.getText();

        return Integer.parseInt(numberOfGroups.replaceAll("\\D+",""));
    }

    public SelectGroupOverlay openSelectGroupOverlay() {
        addUserGroupLnk.click();

        return page(SelectGroupOverlay.class);
    }
}
