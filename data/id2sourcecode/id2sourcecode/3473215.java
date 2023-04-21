    public void test_UseCache_HttpURLConnection_GetInputStream() throws Exception {
        ResponseCache rc = new MockNonCachedResponseCache();
        ResponseCache.setDefault(rc);
        URLConnection uc = url.openConnection();
        assertFalse(isGetCalled);
        uc.setDoOutput(true);
        uc.setUseCaches(true);
        InputStream is = uc.getInputStream();
        assertTrue(isGetCalled);
        assertTrue(isPutCalled);
        ((HttpURLConnection) uc).getResponseCode();
        is.close();
        ((HttpURLConnection) uc).disconnect();
    }
