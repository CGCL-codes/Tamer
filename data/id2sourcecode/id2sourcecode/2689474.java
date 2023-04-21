    public Collection getChannels() {
        Collection channels = info.channelsList.getChannels();
        if (channelsList) {
            for (Iterator it = channels.iterator(); it.hasNext(); ) {
                final Channel channel = (Channel) it.next();
                if (!info.channelsList.contains(channel.getChannelID())) {
                    it.remove();
                }
            }
        }
        return channels;
    }
