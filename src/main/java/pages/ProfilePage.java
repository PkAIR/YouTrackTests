package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import model.User;
import model.UserGroup;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.refresh;

public class ProfilePage {
    public menus.Header Header = Selenide.page(menus.Header.class);

    private SelenideElement groupsCounterSpan = $(By.xpath("//span[@title='Number of groups']"));

    public boolean allGroupsAssigned(User user) {
        refresh();
        for (UserGroup group : user.getGroups()) {
            // Skipping 'All Users' group
            if (group.equals(UserGroup.AllUsers)) continue;
            $(By.xpath(String
                    .format("//div[@id='id_l.U.us.groupsSideBar']//span[@class='vert-list-value' and normalize-space(text())='%s']",
                            group.getGroupName()))).shouldBe(Condition.appear);
        }

        // Skipping 'All Users' group
        return user.getGroups().size() - 1 == getNumberOfGroups();
    }

    public int getNumberOfGroups() {
        refresh();
        groupsCounterSpan.shouldBe(Condition.appear);

        return Integer.parseInt(groupsCounterSpan.getText());
    }
}
