    public InputStream open() {
        try {
            return url.openStream();
        } catch (IOException ex) {
            Assert.fail(ex);
        }
        return null;
    }
