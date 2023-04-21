    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(LockExam1.PATH, "rw");
        FileChannel ch = raf.getChannel();
        System.err.println("Trying to lock from RandomAccessFile");
        MappedByteBuffer mbuf = ch.map(MapMode.READ_WRITE, 0, ch.size());
        mbuf.put(0, mbuf.get(0));
        mbuf.force();
        System.err.println("Mapped : " + mbuf);
        LockExam1.pause();
        raf.close();
    }
