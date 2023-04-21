    @Override
    protected InputStream inputStream() throws IOException {
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(false);
        conn.connect();
        return conn.getInputStream();
    }
