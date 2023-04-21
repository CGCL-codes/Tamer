    public void test_getRequestProperties_Exception() throws IOException {
        URL url = new URL("http", "test", 80, "index.html", new NewHandler());
        URLConnection urlCon = url.openConnection();
        urlCon.connect();
        try {
            urlCon.getRequestProperties();
            fail("should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }
