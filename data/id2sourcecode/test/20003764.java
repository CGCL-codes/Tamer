    public void testCallWhichRaisesException(Endpoint iSender, Endpoint iReceiver, String oId) throws Exception {
        iSender.writeRequest(oId, TypeDescription.getTypeDescription(TestXInterface.class), "method", new ThreadId(new byte[] { 0, 1 }), new Object[0]);
        iReceiver.readMessage();
        iReceiver.writeReply(true, new ThreadId(new byte[] { 0, 1 }), new com.sun.star.uno.RuntimeException("test the exception"));
        Message iMessage = iSender.readMessage();
        Object result = iMessage.getResult();
        assure("", result instanceof com.sun.star.uno.RuntimeException);
    }
