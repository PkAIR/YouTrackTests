package overlays;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class BaseOverlay {
    protected SelenideElement errorIndicator = $(By.cssSelector("div.error-bulb2"));
    protected SelenideElement errorTooltipIndicator = $(By.cssSelector("div.error-tooltip.tooltip"));
}
