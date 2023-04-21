    protected boolean resourceExists(URL url) throws IOException {
        return url.openConnection().getContentLength() != -1;
    }
