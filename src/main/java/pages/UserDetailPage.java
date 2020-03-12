package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import menus.CommonMenu;
import menus.Header;
import model.User;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;

public class UserDetailPage {
    public menus.Header Header = Selenide.page(Header.class);
    public menus.CommonMenu CommonMenu = Selenide.page(CommonMenu.class);

    private SelenideElement usernameSpan = $(By.xpath("//li[@class='breadcrumb-item'][last()]"));

    public boolean isUserCreated(User user) {
        return (usernameSpan.getText().contains(user.getUsername())
                || usernameSpan.getText().contains(user.getFullName())) && url().contains(user.getUsername());
    }
}
