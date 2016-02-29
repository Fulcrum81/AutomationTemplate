package base;

import com.codeborne.selenide.Configuration;
import helpers.Waiter;
import org.testng.annotations.BeforeMethod;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.codeborne.selenide.Selenide.open;

public class TestBase {
    private static final String BASE_URL = "http://10.10.101.81";

    public static final Logger LOGGER = Logger.getLogger(TestBase.class.getName());

    @BeforeMethod
    public void setup() {
        Configuration.baseUrl = System.getProperty("baseUrl", BASE_URL);
        Configuration.timeout = 20000;
        Configuration.startMaximized = true;

        LOGGER.setLevel(Level.CONFIG);

        open(BASE_URL);
        Waiter.waitForPageToLoad();
    }
}
