package helpers;

import com.codeborne.selenide.testng.ScreenShooter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.util.Date;
import java.util.logging.Level;

import static base.TestBase.LOGGER;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class CustomScreenShooter extends ScreenShooter {
    private static int testCount;

    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        LOGGER.log(Level.CONFIG, "/n");
        LOGGER.log(Level.INFO, ++testCount + ". " + result.getMethod().getTestClass().getName() +
                '-' + result.getMethod().getMethodName() + " started at " + new Date());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        super.onTestFailure(result);
        failureScreenshot();
        LOGGER.log(Level.SEVERE, result.getMethod().getTestClass().getName() + '-' + result.getMethod().getMethodName() +
                " failed at " + new Date());
        LOGGER.log(Level.SEVERE, "Execution time: " + (result.getEndMillis() - result.getStartMillis()) / 1000 + " seconds");
        result.getThrowable().printStackTrace();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        super.onTestSuccess(result);
        LOGGER.log(Level.INFO, result.getMethod().getTestClass().getName() + '-' + result.getMethod().getMethodName() +
                " passed at " + new Date());
        LOGGER.log(Level.INFO, "Execution time: " + (result.getEndMillis() - result.getStartMillis()) / 1000 + " seconds");
    }

    @Attachment
    private static byte[] failureScreenshot() {
        return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }
}