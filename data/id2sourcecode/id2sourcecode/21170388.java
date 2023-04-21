    @Override
    public void setup() throws AppiaException {
        Channel[] channels = getAppiaXML().instanceGetChannelList();
        if (channels.length == 0) throw new AppiaXMLException("There are no Channels in the given configuration");
        MessageFactory msgFactory = new MessageFactoryImpl();
        for (int i = 0; i < channels.length; i++) {
            ChannelCursor cc = channels[i].getCursor();
            cc.top();
            Layer layer = cc.getLayer();
            if (!(layer instanceof TOPLayer) && !(layer instanceof SimpleTOPLayer)) throw new AppiaXMLException("Bad configuration: TOP Layer of Channel " + channels[i].getChannelID() + " is " + layer.getClass().getName() + " and should be " + TOPLayer.class.getName() + " or " + SimpleTOPLayer.class.getName());
            if (channels[i].isStarted()) throw new AppiaXMLException("Channels should not be started in the XML configuration file when used with jGCS.");
            channels[i].setMessageFactory(msgFactory);
            if (cc.getSession() == null) {
                if (layer instanceof TOPLayer) {
                    TOPSession s = (TOPSession) layer.createSession();
                    s.setMailbox(mbox);
                    cc.setSession(s);
                } else if (layer instanceof SimpleTOPLayer) {
                    SimpleTOPSession s = (SimpleTOPSession) layer.createSession();
                    s.setMailbox(mbox);
                    cc.setSession(s);
                }
            } else {
                if (layer instanceof TOPLayer) {
                    TOPSession s = (TOPSession) cc.getSession();
                    s.setMailbox(mbox);
                } else if (layer instanceof SimpleTOPLayer) {
                    SimpleTOPSession s = (SimpleTOPSession) cc.getSession();
                    s.setMailbox(mbox);
                }
            }
            channels[i].start();
            logger.debug("Starting Channel: " + channels[i].getChannelID());
        }
    }
