    public URLConnection openConnection(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.connect();
        return conn;
    }
