    @Test
    @Ignore
    public void testClickToCallHttpSessionLeak() throws Exception {
        final int sessionsNumber = containerManager.getSipStandardManager().getActiveSessions();
        log.info("Trying to reach url : " + RESOURCE_LEAK_URL);
        URL url = new URL(RESOURCE_LEAK_URL);
        URLConnection uc = url.openConnection();
        InputStream in = uc.getInputStream();
        byte[] buffer = new byte[10000];
        int len = in.read(buffer);
        String httpResponse = "";
        for (int q = 0; q < len; q++) httpResponse += (char) buffer[q];
        log.info("Received the follwing HTTP response: " + httpResponse);
        assertEquals(sessionsNumber, containerManager.getSipStandardManager().getActiveSessions());
    }
