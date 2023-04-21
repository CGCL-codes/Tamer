    public FileInfo(File lf) throws IOException {
        fc = new RandomAccessFile(lf, "rws").getChannel();
        size = fc.size();
        if (size == 0) {
            fc.write(ByteBuffer.wrap(header));
        }
    }
