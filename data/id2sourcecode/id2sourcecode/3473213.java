    public void test_UseCache_HttpURLConnection_Connect_GetOutputStream() throws Exception {
        ResponseCache rc = new MockNonCachedResponseCache();
        ResponseCache.setDefault(rc);
        uc.setUseCaches(true);
        URLConnection uc = url.openConnection();
        uc.setDoOutput(true);
        assertFalse(isGetCalled);
        uc.connect();
        assertTrue(isGetCalled);
        assertFalse(isPutCalled);
        OutputStream os = uc.getOutputStream();
        assertFalse(isPutCalled);
        os.close();
        ((HttpURLConnection) uc).disconnect();
    }
