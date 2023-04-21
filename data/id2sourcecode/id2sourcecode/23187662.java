    private static Duple<DoubleBuffer, File> createTempBuffer(int size) {
        try {
            File f = File.createTempFile("OnDiskMatrix", ".matrix");
            f.deleteOnExit();
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            FileChannel fc = raf.getChannel();
            DoubleBuffer contextBuffer = fc.map(MapMode.READ_WRITE, 0, size).asDoubleBuffer();
            fc.close();
            return new Duple<DoubleBuffer, File>(contextBuffer, f);
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }
