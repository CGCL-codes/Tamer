    public BufferedFileChannel(final File file, final ByteBuffer buffer) throws IOException {
        f = file;
        name = file.getAbsolutePath();
        fc = new RandomAccessFile(file, "r").getChannel();
        mark = 0;
        buffer.clear();
        buffer.position(buffer.limit());
        rem = fc.size();
        buf = buffer;
        parent = null;
    }
