    public List<RhnConfigChannel> getChannels() throws RhnChannelNotFoundException {
        if (this.channels_.size() == 0) {
            throw new RhnChannelNotFoundException("No config channels found in server " + this.connection_.getServer());
        } else {
            return this.channels_;
        }
    }
