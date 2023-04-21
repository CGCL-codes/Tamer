    public static Channels getChannels() {
        if (m_channels == null) {
            m_channels = Channels.getInstance();
        }
        return m_channels;
    }
