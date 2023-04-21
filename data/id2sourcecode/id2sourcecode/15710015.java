    AsteriskChannelImpl getChannelImplByNameAndActive(String name) {
        AsteriskChannelImpl channel = null;
        if (name == null) {
            return null;
        }
        synchronized (channels) {
            for (AsteriskChannelImpl tmp : channels) {
                if (tmp.getName() != null && tmp.getName().equals(name) && tmp.getState() != ChannelState.HUNGUP) {
                    channel = tmp;
                }
            }
        }
        return channel;
    }
