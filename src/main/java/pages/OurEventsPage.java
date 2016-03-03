package pages;


import base.PageBase;

public class OurEventsPage extends PageBase {
    private static final String TITLE = "Our events | COMAQA.BY – QA Automation Community Belarus";

    public static void shouldAppear() {
        shouldAppear(TITLE);
    }
}
