package base;

import com.codeborne.selenide.Configuration;
import helpers.Waiter;
import org.testng.annotations.BeforeMethod;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.codeborne.selenide.Selenide.open;

public class TestBase {
    private static final String BASE_URL = "https://comaqa.by/";
    private static final String BROWSER = "chrome";
    private static final int TIMEOUT = 5000;

    public static final Logger LOGGER = Logger.getLogger(TestBase.class.getName());

    @BeforeMethod
    public void setup() {
        Configuration.browser = System.getProperty("browser", BROWSER);
        Configuration.baseUrl = System.getProperty("baseUrl", BASE_URL);
        Configuration.timeout = TIMEOUT;
        Configuration.startMaximized = true;

        LOGGER.setLevel(Level.CONFIG);

        open(BASE_URL);
        Waiter.waitForPageToLoad();
    }
}
