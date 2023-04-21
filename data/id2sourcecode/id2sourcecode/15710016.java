    AsteriskChannelImpl getChannelImplById(String id) {
        if (id == null) {
            return null;
        }
        synchronized (channels) {
            for (AsteriskChannelImpl channel : channels) {
                if (id.equals(channel.getId())) {
                    return channel;
                }
            }
        }
        return null;
    }
