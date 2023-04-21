    public long getChannelPosition(Block b, BlockWriteStreams streams) throws IOException {
        FileOutputStream file = (FileOutputStream) streams.dataOut;
        return file.getChannel().position();
    }
