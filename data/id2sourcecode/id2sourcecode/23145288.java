    protected String readURLContent(URL url) throws Exception {
        URLConnection urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setUseCaches(false);
        return readResponse(urlConn);
    }
