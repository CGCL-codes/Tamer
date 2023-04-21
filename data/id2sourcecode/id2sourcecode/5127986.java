    public BufferedInputStream getBufferedInputStream() throws Exception {
        if (exists()) {
            return new BufferedInputStream(new FileInputStream(this));
        }
        String urlname = getPath().replace(File.separatorChar, '/');
        if (urlname.startsWith("./")) {
            urlname = urlname.substring(2);
        }
        InputStream in = ClassLoader.getSystemResourceAsStream(urlname);
        if (in == null) {
            java.net.URL url = getClass().getClassLoader().getResource(urlname);
            if (url != null) {
                in = url.openStream();
            }
        }
        if (in == null) {
            throw new IOException("Can't access resource: " + urlname);
        }
        return new BufferedInputStream(in);
    }
