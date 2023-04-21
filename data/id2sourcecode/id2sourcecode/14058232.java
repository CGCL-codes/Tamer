    public ChannelBuffer getChannelBuffer() throws IOException {
        if (file == null) {
            return ChannelBuffers.EMPTY_BUFFER;
        }
        byte[] array = readFrom(file);
        return ChannelBuffers.wrappedBuffer(array);
    }
