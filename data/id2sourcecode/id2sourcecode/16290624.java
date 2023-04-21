    public final void readFrom(String srcPath) throws IOException {
        FileChannel fc = new RandomAccessFile(srcPath, "r").getChannel();
        readFrom(fc);
        fc.close();
    }
