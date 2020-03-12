package overlays;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import model.User;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Coordinates;
import pages.UserDetailPage;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.*;

public class CreateUserOverlay extends BaseOverlay{
    private SelenideElement createUserOverlay = $(By.id("id_l.U.cr.createUserDialog"));
    private SelenideElement headerOverlay = $(By.xpath("//div[@class='jt-panel-title'][text()='Create User']"));
    private SelenideElement loginFld = $(By.id("id_l.U.cr.login"));
    private SelenideElement passFld = $(By.id("id_l.U.cr.password"));
    private SelenideElement passConfirmationFld = $(By.id("id_l.U.cr.confirmPassword"));
    private SelenideElement forceSetPassCheckbox = $(By.id("id_l.U.cr.forcePasswordChange"));

    private SelenideElement fullNameFld = $(By.id("id_l.U.cr.fullName"));
    private SelenideElement emailFld = $(By.id("id_l.U.cr.email"));
    private SelenideElement jabberFld = $(By.id("id_l.U.cr.jabber"));

    private SelenideElement okBtn = $(By.id("id_l.U.cr.createUserOk"));
    private SelenideElement cancelBtn = $(By.id("id_l.U.cr.createUserCancel"));
    private SelenideElement closeBtn = $(By.id("id_l.U.cr.closeCreateUserDlg"));

    public void fillTheForm(User user, boolean forcePassChange) {
        createUserOverlay.shouldBe(Condition.visible);
        loginFld.clear();
        loginFld.sendKeys(user.getUsername());
        passFld.clear();
        passFld.sendKeys(user.getPassword());
        passConfirmationFld.sendKeys(user.getPasswordConfirmation());

        if (forcePassChange && forceSetPassCheckbox.is(not(Condition.checked))) {
            forceSetPassCheckbox.click();
        }

        fullNameFld.clear();
        fullNameFld.sendKeys(user.getFullName());
        emailFld.clear();
        emailFld.sendKeys(user.getEmail());
        jabberFld.clear();
        jabberFld.sendKeys(user.getJabber());
    }

    public UserDetailPage createNewUser(User user, boolean forcePassChange) {
        fillTheForm(user, forcePassChange);
        confirmOverlay();

        return page(UserDetailPage.class);
    }

    public void cancelOverlay() {
        cancelBtn.click();
    }

    public void confirmOverlay() {
        okBtn.click();
    }

    public void closeOverlay() {
        closeBtn.click();
    }

    public String getErrorTooltipText() {
        errorIndicator.shouldBe(Condition.visible);
        errorIndicator.hover();

        return errorTooltipIndicator.shouldBe(Condition.appears).text();
    }

    public boolean wasOverlayMoved() {
        Coordinates oldCoordinates =  createUserOverlay.getCoordinates();
        actions().dragAndDropBy(headerOverlay, 100, 100).perform();
        
        return !oldCoordinates.equals(createUserOverlay.getCoordinates());
    }
}
