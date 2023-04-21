    public void testWebResourceDirFind() throws Exception {
        assertTrue("JNDI is required", wrapper.isJNDIAvailable);
        boolean throwed = false;
        ResourceDao dir = wrapper.generateResourceDir();
        wrapper.create("/", dir);
        ResourceDao file = wrapper.generateResourceDao();
        wrapper.create("/" + dir.getName(), file);
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
        } catch (Exception e) {
            throwed = true;
        }
        assertTrue("Servlet is not available", !throwed);
    }
