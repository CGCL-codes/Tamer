    private HttpURLConnection createConnect(String url, String method, String referer) throws IOException {
        waitAMoment();
        HttpURLConnection conn = null;
        Proxy proxy = getProxy();
        if (proxy != null) {
            conn = (HttpURLConnection) new URL(url).openConnection(proxy);
        } else {
            conn = (HttpURLConnection) new URL(url).openConnection();
        }
        int timeout = getTimeout();
        if (timeout > 0) {
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod(method);
        String host = getHost(url);
        if (host != null) {
            conn.addRequestProperty("Host", host);
        }
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.3) Gecko/20090824 Firefox/3.5.3 (.NET CLR 3.5.30729)");
        conn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
        conn.addRequestProperty("Accept-Encoding", "gzip,deflate");
        conn.addRequestProperty("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        conn.addRequestProperty("Keep-Alive", "115");
        if (referer != null) {
            conn.addRequestProperty("Referer", referer);
        }
        cookieHandler.get(conn);
        return conn;
    }
