    public Cell(final Rectangle cellBunds, final IGridStructure parent) {
        IDManager.setNewID(this);
        this.bounds = cellBunds;
        logger.info("Celda " + id + " -> x= " + bounds.getX() + " y= " + bounds.getY());
        if (parent != null) {
            refStructure = AppContext.getDataManager().createReference(parent);
        }
        ChannelManager channelMgr = AppContext.getChannelManager();
        String channelName = ChannelNameParser.MOVE_CHANNEL_IDENTIFIER + '_' + id;
        Channel channel = channelMgr.createChannel(channelName, new ChannelMessageListener(), Delivery.RELIABLE);
        this.setChannel(channel);
    }
