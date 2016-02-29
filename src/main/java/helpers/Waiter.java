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
//        if ($(Header.MAIN_MENU_CONTAINER).exists()) {
//            Actions.setMainMenuInvisible();
//        }
//        sleep(1000); //TODO: ?
    }

    private static WebDriverWait getWaiter() {
        return new WebDriverWait(getWebDriver(), DEFAULT_TIME_OUT);
    }
}
