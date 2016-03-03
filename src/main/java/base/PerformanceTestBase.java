package base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import helpers.PerformanceTiming;
import helpers.TrafficListener;
import helpers.Waiter;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.util.ThreadUtils;
import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.open;

public class PerformanceTestBase {
    private static final String BASE_URL = "https://comaqa.by/";
    private static final Date CURRENT_DATE = new Date();
    private static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final DateFormat FOLDER_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
    private static final String CURRENT_FOLDER_DATE_STRING = FOLDER_DATE_FORMAT.format(CURRENT_DATE);
    private static final String CURRENT_DATE_STRING = SIMPLE_DATE_FORMAT.format(CURRENT_DATE);
    private static final String UPLOAD_URL = "http://192.168.0.205:5000/results/upload";
    private static final String INFOTECS_USER_VARIABLE = "INFOTECS_HOME";
    private String BASE_REPORT_DIR = "./build/reports/performance/";
    private String CSV_REPORT_DIR = System.getProperty("reportsFolder", "./build/reports/performance/");

    private ProxyServer proxy;
    private String testCaseName;
    private String browser;
    private PerformanceTiming timing;
    private Har har;
    private TrafficListener trafficListener;
    private long startTimeForIe;
    private long endTimeForIe;
    protected boolean useCache = false;


    @BeforeMethod (groups = {"performance"}/*, timeOut = 300000*/)
    protected void setup (Method method) throws Exception {
        testCaseName = method.getName();
        System.out.println("Starting test: " + testCaseName);

        String cache = System.getProperty("cache", "true");
        if (cache.toLowerCase() == "true") useCache = true;
        else useCache = false;


        try {
            String home = System.getenv().get(INFOTECS_USER_VARIABLE);
            if (home != null && Files.exists(Paths.get(home))) {
                BASE_REPORT_DIR = Paths.get(home).resolve(BASE_REPORT_DIR.replace("./", "")).toString();
            }

            proxy = new ProxyServer(4444);

            proxy.start();
            trafficListener = new TrafficListener();
            proxy.addRequestInterceptor(trafficListener);
            proxy.setRequestTimeout(60000);

            Configuration.browser = System.getProperty("browser", "chrome");
            WebDriverRunner.setProxy(proxy.seleniumProxy());

            Configuration.timeout = 60000;
            Configuration.baseUrl = System.getProperty("baseUrl", BASE_URL);

            browser = StringUtils.substringBefore(WebDriverRunner.getWebDriver().toString(),"Driver");

            //Starting application
            open(Configuration.baseUrl);

        }
        catch (Exception ex) {
            //do nothing
            String exceptionText = ex.toString();
        }
    }

    @AfterMethod(groups = {"performance"})
    protected void teardown (ITestResult result) throws Exception {
        try {
            ITestNGMethod method = result.getMethod();
            if (result.isSuccess()) {
                method.setTimeOut(timing.getFullLoadTime());

                method.setId(testCaseName);
                String methodName = method.getMethodName();
                String name = methodName + "_" + CURRENT_DATE_STRING;
                saveHar(name);
            } else {
                method.setId(testCaseName);
                method.setTimeOut(0);
            }
        }
        catch (Exception ex) {
            //do nothing
        }
        finally {
            proxy.stop();
        }
    }


    //This method is called from each test method to start recording statistics
    //the "testName" parameter will be the name of the test in HAR-Storage
    protected void startHarRecording() throws Exception {
        waitForNetworkTrafficToStop(5000, 60000);

        Waiter.waitForPollingToStart(trafficListener, 1000);
        proxy.newHar(testCaseName + " (" + browser + ")");

        requestPolling();
    }

    //This method is called from each test after the actions to be recorded are finished
    protected void finishHarRecording() throws Exception {
        waitForNetworkTrafficToStop(5000, 60000);

        har = proxy.getHar();

        postToHarStorage(har);
    }

    protected void saveHar(String name) throws IOException {

        Path dirPath = Paths.get(BASE_REPORT_DIR).resolve("har/" + CURRENT_FOLDER_DATE_STRING).toAbsolutePath();

        Files.createDirectories(dirPath);

        har.writeTo(dirPath.resolve(name + ".har").toFile());
    }

    protected String postToHarStorage(Har har) throws Exception {
        URL url = new URL(UPLOAD_URL);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Automated", "true");

        try(StringWriter dataWriter = new StringWriter();
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream())){
            har.writeTo(dataWriter);
            wr.write("file=" + URLEncoder.encode(dataWriter.toString(), "utf-8"));
        }

        String response = connection.getResponseMessage();

        connection.disconnect();

        if(!response.contains("OK")){
            throw new Exception("Failed to post to Harstorage: " + response);
        }

        return response;
    }

    protected void waitForNetworkTrafficToStop(final long quietPeriodInMs, long timeoutInMs) {
        long start = System.currentTimeMillis();
        boolean result = ThreadUtils.waitFor(new ThreadUtils.WaitCondition() {
            @Override
            public boolean checkCondition(long elapsedTimeInMs) {
                Date lastCompleted = null;
                Har har = proxy.getHar();
                if(har == null || har.getLog() == null || har.getLog().getEntries().size() == 0) {
                    return true;
                }

                List<HarEntry> entries = har.getLog().getEntries();
                for(HarEntry entry : har.getLog().getEntries()) {
                    entry.getResponse();
                    if (entry.getRequest().getUrl().contains("ReverseAjax")) {
                        continue;
                    }
                    // if there is an active request, just stop looking
                    if(entry.getResponse().getStatus() < 0) {
                        return false;
                    }

                    Date end = new Date(entry.getStartedDateTime().getTime() + entry.getTime());
                    if(lastCompleted == null) {
                        lastCompleted = end;
                    } else if(end.after(lastCompleted)) {
                        lastCompleted = end;
                    }
                }

                return lastCompleted != null && System.currentTimeMillis() - lastCompleted.getTime() >= quietPeriodInMs;
            }
        }, TimeUnit.MILLISECONDS, timeoutInMs);
        long end = System.currentTimeMillis();
        long time = (end - start);

        if (!result) {
            throw new RuntimeException("Timed out after " + timeoutInMs + " ms while waiting for network traffic to stop");
        }
    }

    //This method should be called in the beginning of each test method in order to set the test case name for reports
    protected void setTestCaseName(String name) { testCaseName = name; }

    private void requestPolling() throws Exception {

        //todo: The following 3 lines is the BrowserMob Proxy version that doesn't seem to work, remove them if alternative solution works
        //proxy.addHeader("forcePolling.action", "http://192.168.5.128/forcePolling.action");
        //RequestInterceptor poll = new PollingInitiator();
        //proxy.addRequestInterceptor(poll);


        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://192.168.5.128/forcePolling.action");

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("query", "20000"));
        //params.add(new BasicNameValuePair("param-2", "Hello!"));
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        //Execute and get the response.
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            InputStream instream = entity.getContent();
            try {
                //todo: add check that response is correct
            }
            finally {
                instream.close();
            }
        }

    }

}
