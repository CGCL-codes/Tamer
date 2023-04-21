    public void testSynchronization() throws IOException {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://localhost:2580/nexopen/sync/a.do");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int code = conn.getResponseCode();
            assertEquals(200, code);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
