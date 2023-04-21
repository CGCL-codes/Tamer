    public static String request(String urlStr, String params) throws IOException {
        URL url = new URL(urlStr);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        writeParameters(conn, params);
        return getResponseString(conn);
    }
