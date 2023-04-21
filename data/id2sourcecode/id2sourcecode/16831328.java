    public int getChannelRoute(int channel) {
        if (channel >= 0 && channel < this.channels.length) {
            int route = this.channels[channel][CHANNEL_INDEX];
            return (route >= 0 ? route : channel);
        }
        return 0;
    }
