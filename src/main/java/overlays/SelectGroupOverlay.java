package overlays;

import com.codeborne.selenide.SelenideElement;
import model.UserGroup;
import org.openqa.selenium.By;

import java.util.ArrayList;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class SelectGroupOverlay {
    SelenideElement closeBtn = $(By.id("id_l.E.EditUserGroups.SelectGroupsDialog.closeSelectGroupsDlg"));
    SelenideElement cancelBtn = $(By.id("id_l.E.EditUserGroups.SelectGroupsDialog.selectGroupCancel"));
    SelenideElement okBtn = $(By.id("id_l.E.EditUserGroups.SelectGroupsDialog.selectGroupOk"));

    SelenideElement searchFld = $(By.id("id_l.E.EditUserGroups.SelectGroupsDialog.selectGroupMulti"));

    private void selectGroups(ArrayList<UserGroup> groups, boolean status) {
        okBtn.shouldBe(visible);

        for (UserGroup group: groups) {
            SelenideElement groupCheckbox = $(By.xpath(String.format("//span[text()=\"%s\"]/preceding-sibling::span",
                    group.getGroupName())));

            if (status) {
                if (!groupCheckbox.has(cssClass("checked"))) {
                    groupCheckbox.shouldBe(visible)
                            .hover()
                            .click();
                }
            } else {
                if (groupCheckbox.has(cssClass("checked"))) {
                    groupCheckbox.shouldBe(visible)
                            .hover()
                            .click();
                }
            }
        }
    }

    public void checkGroups(ArrayList<UserGroup> groups) {
        selectGroups(groups, true);
    }

    public void uncheckGroups(ArrayList<UserGroup> groups) {
        selectGroups(groups, false);
    }

    public void closeOverlay() {
        closeBtn.click();
    }

    public void cancelOverlay() {
        cancelBtn.click();
    }

    public void confirmOverlay() {
        okBtn.click();
    }

    public void addGroupsToUser(ArrayList<UserGroup> groups) {
        checkGroups(groups);

        confirmOverlay();
    }

    public void removeGroupsFromUser(ArrayList<UserGroup> groups) {
        uncheckGroups(groups);

        confirmOverlay();
    }
}
