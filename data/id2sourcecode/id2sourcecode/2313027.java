    public InputStream getStream() {
        try {
            return url.openStream();
        } catch (IOException e) {
            Assert.fail("Failed to open stream: " + url, e);
            return null;
        }
    }
