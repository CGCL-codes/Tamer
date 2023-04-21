    public static InputStream getUrlInputStream(java.net.URL url) throws IOException {
        java.net.URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }
