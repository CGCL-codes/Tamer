    private FileChannel getChannel() throws FileNotFoundException {
        if (channel == null) {
            channel = getRaf().getChannel();
        }
        return channel;
    }
