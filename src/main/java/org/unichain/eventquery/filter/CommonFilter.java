package org.unichain.eventquery.filter;


import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebFilter(urlPatterns = "/*")
public class CommonFilter implements Filter {
    public  static String TOTAL_REQUEST = "TOTAL_REQUEST";
    public  static String FAIL_REQUEST = "FAIL_REQUEST";
    public  static String FAIL4XX_REQUEST = "FAIL4XX_REQUEST";
    public  static String FAIL5XX_REQUEST = "FAIL5XX_REQUEST";
    public  static String OK_REQUEST = "OK_REQUEST";
    private static int totalCount = 0;
    private static int failCount = 0;
    private static int count4xx=0;
    private static int count5xx=0;
    private static int okCount=0;
    private static int interval = 1440; // 24 hour
    private static HashMap<String, JSONObject> EndpointCount = new HashMap<String, JSONObject>();
    public String END_POINT = "END_POINT";
    public long gapMilliseconds = interval * 60 * 1000;
    private long preciousTime = 0;

    public int getTotalCount() {
        return this.totalCount;
    }

    public int getInterval() {
        return this.interval;
    }

    public int getFailCount() {
        return this.failCount;
    }

    public int getOkCount() { return this.okCount; }

    public int getCount4xx() {
        return count4xx;
    }
    
    public int getCount5xx() {
        return count5xx;
    }

    public HashMap<String, JSONObject> getEndpointMap() {
        return this.EndpointCount;
    }

    public CommonFilter getInstance() { return this; }

    @Override public void init(FilterConfig filterConfig) throws ServletException {
        preciousTime = System.currentTimeMillis();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long currentTime = System.currentTimeMillis();
        if (currentTime - preciousTime > gapMilliseconds) {   //reset every 1 minutes
            totalCount = 0;
            failCount = 0;
            count4xx=0;
            count5xx=0;
            okCount=0;
            preciousTime = currentTime;
            EndpointCount.clear();
        }

        if (request instanceof HttpServletRequest) {
            String endpoint = ((HttpServletRequest) request).getRequestURI();
            JSONObject obj = new JSONObject();
            if (EndpointCount.containsKey(endpoint)) {
                obj = EndpointCount.get(endpoint);
            } else {
                obj.put(TOTAL_REQUEST, 0);
                obj.put(FAIL_REQUEST, 0);
                obj.put(FAIL4XX_REQUEST, 0);
                obj.put(FAIL5XX_REQUEST, 0);
                obj.put(OK_REQUEST, 0);
                obj.put(END_POINT, endpoint);
            }
            obj.put(TOTAL_REQUEST, (int) obj.get(TOTAL_REQUEST) + 1);
            totalCount++;
            try {
                chain.doFilter(request, response);
                HttpServletResponse resp = (HttpServletResponse) response;
                if (resp.getStatus() != 200) {
                    failCount++;
                    count5xx=failCount-count4xx;
                    obj.put(FAIL_REQUEST, (int) obj.get(FAIL_REQUEST) + 1);
                    obj.put(FAIL5XX_REQUEST, (int) obj.get(FAIL_REQUEST)-(int) obj.get(FAIL4XX_REQUEST));
                }
                if (resp.getStatus() < 500 & resp.getStatus() > 399) {
                    count4xx++;
                    count5xx=failCount-count4xx;
                    obj.put(FAIL4XX_REQUEST, (int) obj.get(FAIL4XX_REQUEST) + 1);
                    obj.put(FAIL5XX_REQUEST, (int) obj.get(FAIL_REQUEST)-(int) obj.get(FAIL4XX_REQUEST));
                }
            } catch (Exception e) {
                failCount++;
                count5xx=failCount-count4xx;
                obj.put(FAIL_REQUEST, (int) obj.get(FAIL_REQUEST) + 1);
                obj.put(FAIL5XX_REQUEST, (int) obj.get(FAIL_REQUEST)-(int) obj.get(FAIL4XX_REQUEST));
                throw e;
            }
            obj.put(OK_REQUEST, (int) obj.get(TOTAL_REQUEST) - (int) obj.get(FAIL_REQUEST));
            okCount=totalCount-failCount;
            EndpointCount.put(endpoint, obj);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}