    protected OutputStream openUploadStream(String targetfile) throws Exception {
        URL url = makeURL(targetfile);
        urlc = url.openConnection();
        OutputStream os = urlc.getOutputStream();
        return os;
    }
