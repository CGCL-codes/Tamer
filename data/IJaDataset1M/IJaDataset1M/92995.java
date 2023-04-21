package org.apache.shindig.common.servlet;

import org.apache.shindig.common.util.TimeSource;
import javax.servlet.http.HttpServletResponse;

/**
 * Collection of HTTP utilities
 */
public class HttpUtil {

    public static final int DEFAULT_TTL = 60 * 60 * 24 * 365;

    private static TimeSource timeSource;

    static {
        setTimeSource(new TimeSource());
    }

    public static void setTimeSource(TimeSource timeSource) {
        HttpUtil.timeSource = timeSource;
    }

    /**
   * Sets HTTP headers that instruct the browser to cache content. Implementations should take care
   * to use cache-busting techniques on the url if caching for a long period of time.
   *
   * @param response The HTTP response
   */
    public static void setCachingHeaders(HttpServletResponse response) {
        setCachingHeaders(response, DEFAULT_TTL, false);
    }

    /**
   * Sets HTTP headers that instruct the browser to cache content. Implementations should take care
   * to use cache-busting techniques on the url if caching for a long period of time.
   *
   * @param response The HTTP response
   * @param noProxy True if you don't want the response to be cacheable by proxies.
   */
    public static void setCachingHeaders(HttpServletResponse response, boolean noProxy) {
        setCachingHeaders(response, DEFAULT_TTL, noProxy);
    }

    /**
   * Sets HTTP headers that instruct the browser to cache content. Implementations should take care
   * to use cache-busting techniques on the url if caching for a long period of time.
   *
   * @param response The HTTP response
   * @param ttl The time to cache for, in seconds. If 0, then insure that
   *            this object is not cached.
   */
    public static void setCachingHeaders(HttpServletResponse response, int ttl) {
        setCachingHeaders(response, ttl, false);
    }

    public static void setNoCache(HttpServletResponse response) {
        setCachingHeaders(response, 0, false);
    }

    /**
   * Sets HTTP headers that instruct the browser to cache content. Implementations should take care
   * to use cache-busting techniques on the url if caching for a long period of time.
   *
   * @param response The HTTP response
   * @param ttl The time to cache for, in seconds. If 0, then insure that
   *            this object is not cached.
   * @param noProxy True if you don't want the response to be cacheable by proxies.
   */
    public static void setCachingHeaders(HttpServletResponse response, int ttl, boolean noProxy) {
        response.setDateHeader("Expires", timeSource.currentTimeMillis() + (1000L * ttl));
        if (ttl == 0) {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
        } else {
            if (noProxy) {
                response.setHeader("Cache-Control", "private,max-age=" + Integer.toString(ttl));
            } else {
                response.setHeader("Cache-Control", "public,max-age=" + Integer.toString(ttl));
            }
        }
    }
}
