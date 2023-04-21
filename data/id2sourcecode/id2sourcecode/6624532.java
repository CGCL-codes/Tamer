    public ReadableByteChannel openChannel(ChannelConfig config) throws IOException {
        File file = config.getFile();
        FileInputStream inputStream = new FileInputStream(file);
        long length = file.length();
        final long skip = length;
        if (0 < skip) {
            inputStream.skip(skip);
        }
        return inputStream.getChannel();
    }
