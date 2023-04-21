    public void test_setChunkedStreamingModeI_effect() throws Exception {
        String posted = "just a test";
        int chunkSize = posted.length() / 2;
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setChunkedStreamingMode(chunkSize);
        assertNull(conn.getRequestProperty("Transfer-Encoding"));
        conn.setRequestProperty("Content-length", String.valueOf(posted.length() - 1));
        assertEquals(conn.getRequestProperty("Content-length"), String.valueOf(posted.length() - 1));
        OutputStream out = conn.getOutputStream();
        out.write(posted.getBytes());
        out.close();
        assertTrue(conn.getResponseCode() > 0);
    }
