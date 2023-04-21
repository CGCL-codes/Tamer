    public boolean onEvent(SKJMessage msg) {
        logger.finest("MyEventListener[" + instance + "] got an " + msg.toString() + "event!");
        if (msg instanceof XL_RFSWithData) {
            XL_RFSWithData rfsd = (XL_RFSWithData) msg;
            logger.finest("RFS With Data on Port:" + rfsd.getSpan() + ":" + rfsd.getChannel() + " - " + SKJMessage.printableFormat(rfsd.getData()));
            XL_GenerateCallProcessingEvent gcpe = new XL_GenerateCallProcessingEvent();
            gcpe.setSpan(rfsd.getSpan());
            gcpe.setChannel(rfsd.getChannel());
            gcpe.setEvent(1);
            csp.sendMessage(gcpe, null);
        } else if (msg instanceof XL_PPLEventIndication) {
            XL_PPLEventIndication pei = (XL_PPLEventIndication) msg;
            logger.finest("PPL Event Indication on Component:" + pei.getComponentID() + ", Event:" + pei.getPPLEvent() + ", AIB: " + SKJMessage.printableFormat(pei.getAddrInfo()) + ", Data:" + SKJMessage.printableFormat(pei.getData()));
        } else if (msg instanceof ISDN_ReleaseComplete) {
            ISDN_ReleaseComplete rc = (ISDN_ReleaseComplete) msg;
        } else if (msg instanceof XLJ_L3PCC_Message) {
            XLJ_L3PCC_Message ccm = (XLJ_L3PCC_Message) msg;
            logger.finest("ISDN Call Control Message on Port:" + ccm.toString());
        } else if (msg instanceof XL_ChannelReleasedWithData) {
            XL_ChannelReleasedWithData crwd = (XL_ChannelReleasedWithData) msg;
            logger.finest("Channel Released With Data on Port:" + crwd.getSpan() + ":" + crwd.getChannel() + " - " + SKJMessage.printableFormat(crwd.getICBData()));
            csp.popListener();
        }
        return false;
    }
