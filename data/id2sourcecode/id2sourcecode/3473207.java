    public void test_setFixedLengthStreamingModeI_effect() throws Exception {
        String posted = "just a test";
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setFixedLengthStreamingMode(posted.length() - 1);
        assertNull(conn.getRequestProperty("Content-length"));
        conn.setRequestProperty("Content-length", String.valueOf(posted.length()));
        assertEquals(String.valueOf(posted.length()), conn.getRequestProperty("Content-length"));
        OutputStream out = conn.getOutputStream();
        try {
            out.write(posted.getBytes());
            fail("should throw IOException");
        } catch (IOException e) {
        }
        try {
            out.close();
            fail("should throw IOException");
        } catch (IOException e) {
        }
    }
