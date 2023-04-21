    public void testWebResourceGetResource() throws Exception {
        assertTrue("JNDI is required", wrapper.isJNDIAvailable);
        boolean throwed = false;
        ResourceDao dao = wrapper.generateResourceDao();
        wrapper.create("/", dao);
        String path = (String) wrapper.pathes.lastElement();
        try {
            URL url = new URL(wrapper.baseServletUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            assertEquals(200, connection.getResponseCode());
            String sUrl = wrapper.baseServletUrl + path;
            sUrl = sUrl.replaceAll("/" + path, path);
            url = new URL(sUrl);
            connection = (HttpURLConnection) url.openConnection();
            assertEquals(200, connection.getResponseCode());
            String contentType = connection.getContentType();
            int contentLength = connection.getContentLength();
            assertEquals("Content length should be equals", (dao.getContentLength() != null) ? dao.getContentLength().intValue() : 0, contentLength);
            assertEquals("Content type should be equals", dao.getContentType(), contentType);
        } catch (Exception e) {
            throwed = true;
        }
        assertTrue("Servlet is not available", !throwed);
    }
