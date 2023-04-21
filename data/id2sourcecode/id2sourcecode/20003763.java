    public void testCallWithResult(Endpoint iSender, Endpoint iReceiver, String oId) throws Exception {
        iSender.writeRequest(oId, TypeDescription.getTypeDescription(TestXInterface.class), "methodWithResult", new ThreadId(new byte[] { 0, 1 }), new Object[0]);
        iReceiver.readMessage();
        iReceiver.writeReply(false, new ThreadId(new byte[] { 0, 1 }), "resultString");
        Message iMessage = iSender.readMessage();
        Object result = iMessage.getResult();
        assure("", "resultString".equals(result));
    }
