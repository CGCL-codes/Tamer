    public final String[] getChannels() {
        String[] channels = new String[0];
        synchronized (_channels) {
            channels = new String[_channels.size()];
            Enumeration enumeration = _channels.keys();
            for (int i = 0; i < channels.length; i++) {
                channels[i] = (String) enumeration.nextElement();
            }
        }
        return channels;
    }
