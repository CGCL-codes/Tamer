    @Test
    public void testClickToCallExpirationTime() throws Exception {
        log.info("Trying to reach url : " + CLICK2DIAL_URL + EXPIRATION_TIME_PARAMS);
        URL url = new URL(CLICK2DIAL_URL + EXPIRATION_TIME_PARAMS);
        InputStream in = url.openConnection().getInputStream();
        byte[] buffer = new byte[10000];
        int len = in.read(buffer);
        String httpResponse = "";
        for (int q = 0; q < len; q++) httpResponse += (char) buffer[q];
        log.info("Received the follwing HTTP response: " + httpResponse);
        assertFalse("0".equals(httpResponse.trim()));
    }
