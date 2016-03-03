package helpers;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarPage;
import net.lightbody.bmp.core.har.HarPageTimings;

import java.util.Date;
import java.util.Map;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class PerformanceTiming {
    private long connectEnd;
    private long connectStart;
    private long domComplete;
    private long domContentLoadedEventEnd;
    private long domContentLoadedEventStart;
    private long domInteractive;
    private long domLoading;
    private long domainLookupEnd;
    private long domainLookupStart;
    private long fetchStart;
    private long loadEventEnd;
    private long loadEventStart;
    private long navigationStart;
    private long redirectEnd;
    private long redirectStart;
    private long requestStart;
    private long responseEnd;
    private long responseStart;
    private long secureConnectionStart;
    private long unloadEventEnd;
    private long unloadEventStart;
    private long fullLoadTime;

    public PerformanceTiming(Har har) {
        @SuppressWarnings("unchecked") Map<String, Long> timing = (Map<String, Long>) executeJavaScript("return window.performance.timing;");
        connectEnd = get(timing, "connectEnd");
        connectStart = get(timing, "connectStart");
        domComplete = get(timing, "domComplete");
        domContentLoadedEventEnd = get(timing, "domContentLoadedEventEnd");
        domContentLoadedEventStart = get(timing, "domContentLoadedEventStart");
        domInteractive = get(timing, "domInteractive");
        domLoading = get(timing, "domLoading");
        domainLookupEnd = get(timing, "domainLookupEnd");
        domainLookupStart = get(timing, "domainLookupStart");
        fetchStart = get(timing, "fetchStart");
        loadEventEnd = get(timing, "loadEventEnd");
        loadEventStart = get(timing, "loadEventStart");
        navigationStart = get(timing, "navigationStart");
        redirectEnd = get(timing, "redirectEnd");
        redirectStart = get(timing, "redirectStart");
        requestStart = get(timing, "requestStart");
        responseEnd = get(timing, "responseEnd");
        responseStart = get(timing, "responseStart");
        secureConnectionStart = get(timing, "secureConnectionStart");
        unloadEventEnd = get(timing, "unloadEventEnd");
        unloadEventStart = get(timing, "unloadEventStart");

        // also augment the HAR with some stuff
        String href = (String) executeJavaScript("return window.location.href;");
        if (href.contains("#")) {
            href = href.substring(0, href.indexOf("#"));
        }

        HarPage page = null;
        HarPage lastPage = har.getLog().getPages().get(har.getLog().getPages().size() - 1);
        fullLoadTime = 0;
        for (HarEntry entry : har.getLog().getEntries()) {

            if (entry.getRequest().getUrl().contains("ReverseAjax")) {
                har.getLog().getEntries().remove(entry);
                continue;
            }

            if (href.equals(entry.getRequest().getUrl())) {
                for (HarPage harPage : har.getLog().getPages()) {
                    if (harPage.getId().equals(entry.getPageref())) {
                        page = harPage;
                        break;
                    }
                }
            }

            long time = entry.getStartedDateTime().getTime() + entry.getTime() - lastPage.getStartedDateTime().getTime();
            if(time > fullLoadTime){
                fullLoadTime = time;
            }
        }

        if (page != null) {
            page.setTitle((String) executeJavaScript("return window.document.title;"));
            page.setStartedDateTime(new Date(navigationStart));
            HarPageTimings pageTimings = new HarPageTimings();
            pageTimings.setOnContentLoad(domContentLoadedEventEnd - navigationStart);
            pageTimings.setOnLoad(loadEventEnd - navigationStart);
            page.setPageTimings(pageTimings);
        }
    }

    private long get(Map<String, Long> timings, String key) {
        if (timings.containsKey(key)) {
            return timings.get(key);
        } else {
            return 0;
        }
    }

    public long getFullLoadTime() {
        return fullLoadTime;
    }

    public long getConnectEnd() {
        return connectEnd;
    }

    public long getConnectStart() {
        return connectStart;
    }

    public long getDomComplete() {
        return domComplete;
    }

    public long getDomContentLoadedEventEnd() {
        return domContentLoadedEventEnd;
    }

    public long getDomContentLoadedEventStart() {
        return domContentLoadedEventStart;
    }

    public long getDomInteractive() {
        return domInteractive;
    }

    public long getDomLoading() {
        return domLoading;
    }

    public long getDomainLookupEnd() {
        return domainLookupEnd;
    }

    public long getDomainLookupStart() {
        return domainLookupStart;
    }

    public long getFetchStart() {
        return fetchStart;
    }

    public long getLoadEventEnd() {
        return loadEventEnd;
    }

    public long getLoadEventStart() {
        return loadEventStart;
    }

    public long getNavigationStart() {
        return navigationStart;
    }

    public long getRedirectEnd() {
        return redirectEnd;
    }

    public long getRedirectStart() {
        return redirectStart;
    }

    public long getRequestStart() {
        return requestStart;
    }

    public long getResponseEnd() {
        return responseEnd;
    }

    public long getResponseStart() {
        return responseStart;
    }

    public long getSecureConnectionStart() {
        return secureConnectionStart;
    }

    public long getUnloadEventEnd() {
        return unloadEventEnd;
    }

    public long getUnloadEventStart() {
        return unloadEventStart;
    }
}
