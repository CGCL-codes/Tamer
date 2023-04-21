    public void setLength(long length) throws IOException {
        FileChannel channel = fileOutput.getChannel();
        channel.truncate(length);
        fsync();
        close();
        open();
    }
