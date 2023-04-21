    public void testSipAppSessionTerminationHttpSessionStillAlive() throws Exception {
        logger.info("Trying to reach url : " + CLICK2DIAL_URL + CLICK2DIAL_PARAMS);
        URL url = new URL(CLICK2DIAL_URL + CLICK2DIAL_PARAMS);
        InputStream in = url.openConnection().getInputStream();
        byte[] buffer = new byte[10000];
        int len = in.read(buffer);
        String httpResponse = "";
        for (int q = 0; q < len; q++) httpResponse += (char) buffer[q];
        logger.info("Received the follwing HTTP response: " + httpResponse);
        Thread.sleep(TIMEOUT);
        assertTrue(receiver.getOkToByeReceived());
        Thread.sleep(TIMEOUT);
        Iterator<String> allMessagesIterator = receiver.getAllMessagesContent().iterator();
        logger.info("all messages received : ");
        while (allMessagesIterator.hasNext()) {
            String message = (String) allMessagesIterator.next();
            logger.info(message);
        }
        assertFalse(receiver.getAllMessagesContent().contains("sipAppSessionDestroyed"));
    }
