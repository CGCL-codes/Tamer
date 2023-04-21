    protected OutputStream openUploadStream(String dir, String targetfile) throws IOException {
        URL url = makeURL(dir, targetfile);
        URLConnection urlc = url.openConnection();
        urlc.setDoOutput(true);
        OutputStream os = urlc.getOutputStream();
        return os;
    }
