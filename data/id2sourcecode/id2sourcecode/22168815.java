    private void testIfModified() throws IOException {
        HttpURLConnection c2 = (HttpURLConnection) url.openConnection();
        c2.setIfModifiedSince(System.currentTimeMillis() + 1000);
        c2.connect();
        int code = c2.getResponseCode();
        boolean supported = (code == 304);
        ifModifiedSinceSupported = supported;
    }
