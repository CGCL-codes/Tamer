    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "get", args = { URI.class, String.class, Map.class })
    public void test_get() throws Exception {
        String uri = "http://localhost/";
        URL url = new URL(uri);
        TestResponseCache cache = new TestResponseCache(uri, true);
        ResponseCache.setDefault(cache);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setUseCaches(true);
        httpCon.connect();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
        InputStream is = httpCon.getInputStream();
        byte[] array = new byte[10];
        is.read(array);
        assertEquals(url.toURI(), cache.getWasCalled);
        assertEquals("Cache test", new String(array));
        is.close();
        httpCon.disconnect();
    }
