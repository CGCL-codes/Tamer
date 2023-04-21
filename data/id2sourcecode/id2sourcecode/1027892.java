    ChannelStream createSource() throws IOException {
        return info.srcProtocol.getChannelStream(this);
    }
