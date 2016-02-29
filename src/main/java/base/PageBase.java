package base;

import helpers.Waiter;

public class PageBase {

    protected static void shouldAppear(String title) {
        Waiter.waitForPageTitle(title);
        //Waiter.waitForPageToLoad();
        Waiter.waitForJquery();
    }
}
