    public void test_getRequestProperty_LString_Exception() throws IOException {
        URL url = new URL("http", "test", 80, "index.html", new NewHandler());
        URLConnection urlCon = url.openConnection();
        urlCon.setRequestProperty("test", "testProperty");
        assertEquals("testProperty", urlCon.getRequestProperty("test"));
        urlCon.connect();
        try {
            urlCon.getRequestProperty("test");
            fail("should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }
