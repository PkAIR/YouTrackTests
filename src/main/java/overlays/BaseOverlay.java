package overlays;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class BaseOverlay {
    protected SelenideElement errorIndicator = $(By.xpath("(//div[@class='error-bulb2'])[1]"));
    protected SelenideElement errorTooltipIndicator = $(By.xpath("(//div[@class='error-tooltip tooltip'])[1]"));

    public enum OverlayActions {
        cancel,
        close
    }

    public String getErrorTooltipText() {
        errorIndicator.shouldBe(Condition.visible);
        errorIndicator.hover();

        return errorTooltipIndicator.shouldBe(Condition.appears).text();
    }
}
