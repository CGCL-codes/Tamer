    private InputStream performRequest() throws UnsupportedEncodingException, MalformedURLException, IOException {
        OutputStreamWriter writer = null;
        encodeParam();
        URL url;
        if (method == Method.POST) {
            url = new URL(this.url);
        } else {
            url = new URL(this.url + encodedParam);
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
        conn.setInstanceFollowRedirects(followRedirects);
        conn.setDoOutput(true);
        setupCookie(conn);
        try {
            if (method == Method.POST) {
                writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(encodedParam);
                writer.flush();
            }
            setCookies(conn.getHeaderField("Set-Cookie"));
            responseCode = conn.getResponseCode();
            headerFields = conn.getHeaderFields();
            return conn.getInputStream();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
