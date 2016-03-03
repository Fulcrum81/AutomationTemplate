package pages;

import base.PageBase;
import com.codeborne.selenide.SelenideElement;
import helpers.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static helpers.Locators.get;

public class SearchPage extends PageBase {
    private static final String TITLE = "COMAQA.BY – QA Automation Community Belarus";

    private static final By SEARCH_FIELD = get("search.search-field");
    private static final By CLEAR_SEARCH_BUTTON = get("search.clear-search-button");
    private static final By SEARCH_TEXT = get("search.search-text");
    private static final By SEARCH_RESULTS = get("search.search-results");

    public static void shouldAppear() {
        shouldAppear(TITLE);
    }

    public static void searchFor(String searchText) {
        $(SEARCH_FIELD).sendKeys(searchText + Keys.ENTER);
        Waiter.waitForJquery();
    }

    public static boolean searchResultMatches(String result) {

        for (SelenideElement resultTitle : $$(SEARCH_RESULTS)) {
            if (resultTitle.getAttribute("innerHTML").contains(result)) {
                return true;
            }
        }
        return false;
    }

}
