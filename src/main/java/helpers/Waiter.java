package helpers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class Waiter {
    private static final int DEFAULT_TIME_OUT = 10;

    public static void waitForPageTitle(String title) {
        getWaiter().until(ExpectedConditions.titleIs(title));
    }

    public static void waitForJquery() {
        getWaiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                return (Boolean) js.executeScript("return jQuery.active == 0");
            }
        });
    }

    public static void waitForPageToLoad() {
        Wait().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                boolean complete = ((String) js.executeScript("return document.readyState")).equalsIgnoreCase("complete");
                if (!complete) {
                    sleep(1000);
                }
                return complete;
            }
        });
    }

//    public static void waitForProcessing(){
//        try {
//            getWaiter().until(new ExpectedCondition<Boolean>() {
//                @Override
//                public Boolean apply(WebDriver webDriver) {
//                    return $(get("processingDialog")).isDisplayed();
//                }
//            });
//            $(get("processingDialog")).waitUntil(disappear, 120000);
//        }
//        catch (TimeoutException ex){
//            //do nothing
//        }
//    }

    public static void waitForPollingToStart(final TrafficListener poller, final long millisec) {
        getWaiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return poller.isPolling(millisec);
            }
        });
    }

    public static void waitForQuietPeriodOf(final TrafficListener poller, final long millisec) {
        getWaiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return poller.isQuiteFor(millisec);
            }
        });
    }

    private static WebDriverWait getWaiter() {
        return new WebDriverWait(getWebDriver(), DEFAULT_TIME_OUT);
    }
}
