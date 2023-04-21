    private void createVersionFile(File versionFile) throws IOException {
        FileChannel channel = new FileOutputStream(versionFile).getChannel();
        ByteBuffer buffer = ByteBuffer.wrap(VERSION_PREFIX.getBytes());
        channelHelper.writeRemaining(channel, buffer);
        buffer = ByteBuffer.allocate(8);
        buffer.putLong(VERSION.toLong()).flip();
        channelHelper.writeRemaining(channel, buffer);
        channel.close();
    }
