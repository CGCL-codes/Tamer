    private InputStream getInputStream(String filename) throws IOException {
        filename = codebase + filename;
        URL url = new URL(filename);
        URLConnection urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(false);
        urlConn.setUseCaches(false);
        int l = urlConn.getContentLength();
        if (l > -1) bytesToRead += l;
        return urlConn.getInputStream();
    }
