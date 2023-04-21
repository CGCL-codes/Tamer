    @Test
    public void basicTest() throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://localhost:8380/any.html");
            conn = (HttpURLConnection) url.openConnection();
            int code = conn.getResponseCode();
            assertEquals(HttpURLConnection.HTTP_NOT_FOUND, code);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
