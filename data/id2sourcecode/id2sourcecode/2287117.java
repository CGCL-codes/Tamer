    protected Channel getChannel() {
        if (channel == null) {
            String channelId = context.getAMFContext().getChannelId();
            if (channelId != null) {
                channel = context.getServicesConfig().findChannelById(channelId);
                if (channel == null) log.warn("Could not get channel for channel id: %s", channelId);
            } else if (warnOnChannelIdMissing) log.warn("Could not get channel id for message: %s", context.getAMFContext().getRequest());
        }
        return channel;
    }
