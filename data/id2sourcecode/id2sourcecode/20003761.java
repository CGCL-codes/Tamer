    public void testCallWithOutParameter(Endpoint iSender, Endpoint iReceiver, String oId) throws Exception {
        Object params[] = new Object[] { new String[1] };
        iSender.writeRequest(oId, TypeDescription.getTypeDescription(TestXInterface.class), "methodWithOutParameter", new ThreadId(new byte[] { 0, 1 }), params);
        Message iMessage = iReceiver.readMessage();
        Object[] t_params = iMessage.getArguments();
        ((String[]) t_params[0])[0] = "testString";
        iReceiver.writeReply(false, new ThreadId(new byte[] { 0, 1 }), null);
        iSender.readMessage();
        assure("", "testString".equals(((String[]) params[0])[0]));
    }
