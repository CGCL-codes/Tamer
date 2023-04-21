    public XMPPPacketReader(DataInputStream reader, XMPPPacketWriter packetwriter) {
        try {
            this.reader = reader;
            this.packetwriter = packetwriter;
            inputfactory = XMLInputFactory.newInstance();
            streamreader = inputfactory.createXMLStreamReader(this.reader);
            delegates = new HashMap();
            handler = new EventHandler(delegates);
            handler.registerParser("stream", new StreamHandler(packetwriter));
            handler.registerParser("auth", new AuthHandler(packetwriter, packetwriter.getClient()));
            handler.registerParser("iq", new IQHandler(packetwriter, packetwriter.getClient()));
            handler.registerParser("presence", new PresenceHandler(packetwriter, packetwriter.getClient()));
            handler.registerParser("message", new MessageHandler(packetwriter, packetwriter.getClient()));
        } catch (XMLStreamException ex) {
            Logger.getLogger(XMPPPacketReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
