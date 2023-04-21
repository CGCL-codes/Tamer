    public void fromURL(URL url, String charset) throws Exception {
        _charset = charset;
        loadStream(url.openStream());
    }
