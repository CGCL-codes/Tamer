    public void test_UseCache_HttpURLConnection_NonCached() throws IOException {
        ResponseCache.setDefault(new MockNonCachedResponseCache());
        uc = (HttpURLConnection) url.openConnection();
        assertTrue(uc.getUseCaches());
        isGetCalled = false;
        isPutCalled = false;
        InputStream is = uc.getInputStream();
        assertFalse(is instanceof MockInputStream);
        assertTrue(isGetCalled);
        assertTrue(isPutCalled);
        isCacheWriteCalled = false;
        is.read();
        assertTrue(isCacheWriteCalled);
        isCacheWriteCalled = false;
        byte[] buf = new byte[1];
        is.read(buf);
        assertTrue(isCacheWriteCalled);
        isCacheWriteCalled = false;
        buf = new byte[1];
        is.read(buf, 0, 1);
        assertTrue(isCacheWriteCalled);
        isAbortCalled = false;
        is.close();
        assertTrue(isAbortCalled);
        uc.disconnect();
    }
