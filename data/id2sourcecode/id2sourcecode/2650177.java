    @Override
    protected FileRegion createMessage(byte[] data) throws IOException {
        File file = File.createTempFile("mina", "unittest");
        file.deleteOnExit();
        FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        channel.write(buffer);
        return new DefaultFileRegion(channel);
    }
