    public boolean containsChannel(ChannelName s) {
        for (InkChannel c : this.getChannels()) {
            if (c.getName() == s) {
                return true;
            }
        }
        return false;
    }
