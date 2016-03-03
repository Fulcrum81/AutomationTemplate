package test.performance;

import base.PerformanceTestBase;
import org.testng.annotations.Test;
import pages.Header;

public class PreliminaryPerformanceTest extends PerformanceTestBase {
    @Test(groups = {"performance"})
    public void eventsPageTest() throws Exception {
        startHarRecording();
        Header.goToEventsPage();
        finishHarRecording();
    }

    @Test(groups = {"performance"})
    public void newsPageTest() throws Exception {
        startHarRecording();
        Header.goToNewsPage();
        finishHarRecording();
    }

    @Test(groups = {"performance"})
    public void ourEventsPageTest() throws Exception {
        startHarRecording();
        Header.goToOurEventsPage();
        finishHarRecording();
    }
}
