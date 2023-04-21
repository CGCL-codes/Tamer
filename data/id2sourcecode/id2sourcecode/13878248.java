    public List getChannelList() {
        List list = null;
        try {
            list = channelManager.getChannelList(false);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return list;
    }
