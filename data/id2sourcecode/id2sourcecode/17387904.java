    public ByteChannel getChannel() throws IOException {
        if (channel == null) {
            channel = new StreamingByteChannel(socket);
        }
        return channel;
    }
