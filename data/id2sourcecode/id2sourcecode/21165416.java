    public ChannelSftp getChannel() throws IOException {
        if (m_channel == null) {
            connect();
        }
        return m_channel;
    }
