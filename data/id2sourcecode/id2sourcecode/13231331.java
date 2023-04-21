    private WritableByteChannel getWriteChannel(String path) throws IOException {
        WritableByteChannel channel;
        File f = new File(path);
        if (!f.exists() && !f.createNewFile()) {
            throw new IOException("Cannot create file " + f);
        }
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        channel = raf.getChannel();
        return channel;
    }
