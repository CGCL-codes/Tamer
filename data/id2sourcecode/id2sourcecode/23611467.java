    public static void call(String url) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        conn.getInputStream();
    }
