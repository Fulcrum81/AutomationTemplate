package pages;

import base.PageBase;
import com.codeborne.selenide.SelenideElement;
import helpers.Waiter;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static helpers.Locators.get;

public class LubricantComponentsPage extends PageBase {
    private static final String TITLE = "";

    private static final By OVERVIEW_BUTTON = get("sbu.overview");
    private static final By SOLUTIONS_BUTTON = get("sbu.solutions");
    private static final By FAQS_BUTTON = get("sbu.faqs");
    private static final By PRODUCT_DATASHEETS_BUTTON = get("sbu.productdatasheets");
    private static final By INSIGHTS_AND_RESOURCES_BUTTON = get("sbu.insights&resources");

    private static final By FAQ_QUESTIONS = get("sbu.faqs.questions");
    private static final By FAQ_ANSWER = get("sbu.faqs.answer");

    public LubricantComponentsPage(){
        Waiter.waitForPageToLoad();
        //add code for page load time verification
    }

    public void shouldAppear() {
        shouldAppear(TITLE);
    }

    public LubricantComponentsPage gotoOverviewSection() {
        $(OVERVIEW_BUTTON).click();
        return this;
    }

    public LubricantComponentsPage gotoSolutionsSection() {
        $(SOLUTIONS_BUTTON).click();
        return this;
    }

//    public LubricantComponentsPage gotoFaqsSection() {
//        $(FAQS_BUTTON).click();
//        return this;
//    }

    public LubricantComponentsPage gotoProductDatasheetsSection() {
        $(PRODUCT_DATASHEETS_BUTTON).click();
        return this;
    }

    public LubricantComponentsPage gotoInsightsAndResourcesSection() {
        $(INSIGHTS_AND_RESOURCES_BUTTON).click();
        return this;
    }

    public LubricantComponentsPage checkFaqAccordion() {
        $(FAQS_BUTTON).click();

        for(SelenideElement element : $$(FAQ_QUESTIONS)) {
            element.click();
            element.find(FAQ_ANSWER).shouldBe(visible);
            element.click();
            element.find(FAQ_ANSWER).shouldNotBe(visible);
        }
        return this;
    }

}
