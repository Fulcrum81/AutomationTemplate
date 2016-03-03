package pages;


import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static helpers.Locators.get;

public class Header {
    private static final By NEWS = get("header.news");
    private static final By EVENTS = get("header.events");
    private static final By OUR_EVENTS = get("header.our-events");
    private static final By EDUCATION = get("header.education");
    private static final By VIDEO = get("header.video");
    private static final By ABOUT_US = get("header.about-us");
    private static final By CONTACT_US = get("header.contact-us");
    private static final By RUSSIAN_LANGUAGE = get("header.russian-language-switch");
    private static final By ENGLISH_LANGUAGE = get("header.english-language-switch");
    private static final By RSS_FEED = get("header.rss-feed");
    private static final By SEARCH = get("header.search");

    public static void goToNewsPage() {
        $(NEWS).click();
        NewsPage.shouldAppear();
    }

    public static void goToEventsPage() {
        $(EVENTS).click();
        EventsPage.shouldAppear();
    }

    public static void goToOurEventsPage() {
        $(OUR_EVENTS).click();
        OurEventsPage.shouldAppear();
    }

    public static void goToSearchPage() {
        $(SEARCH).click();
        SearchPage.shouldAppear();
    }
}
