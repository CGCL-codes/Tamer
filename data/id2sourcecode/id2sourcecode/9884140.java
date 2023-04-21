    private void doCloseChannel(CloseChannel cc) {
        try {
            ChannelManager cman = AppContext.getChannelManager();
            DataManager dman = AppContext.getDataManager();
            String boundName = Constants.CHANNEL_DATA + cc.channelName;
            try {
                ChannelData data = dman.getBinding(boundName, ChannelData.class);
                dman.removeBinding(boundName);
                dman.removeObject(data);
                logger.info(String.format("instance of %s released from '%s'", data.getClass().getName(), boundName));
                Channel c = cman.getChannel(cc.channelName);
                logger.info("closing channel: " + c.getName());
                c.close();
            } catch (NameNotBoundException ex) {
            }
            send(new ResultResponse(Result.OK));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }
