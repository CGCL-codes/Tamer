    public void testContentType() throws Exception {
        count++;
        if (shouldSkip()) return;
        String uri = "onejar:index.html";
        URL url = new URL(uri);
        URLConnection connection = url.openConnection();
        String contenttype = connection.getContentType();
        if (!contenttype.equals("text/html")) {
            fail("Unexpected content type for " + uri + ": " + contenttype);
        }
    }
