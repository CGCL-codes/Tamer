    public static InputStream getStream(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.setDefaultUseCaches(false);
        conn.setUseCaches(false);
        return conn.getInputStream();
    }
