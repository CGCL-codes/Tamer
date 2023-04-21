    public java.nio.ByteBuffer getNioByteBuffer() throws IOException {
        if (filename != null) {
            FileChannel channel;
            if (plainRandomAccess) channel = trf.getChannel(); else channel = rf.getChannel();
            return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        }
        return java.nio.ByteBuffer.wrap(arrayIn);
    }
