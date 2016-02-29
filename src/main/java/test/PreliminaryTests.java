package test;

import base.TestBase;
import org.testng.annotations.Test;
import pages.Header;
import pages.HomePage;

public class PreliminaryTests extends TestBase {

    @Test
    public void homepageTest() {
        HomePage.shouldAppear();

    }

    @Test()
    public void lubricantComponentTest() {
        Header.goToLubricantComponents()
                .gotoOverviewSection()
                .gotoSolutionsSection()
                .checkFaqAccordion()
                .gotoProductDatasheetsSection()
                .gotoInsightsAndResourcesSection();

    }
}
