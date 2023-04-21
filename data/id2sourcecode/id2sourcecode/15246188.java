    public static HttpURLConnection openConnection(String url, String method, String contentType, String ssic) throws IOException {
        URL realURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) realURL.openConnection();
        conn.setRequestProperty("User-Agent", "IIC2.0/PC 4.0.0000");
        conn.setRequestProperty("Cookie", "ssic=" + ssic);
        conn.setRequestProperty("Host", realURL.getHost());
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Cache-Control", "no-cache");
        if (contentType != null) conn.setRequestProperty("Content-Type", contentType);
        conn.setRequestMethod(method);
        return conn;
    }
