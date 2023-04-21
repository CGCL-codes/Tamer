    public Channel getChannelByName(String name) {
        Channel result = null;
        try {
            result = channelManager.getChannel(name);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return result;
    }
