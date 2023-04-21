    private InputStream openStream(String uri) throws IOException {
        URL url = new URL(uri);
        URLConnection conn = url.openConnection();
        if (this.cookie_session != null) {
            conn.setRequestProperty("Cookie", "cookie_session=" + cookie_session);
        }
        return conn.getInputStream();
    }
