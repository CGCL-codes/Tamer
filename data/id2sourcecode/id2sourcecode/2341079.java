    private void run(File f) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        FileChannel fc = raf.getChannel();
        int sz = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_WRITE, 0, sz);
        searchAndReplace(bb, sz);
        bb.force();
        raf.close();
    }
