package menus;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.refresh;

public class CommonMenu {
    private SelenideElement userCounterSpan = $(By.xpath("//a[@title='User list']//span[@class='admin-menu-counter']"));

    public int getUserNumber() {
        refresh();
        userCounterSpan.shouldBe(Condition.appear);
        String numberOfUsers = userCounterSpan.getText();

        return Integer.parseInt(numberOfUsers.substring(1, numberOfUsers.length() - 1));
    }
}
