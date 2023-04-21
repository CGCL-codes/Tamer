    public void testWebResourceNotFind() throws Exception {
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
            url = new URL(sUrl + "asasas");
            connection = (HttpURLConnection) url.openConnection();
            assertEquals(404, connection.getResponseCode());
        } catch (Exception e) {
            throwed = true;
        }
        assertTrue("Servlet is not available", !throwed);
    }
