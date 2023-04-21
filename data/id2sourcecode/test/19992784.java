    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream(LockExam1.PATH);
        FileChannel ch = fis.getChannel();
        System.err.println("Trying to lock from FileInputStream");
        FileLock lock = ch.lock();
        System.err.println("Success on lock : " + lock);
        fis.close();
    }
