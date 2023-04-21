    protected InputStream getURLInputStream(String name) throws Exception {
        URL url = new URL(name);
        InputStream is = url.openStream();
        BufferedInputStream bin = new BufferedInputStream(is, 2 * 1024);
        return bin;
    }
