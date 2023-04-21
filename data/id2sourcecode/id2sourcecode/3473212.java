    public void test_UseCache_HttpURLConnection_Connect_GetInputStream() throws Exception {
        ResponseCache rc = new MockNonCachedResponseCache();
        ResponseCache.setDefault(rc);
        uc = (HttpURLConnection) url.openConnection();
        assertFalse(isGetCalled);
        uc.setUseCaches(true);
        uc.setDoOutput(true);
        uc.connect();
        assertTrue(isGetCalled);
        assertFalse(isPutCalled);
        InputStream is = uc.getInputStream();
        assertTrue(isPutCalled);
        is.close();
        ((HttpURLConnection) uc).disconnect();
    }
