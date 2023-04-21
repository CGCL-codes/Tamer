    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream(LockExam1.PATH, true);
        FileChannel ch = fos.getChannel();
        ch.lock();
        LockExam1.pause();
        fos.close();
    }
