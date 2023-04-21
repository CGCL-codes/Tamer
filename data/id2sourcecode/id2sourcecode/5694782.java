    @Suppress
    public void testHttpConnectionTimeout() throws Exception {
        int timeout = 5000;
        HttpURLConnection cn = null;
        long start = 0;
        try {
            start = System.currentTimeMillis();
            URL url = new URL("http://123.123.123.123");
            cn = (HttpURLConnection) url.openConnection();
            cn.setConnectTimeout(5000);
            cn.connect();
            fail("should have thrown an exception");
        } catch (IOException ioe) {
            long delay = System.currentTimeMillis() - start;
            if (Math.abs(timeout - delay) > 1000) {
                fail("Timeout was not accurate. it needed " + delay + " instead of " + timeout + "miliseconds");
            }
        } finally {
            if (cn != null) {
                cn.disconnect();
            }
        }
    }
