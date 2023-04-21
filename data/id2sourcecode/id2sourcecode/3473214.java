    public void test_UseCache_HttpURLConnection_GetOutputStream() throws Exception {
        ResponseCache rc = new MockNonCachedResponseCache();
        ResponseCache.setDefault(rc);
        uc = (HttpURLConnection) url.openConnection();
        assertFalse(isGetCalled);
        uc.setDoOutput(true);
        uc.setUseCaches(true);
        OutputStream os = uc.getOutputStream();
        assertTrue(isGetCalled);
        assertFalse(isPutCalled);
        os.write(1);
        os.flush();
        os.close();
        ((HttpURLConnection) uc).getResponseCode();
        assertTrue(isGetCalled);
        assertTrue(isPutCalled);
        isGetCalled = false;
        isPutCalled = false;
        InputStream is = uc.getInputStream();
        assertFalse(isGetCalled);
        assertFalse(isPutCalled);
        is.close();
        ((HttpURLConnection) uc).disconnect();
    }
