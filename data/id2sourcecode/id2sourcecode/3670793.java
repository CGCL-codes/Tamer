    public void testWebResourceAvailable() throws Exception {
        assertTrue("JNDI is required", wrapper.isJNDIAvailable);
        boolean throwed = false;
        try {
            URL url = new URL(wrapper.baseServletUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            assertEquals(connection.getResponseCode(), 200);
        } catch (Exception e) {
            throwed = true;
        }
        assertTrue("Servlet is not available", !throwed);
    }
