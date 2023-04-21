    public Channel getChannel(int id) {
        Channel result = null;
        try {
            result = channelManager.getChannel(id);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return result;
    }
