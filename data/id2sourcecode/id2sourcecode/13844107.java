    public boolean getChannel(final int channel) {
        if (channel < 0 || channel >= CHANNEL_NAMES.length) throw new IndexOutOfBoundsException("Invalid lighting channel " + channel);
        return channels.get(channel);
    }
