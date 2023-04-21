    protected URLConnection getURLConnection(String url, String httpMethod) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        if (conn instanceof HttpURLConnection) {
            HttpURLConnection hConn = (HttpURLConnection) conn;
            hConn.setRequestMethod(httpMethod);
        }
        conn.setRequestProperty("Accept", "text/*");
        conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
        conn.setDoInput(true);
        conn.setDoOutput(HTTP_METHOD_POST.equalsIgnoreCase(httpMethod));
        conn.setConnectTimeout(this.connectionTimeout);
        conn.setReadTimeout(connectionTimeout);
        return conn;
    }
