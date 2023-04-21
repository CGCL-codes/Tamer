    InputStream open() throws IOException {
        URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }
