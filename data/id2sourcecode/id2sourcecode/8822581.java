    public String getChannel() {
        if (channel == null) {
            channel = getString(Bayeux.CHANNEL_FIELD);
        }
        return channel;
    }
