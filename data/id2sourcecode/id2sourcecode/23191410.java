    public static FileChannel createFileChannelForWriting(File file) throws IOException {
        createNewFile(file);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        return raf.getChannel();
    }
