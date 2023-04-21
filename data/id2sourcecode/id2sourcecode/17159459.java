    public void processNotification(final ChannelNotificationInfo info) {
        final ChannelMedia media = info.getChannelMedia();
        if (media.equals(ChannelMedia.ASYNC)) {
            persistInfo(info);
        } else {
            service.execute(new Runnable() {

                public void run() {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Invoking asynchronously channel notification information [" + info + "]");
                    }
                    persistInfo(info);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Invoked asynchronously [" + info + "]");
                    }
                }
            });
        }
    }
