    public ReadableByteChannel getChannel() throws IOException {
        return Channels.newChannel(getContent());
    }
