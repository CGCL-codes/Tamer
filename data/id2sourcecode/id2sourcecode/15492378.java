    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "get", args = { java.net.URI.class, java.util.Map.class }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "put", args = { java.net.URI.class, java.util.Map.class }) })
    @KnownFailure("Cache is not used")
    public void test_get_put() {
        MockCookieHandler mch = new MockCookieHandler();
        CookieHandler defaultHandler = CookieHandler.getDefault();
        CookieHandler.setDefault(mch);
        class TestThread extends Thread {

            public void run() {
                try {
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();
                    Object obj = conn.getContent();
                    url = new URL(link);
                    conn = url.openConnection();
                    obj = conn.getContent();
                } catch (MalformedURLException e) {
                    fail("MalformedURLException was thrown: " + e.toString());
                } catch (IOException e) {
                    fail("IOException was thrown.");
                }
            }
        }
        ;
        try {
            TestThread thread = new TestThread();
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                fail("InterruptedException was thrown.");
            }
            assertTrue(isGetCalled);
            assertTrue(isPutCalled);
        } finally {
            CookieHandler.setDefault(defaultHandler);
        }
    }
