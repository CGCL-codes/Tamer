    @Override
    protected OutputStream outputStream() throws IOException {
        URLConnection conn = url.openConnection();
        conn.setDoInput(false);
        conn.setDoOutput(true);
        conn.connect();
        return conn.getOutputStream();
    }
