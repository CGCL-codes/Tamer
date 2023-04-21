    public Channel createChannel(ChannelFilter filter) {
        for (int c = 0; c < getChannelCount(); c++) {
            if (filter.accept(c)) {
                while (channels.size() <= c) {
                    channels.add(null);
                }
                if (channels.get(c) == null) {
                    return new ChannelImpl(c);
                }
            }
        }
        return null;
    }
