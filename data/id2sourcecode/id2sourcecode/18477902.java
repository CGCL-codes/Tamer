    private InputStream getURLInputStream(String urlPath) throws Exception {
        if (in != null) in.close();
        URL url = new URL(urlPath);
        in = url.openStream();
        BufferedInputStream bin = new BufferedInputStream(in);
        return bin;
    }
