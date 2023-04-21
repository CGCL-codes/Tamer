    public FileChannel getFileChannel() throws IOException {
        FileInputStream fis = new FileInputStream(getFile());
        tmp.addResource(fis);
        FileChannel channel = fis.getChannel();
        tmp.addResource(channel);
        return channel;
    }
