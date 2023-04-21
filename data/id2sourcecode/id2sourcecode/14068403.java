    public void close() {
        synchronized (openedChannels) {
            for (LoopbackChannel channel : openedChannels) {
                if (channel.getChannelId() == this.channelId) {
                    openedChannels.remove(channel);
                    break;
                }
            }
        }
        this.input.clear();
        this.output.clear();
    }
