    private String getChannelName() {
        if (logger.isDebugEnabled()) {
            logger.debug("getChannelName() - start");
        }
        String channelName;
        channelName = Messages.getString("CridInfo.Channel") + " " + info.getCridServiceID();
        try {
            channelName = getBoxManager().getBox(info.getCridFile()).getChannelManager().getServiceName(info.getCridServiceID());
        } catch (Exception e) {
            logger.error("getChannelName()", e);
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getChannelName() - end");
        }
        return channelName;
    }
