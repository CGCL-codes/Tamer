    public void test_getURLEncodedEntry() throws IOException {
        String base = "file:resources/org/apache/harmony/luni/tests/java/net/url-test.jar";
        URL url = new URL("jar:" + base + "!/test%20folder%20for%20url%20test/test");
        if (url != null) {
            InputStream is = url.openStream();
            is.close();
        }
    }
