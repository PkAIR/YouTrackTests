package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import model.User;
import model.UserGroup;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    public menus.Header Header = Selenide.page(menus.Header.class);

    private SelenideElement groupsCounterSpan = $(By.xpath("//span[@title='Number of groups']"));

    public static ProfilePage openProfilePageLink() {
        open("/user");

        return page(ProfilePage.class);
    }

    public boolean allGroupsAssigned(User user) {
        refresh();
        for (UserGroup group : user.getGroups()) {
            // Skipping 'All Users' group - not visible on user profile page
            if (group.equals(UserGroup.AllUsers)) continue;
            $(By.xpath(String
                    .format("//div[@id='id_l.U.us.groupsSideBar']//span[@class='vert-list-value' and normalize-space(text())=\"%s\"]",
                            group.getGroupName()))).shouldBe(Condition.visible);
        }

        // Skipping 'All Users' group
        return user.getGroups().size() - 1 == getNumberOfGroups();
    }

    public int getNumberOfGroups() {
        refresh();
        groupsCounterSpan.shouldBe(Condition.visible);

        return Integer.parseInt(groupsCounterSpan.getText());
    }
}
