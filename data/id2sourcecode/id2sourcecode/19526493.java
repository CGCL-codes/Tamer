    protected InputStream getURLInputStream() throws Exception {
        URL url = new URL(filePath);
        InputStream fin = url.openStream();
        BufferedInputStream bin = new BufferedInputStream(fin);
        return bin;
    }
