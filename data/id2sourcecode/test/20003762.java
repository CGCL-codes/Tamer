    public void testCallWithInOutParameter(Endpoint iSender, Endpoint iReceiver, String oId) throws Exception {
        Object params[] = new Object[] { new String[] { "inString" } };
        iSender.writeRequest(oId, TypeDescription.getTypeDescription(TestXInterface.class), "methodWithInOutParameter", new ThreadId(new byte[] { 0, 1 }), params);
        Message iMessage = iReceiver.readMessage();
        Object[] t_params = iMessage.getArguments();
        assure("", "inString".equals(((String[]) t_params[0])[0]));
        ((String[]) t_params[0])[0] = "outString";
        iReceiver.writeReply(false, new ThreadId(new byte[] { 0, 1 }), null);
        iSender.readMessage();
        assure("", "outString".equals(((String[]) params[0])[0]));
    }
