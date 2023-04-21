    public void testHtmlAnchor() throws Exception {
        count++;
        if (shouldSkip()) return;
        String uri = "onejar:index.html#anchor";
        URL url = new URL(uri);
        InputStream is = url.openStream();
        if (is == null) {
            fail("Unable to load anchored URL: " + uri);
        }
        System.out.println("testHtmlAnchor(): found URL with anchor OK: " + uri);
    }
