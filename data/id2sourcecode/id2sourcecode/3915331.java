    public URLConnection openConnection(URL url) throws IOException {
        URLConnection conn = new DataURLConnection(url);
        conn.connect();
        return conn;
    }
