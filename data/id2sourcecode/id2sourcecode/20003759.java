    public void testCall(Endpoint iSender, Endpoint iReceiver, String oId) throws Exception {
        iSender.writeRequest(oId, TypeDescription.getTypeDescription(TestXInterface.class), "method", new ThreadId(new byte[] { 0, 1 }), new Object[0]);
        iReceiver.readMessage();
        iReceiver.writeReply(false, new ThreadId(new byte[] { 0, 1 }), null);
        iSender.readMessage();
    }
