package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import menus.Header;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    public menus.Header Header = Selenide.page(Header.class);

    private SelenideElement searchFld = $(By.id("id_l.D.sb.searchField"));

    public boolean pageIsVisible() {
        return searchFld.isDisplayed();
    }
}
