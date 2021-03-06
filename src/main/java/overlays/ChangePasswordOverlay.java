package overlays;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import model.User;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class ChangePasswordOverlay extends BaseOverlay {
    private SelenideElement changePasswordOverlay = $(By.id("id_l.U.ChangePasswordDialog.changePasswordDlg"));
    private SelenideElement oldPasswordFld = $(By.id("id_l.U.ChangePasswordDialog.oldPassword"));
    private SelenideElement newPasswordFld = $(By.id("id_l.U.ChangePasswordDialog.newPassword1"));
    private SelenideElement newPasswordConfirmationFld = $(By.id("id_l.U.ChangePasswordDialog.newPassword2"));

    private SelenideElement okBtn = $(By.id("id_l.U.ChangePasswordDialog.passOk"));

    public void fillTheForm(User user) {
        changePasswordOverlay.shouldBe(Condition.visible);

        oldPasswordFld.setValue(user.getPassword());
        newPasswordFld.setValue(user.getNewPassword());
        newPasswordConfirmationFld.setValue(user.getNewPasswordConfirmation());
    }

    public void changeThePassword(User user) {
        fillTheForm(user);
        okBtn.click();
        user.setPassword(user.getNewPassword());
        user.setPasswordConfirmation(user.getNewPassword());
    }
}
