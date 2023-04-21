    public static InputStream getFileContent(URL url, String sessionId) throws IOException {
        url = new URL(escapeDoubleSlash(url.toString()));
        URLConnection conn = url.openConnection();
        if (sessionId != null) {
            conn.addRequestProperty("Cookie", "JSESSIONID=" + sessionId);
        }
        conn.connect();
        return conn.getInputStream();
    }
