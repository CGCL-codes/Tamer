    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "put", args = { URI.class, URLConnection.class })
    @KnownFailure("the call to put is made with a wrong uri." + " The RI calls with http://localhost:<port>/test1," + " but android only calls with http://localhost:<port>")
    public void test_put() throws Exception {
        TestResponseCache cache = new TestResponseCache("http://localhost/not_cached", false);
        ResponseCache.setDefault(cache);
        int port = Support_PortManager.getNextPort();
        Support_TestWebServer s = new Support_TestWebServer();
        try {
            s.initServer(port, 10000, false);
            Thread.currentThread().sleep(2500);
            URL url = new URL("http://localhost:" + port + "/test1");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setUseCaches(true);
            httpCon.connect();
            Thread.currentThread().sleep(2500);
            assertEquals(url.toURI(), cache.getWasCalled);
            httpCon.getContent();
            assertEquals(url.toURI(), cache.putWasCalled);
            InputStream is = httpCon.getInputStream();
            byte[] array = new byte[Support_TestWebData.test1.length];
            is.read(array);
            assertTrue(Arrays.equals(Support_TestWebData.tests[0], array));
            is.close();
            httpCon.disconnect();
        } finally {
            s.close();
        }
    }
