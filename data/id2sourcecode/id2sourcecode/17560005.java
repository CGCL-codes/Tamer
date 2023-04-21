    public void testElapsedTimeAndSessionOverlapping() throws Exception {
        deployApplication(ConcurrencyControlMode.SipSession);
        String fromName = "asyncWork";
        String fromSipAddress = "sip-servlets.com";
        SipURI fromAddress = senderProtocolObjects.addressFactory.createSipURI(fromName, fromSipAddress);
        fromAddress.setParameter("mode", ConcurrencyControlMode.SipSession.toString());
        String toUser = "receiver";
        String toSipAddress = "sip-servlets.com";
        SipURI toAddress = senderProtocolObjects.addressFactory.createSipURI(toUser, toSipAddress);
        sender.setSendBye(false);
        sender.sendSipRequest("INVITE", fromAddress, toAddress, null, null, false);
        Thread.sleep(3000);
        String sasId = new String(sender.getFinalResponse().getRawContent());
        String CLICK2DIAL_PARAMS = "?asyncWorkMode=" + ConcurrencyControlMode.SipSession + "&asyncWorkSasId=" + sasId;
        sender.sendInDialogSipRequest("OPTIONS", "1", "text", "plain", null, null);
        Thread.sleep(100);
        sender.sendInDialogSipRequest("OPTIONS", "2", "text", "plain", null, null);
        Thread.sleep(100);
        logger.info("Trying to reach url : " + CLICK2DIAL_URL + CLICK2DIAL_PARAMS);
        URL url = new URL(CLICK2DIAL_URL + CLICK2DIAL_PARAMS);
        InputStream in = url.openConnection().getInputStream();
        byte[] buffer = new byte[10000];
        int len = in.read(buffer);
        String httpResponse = "";
        for (int q = 0; q < len; q++) httpResponse += (char) buffer[q];
        logger.info("Received the follwing HTTP response: " + httpResponse);
        sender.sendInDialogSipRequest("OPTIONS", "3", "text", "plain", null, null);
        Thread.sleep(100);
        sender.sendBye();
        Thread.sleep(20000);
        assertTrue(!sender.isServerErrorReceived());
        assertTrue(sender.isAckSent());
        assertTrue(sender.getOkToByeReceived());
        Iterator<String> allMessagesIterator = sender.getAllMessagesContent().iterator();
        while (allMessagesIterator.hasNext()) {
            String message = (String) allMessagesIterator.next();
            logger.info(message);
        }
        assertTrue(sender.getAllMessagesContent().contains("OK"));
    }
