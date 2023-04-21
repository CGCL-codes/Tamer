    public static String request(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(false);
        return getResponseString(conn);
    }
