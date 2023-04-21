    private Reader getUrlContents(String urlStr) throws Exception {
        URL urlObj = uLis.buildUrl(urlStr);
        URLConnection uConn = urlObj.openConnection();
        InputStream inStr = new BufferedInputStream(uConn.getInputStream());
        return new InputStreamReader(inStr);
    }
