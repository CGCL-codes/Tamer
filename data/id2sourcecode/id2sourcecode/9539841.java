    private static void j_lock() {
        do {
            LOCK_FILES_DIR.mkdirs();
            try {
                RandomAccessFile raf = new RandomAccessFile(GLOBAL_LOCK_FILE, "rw");
                FileChannel channel = raf.getChannel();
                FileLock lock = channel.lock();
                globalFileChannel = channel;
                globalFileLock = lock;
                break;
            } catch (Throwable t) {
                ;
            }
        } while (true);
    }
