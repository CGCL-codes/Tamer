    protected String readURL2String(java.net.URL url, String encoding) throws IOException {
        InputStream is = url.openStream();
        String result = IOUtils.toString(is, encoding);
        is.close();
        return result;
    }
