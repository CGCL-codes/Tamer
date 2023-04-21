    private HttpResponse executeMethod(String url, String method, String referer, NameValuePair[] params, String charset) throws IOException {
        HttpURLConnection conn = createConnect(url, method, referer);
        if (params != null) {
            for (int i = 0, n = params.length; i < n; i++) {
                if (i != 0) {
                    conn.getOutputStream().write('&');
                }
                byte[] bytes = params[i].encode(charset).getBytes();
                conn.getOutputStream().write(bytes);
            }
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
        }
        conn.connect();
        HttpResponse resp = new HttpResponse(conn, charset);
        return readContent(resp, charset);
    }
