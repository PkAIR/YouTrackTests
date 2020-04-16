package overlays;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class ErrorOverlay {
    private SelenideElement errorSeverity = $(By.xpath("//li[@class='errorSeverity']"));
    private SelenideElement closeBtn = $(By.xpath("//a[@class='close']"));

    public String getErrorSeverityText() {
        closeBtn.should(Condition.appear);

        return errorSeverity.text();
    }
}
