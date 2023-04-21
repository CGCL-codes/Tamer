    private static void patch(File f, byte[] sought, byte[] replacement) throws IOException {
        RandomAccessFile raf = null;
        FileChannel fc = null;
        try {
            raf = new RandomAccessFile(f, "rw");
            fc = raf.getChannel();
            int sz = (int) fc.size();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_WRITE, 0, sz);
            searchAndReplace(bb, sought, replacement, sz);
            bb.force();
        } finally {
            StreamHelper.close(fc);
            StreamHelper.close(raf);
        }
    }
