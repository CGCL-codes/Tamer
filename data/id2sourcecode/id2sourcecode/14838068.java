    public void removeChannel(int channel, boolean lazy) {
        if (!lazy) {
            channels.remove(channel);
        } else if (channel < getChannelCount() - 1) {
            channels.add(channels.remove(channel));
        }
        channelCount--;
    }
