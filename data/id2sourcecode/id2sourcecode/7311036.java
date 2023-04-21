    public Channel removeChannel(String channelId) {
        if (channelId == null) return null;
        TimeChannel timeChannel = channels.get(channelId);
        Channel channel = null;
        if (timeChannel != null) {
            try {
                if (timeChannel.getTimerTask() != null) timeChannel.getTimerTask().cancel();
            } catch (Exception e) {
            }
            channel = timeChannel.getChannel();
            try {
                for (Subscription subscription : channel.getSubscriptions()) {
                    try {
                        Message message = subscription.getUnsubscribeMessage();
                        handleMessage(message, true);
                    } catch (Exception e) {
                        log.error(e, "Error while unsubscribing channel: %s from subscription: %s", channel, subscription);
                    }
                }
            } finally {
                try {
                    channel.destroy();
                } finally {
                    channels.remove(channelId);
                }
            }
        }
        return channel;
    }
