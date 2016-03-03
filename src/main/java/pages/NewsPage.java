package pages;

import base.PageBase;

public class NewsPage extends PageBase {

    private static final String TITLE = "News | COMAQA.BY – QA Automation Community Belarus";

    public static void shouldAppear() {
        shouldAppear(TITLE);
    }
}
