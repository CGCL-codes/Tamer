    private SFTPv3Client getChannel() throws IOException {
        if (channel == null) channel = new SFTPv3Client(connection);
        return channel;
    }
