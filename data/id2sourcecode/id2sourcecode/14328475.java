    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(LockExam1.PATH, "rw");
        FileChannel ch = raf.getChannel();
        System.err.println("Trying to lock from FileOutputStream");
        FileLock lock = ch.lock(0, 0, false);
        System.err.println("Got a lock");
        LockExam1.pause();
        raf.close();
    }
