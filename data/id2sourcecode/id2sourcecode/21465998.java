    @Test
    public void testClickToCallNoConvergedSession() throws Exception {
        SipCall sipCallA = ua.createSipCall();
        SipCall sipCallB = ub.createSipCall();
        sipCallA.listenForIncomingCall();
        sipCallB.listenForIncomingCall();
        log.info("Trying to reach url : " + CLICK2DIAL_URL + CLICK2DIAL_PARAMS);
        URL url = new URL(CLICK2DIAL_URL + CLICK2DIAL_PARAMS);
        InputStream in = url.openConnection().getInputStream();
        byte[] buffer = new byte[10000];
        int len = in.read(buffer);
        String httpResponse = "";
        for (int q = 0; q < len; q++) httpResponse += (char) buffer[q];
        log.info("Received the following HTTP response: " + httpResponse);
        assertTrue(sipCallA.waitForIncomingCall(timeout));
        assertTrue(sipCallA.sendIncomingCallResponse(Response.RINGING, "Ringing", 0));
        assertTrue(sipCallA.sendIncomingCallResponse(Response.OK, "OK", 0));
        assertTrue(sipCallB.waitForIncomingCall(timeout));
        assertTrue(sipCallB.sendIncomingCallResponse(Response.RINGING, "Ringing", 0));
        assertTrue(sipCallB.sendIncomingCallResponse(Response.OK, "OK", 0));
        assertTrue(sipCallB.waitForAck(timeout));
        assertTrue(sipCallA.waitForAck(timeout));
        assertTrue(sipCallA.disconnect());
        assertTrue(sipCallB.waitForDisconnect(timeout));
        assertTrue(sipCallB.respondToDisconnect());
    }
