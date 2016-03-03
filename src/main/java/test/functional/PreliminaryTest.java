package test.functional;

import base.TestBase;
import helpers.CustomScreenShooter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Header;
import pages.SearchPage;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;

import static org.testng.AssertJUnit.assertTrue;

@Listeners(CustomScreenShooter.class)
@Features("Test feature")
@Test
public class PreliminaryTest extends TestBase {

    @Stories("Failure")
    @Test(groups = {"functional"})
    public void failingTest() {
        assertTrue("This test is failing for demo purposes to show what the fained tests look line in the report", false);
    }

    @Stories("Header")
    @Test(groups = {"functional"})
    public void headerClickthroughTest() {
        Header.goToEventsPage();
        Header.goToNewsPage();
        Header.goToOurEventsPage();
    }

    @Stories("Search")
    @Test(groups = {"functional"})
    public void searchTest() {
        String searchItem = "Selenium";

        Header.goToSearchPage();
        SearchPage.searchFor(searchItem);
        assertTrue(String.format("Search dod not return any matches for '%s'", searchItem), SearchPage.searchResultMatches(searchItem));
    }

    @Stories("New story")
    @Test(enabled = false, groups = {"functional"})
    public void nonImplementedTest() {
        //This test is not yet implemented
    }
}
