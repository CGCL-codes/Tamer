    public void testCallWithIn_Out_InOut_Paramters_and_result(Endpoint iSender, Endpoint iReceiver, String oId) throws Exception {
        Object params[] = new Object[] { "hallo", new String[1], new String[] { "inOutString" } };
        iSender.writeRequest(oId, TypeDescription.getTypeDescription(TestXInterface.class), "MethodWithIn_Out_InOut_Paramters_and_result", new ThreadId(new byte[] { 0, 1 }), params);
        Message iMessage = iReceiver.readMessage();
        Object[] t_params = iMessage.getArguments();
        assure("", "hallo".equals((String) t_params[0]));
        assure("", "inOutString".equals(((String[]) t_params[2])[0]));
        ((String[]) t_params[1])[0] = "outString";
        ((String[]) t_params[2])[0] = "inOutString_res";
        iReceiver.writeReply(false, new ThreadId(new byte[] { 0, 1 }), "resultString");
        iMessage = iSender.readMessage();
        Object result = iMessage.getResult();
        assure("", "outString".equals(((String[]) params[1])[0]));
        assure("", "inOutString_res".equals(((String[]) params[2])[0]));
        assure("", "resultString".equals(result));
    }
