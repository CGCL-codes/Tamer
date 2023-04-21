    public List<Channel> getChannels() {
        if (channels == null) {
            channels = new EObjectContainmentWithInverseEList<Channel>(Channel.class, this, NetworkPackage.NETWORK__CHANNELS, NetworkPackage.CHANNEL__NETWORK);
        }
        return channels;
    }
