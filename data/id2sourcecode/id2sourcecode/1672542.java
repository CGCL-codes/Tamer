    public ByteChannel getChannel() throws IOException {
        if (channel == null) {
            channel = new StreamingByteChannel(inStream, outStream);
        }
        return channel;
    }
