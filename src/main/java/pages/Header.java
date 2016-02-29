package pages;


import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static helpers.Locators.get;

public class Header {
    private static final By LANGUAGE_SWITCH = get("header.languageswitch");
    private static final By LUBRICANT_COMPONENTS_BUTTON = get("header.lubricantcomponents");


    public static LubricantComponentsPage goToLubricantComponents() {
        $(LUBRICANT_COMPONENTS_BUTTON).click();
        return new LubricantComponentsPage();
    }
}
