    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream(PATH);
        FileChannel ch = fos.getChannel();
        System.err.println("Trying to lock from FileOutputStream");
        FileLock lock = ch.lock();
        System.err.println("Got a lock");
        pause();
        fos.close();
    }
