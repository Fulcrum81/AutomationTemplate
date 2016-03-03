package helpers;

import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import net.lightbody.bmp.proxy.http.RequestInterceptor;

import java.util.Date;

public class TrafficListener implements RequestInterceptor {
    private long lastRequestTime;
    private long lastPollingTime;

    @Override
    public void process(BrowserMobHttpRequest request) {
        /*if (request.getMethod().toString().contains("POST")) {
            String requestText = request.getMethod().toString();
        }*/

        if(request.getMethod().toString().contains("ReverseAjax")) {
            //"GeneralNode" full: "GeneralNodeDwrEvent" / "ReverseAjax"
            lastPollingTime = new Date().getTime();
        }
        else {
            lastRequestTime = new Date().getTime();
        }
    }

    public boolean isQuiteFor(long milliSecQuitePeriod){
        return new Date().getTime() - lastRequestTime > milliSecQuitePeriod;
    }

    public boolean isPolling(long milliSecSinceLastPolling) {
        return new Date().getTime() - lastPollingTime < milliSecSinceLastPolling;
    }
}
