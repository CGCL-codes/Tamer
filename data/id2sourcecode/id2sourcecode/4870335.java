    public void onFailure(Object userContext, Throwable t) {
        logger.debug("#failure client.id:" + getChannelId(), t);
        client.asyncClose(userContext);
    }
