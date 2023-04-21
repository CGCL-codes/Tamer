    protected InputStream openDownloadStream(String targetfile) throws Exception {
        URL url = makeURL(targetfile);
        urlc = url.openConnection();
        InputStream is = urlc.getInputStream();
        return is;
    }
