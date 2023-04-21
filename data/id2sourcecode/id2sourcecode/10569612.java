    public STBPGameEngineProxy(ProtocolReader reader, ProtocolWriter writer) {
        pmwriter = writer;
        imh = new IncommingMessageHandler(reader);
        imh.start();
    }
