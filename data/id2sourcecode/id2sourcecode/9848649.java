    public Object getGroupTitel(CridInfo info) {
        if (logger.isDebugEnabled()) {
            logger.debug("getGroupTitel(CridInfo) - start");
        }
        String channelName;
        channelName = Messages.getString("CridInfo.Channel") + " " + info.getCridServiceID();
        try {
            channelName = getBoxManager().getBox(info.getCridFile()).getChannelManager().getServiceName(info.getCridServiceID());
        } catch (Exception e) {
            logger.error("getGroupTitel(CridInfo)", e);
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getGroupTitel(CridInfo) - end");
        }
        return channelName;
    }
