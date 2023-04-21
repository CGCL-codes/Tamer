    public void testCallWhichReturnsAny(Endpoint iSender, Endpoint iReceiver, String oId) throws Exception {
        iSender.writeRequest(oId, TypeDescription.getTypeDescription(TestXInterface.class), "returnAny", new ThreadId(new byte[] { 0, 1 }), null);
        iReceiver.readMessage();
        iReceiver.writeReply(false, new ThreadId(new byte[] { 0, 1 }), Any.VOID);
        Message iMessage = iSender.readMessage();
        Object result = iMessage.getResult();
        assure("", result instanceof Any && (TypeDescription.getTypeDescription(((Any) result).getType()).getZClass() == void.class));
        iSender.writeRequest(oId, TypeDescription.getTypeDescription(TestXInterface.class), "returnAny", new ThreadId(new byte[] { 0, 1 }), null);
        iReceiver.readMessage();
        iReceiver.writeReply(false, new ThreadId(new byte[] { 0, 1 }), new Any(XInterface.class, null));
        iMessage = iSender.readMessage();
        result = iMessage.getResult();
        assure("", result == null);
        iSender.writeRequest(oId, TypeDescription.getTypeDescription(TestXInterface.class), "returnAny", new ThreadId(new byte[] { 0, 1 }), null);
        iReceiver.readMessage();
        iReceiver.writeReply(false, new ThreadId(new byte[] { 0, 1 }), new Integer(501));
        iMessage = iSender.readMessage();
        result = iMessage.getResult();
        assure("", result.equals(new Integer(501)));
    }
