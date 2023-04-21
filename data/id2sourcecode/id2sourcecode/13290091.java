    public InputStream getFileStream(String resname) throws Exception {
        URL url = getURL(resname);
        return url.openStream();
    }
