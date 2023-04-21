    public void testCallWithInParameter(Endpoint iSender, Endpoint iReceiver, String oId) throws Exception {
        iSender.writeRequest(oId, TypeDescription.getTypeDescription(TestXInterface.class), "methodWithInParameter", new ThreadId(new byte[] { 0, 1 }), new Object[] { "hallo" });
        Message iMessage = iReceiver.readMessage();
        Object[] t_params = iMessage.getArguments();
        assure("", "hallo".equals((String) t_params[0]));
        iReceiver.writeReply(false, new ThreadId(new byte[] { 0, 1 }), null);
        iMessage = iSender.readMessage();
    }
