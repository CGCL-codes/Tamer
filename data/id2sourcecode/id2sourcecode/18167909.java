    protected ByteBuffer getByteBuffer(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel channel = in.getChannel();
        MappedByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
        bbuf.load();
        return bbuf;
    }
