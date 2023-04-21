    public void testClickToCallHttpSessionLeak() throws Exception {
        final int sessionsNumber = manager.getActiveSessions();
        logger.info("Trying to reach url : " + RESOURCE_LEAK_URL);
        URL url = new URL(RESOURCE_LEAK_URL);
        InputStream in = url.openConnection().getInputStream();
        byte[] buffer = new byte[10000];
        int len = in.read(buffer);
        String httpResponse = "";
        for (int q = 0; q < len; q++) httpResponse += (char) buffer[q];
        logger.info("Received the follwing HTTP response: " + httpResponse);
        assertEquals(sessionsNumber, manager.getActiveSessions());
    }
