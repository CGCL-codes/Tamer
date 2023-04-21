    public void init() {
        try {
            RandomAccessFile f = new RandomAccessFile(file, "rw");
            FileChannel fc = f.getChannel();
            bb = fc.map(FileChannel.MapMode.READ_WRITE, 0, f.length());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
