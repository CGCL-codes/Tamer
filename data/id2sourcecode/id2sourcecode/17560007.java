    public void testElapsedTimeAndSipApplicationSessionDeadlock() throws Exception {
        deployApplication(ConcurrencyControlMode.SipApplicationSession);
        String fromName = "Thread";
        String fromSipAddress = "sip-servlets.com";
        SipURI fromAddress = senderProtocolObjects.addressFactory.createSipURI(fromName, fromSipAddress);
        fromAddress.setParameter("mode", ConcurrencyControlMode.SipApplicationSession.toString());
        String toUser = "receiver";
        String toSipAddress = "sip-servlets.com";
        SipURI toAddress = senderProtocolObjects.addressFactory.createSipURI(toUser, toSipAddress);
        String CLICK2DIAL_PARAMS = "?asyncWorkMode=Thread&asyncWorkSasId=test";
        logger.info("Trying to reach url : " + CLICK2DIAL_URL + CLICK2DIAL_PARAMS);
        URL url = new URL(CLICK2DIAL_URL + CLICK2DIAL_PARAMS);
        InputStream in = url.openConnection().getInputStream();
        byte[] buffer = new byte[10000];
        int len = in.read(buffer);
        String httpResponse = "";
        for (int q = 0; q < len; q++) httpResponse += (char) buffer[q];
        logger.info("Received the follwing HTTP response: " + httpResponse);
        Thread.sleep(10000);
        Iterator<String> allMessagesIterator = sender.getAllMessagesContent().iterator();
        while (allMessagesIterator.hasNext()) {
            String message = (String) allMessagesIterator.next();
            logger.info(message);
        }
        assertTrue(sender.getAllMessagesContent().contains("OK"));
    }
