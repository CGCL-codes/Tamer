    public static Map<String, String> getCookies() throws Exception {
        URL url = new URL("http://scholar.google.com");
        URLConnection conn = url.openConnection();
        conn.connect();
        return getCookies(conn);
    }
