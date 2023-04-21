    public static InputStream createInputStreamFromFile(File file, Range range) throws IOException {
        FileChannel fastaFileChannel = new FileInputStream(file).getChannel();
        ByteBuffer buf = ByteBuffer.allocate((int) range.size());
        fastaFileChannel.position((int) range.getStart());
        fastaFileChannel.read(buf);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(buf.array());
        return inputStream;
    }
