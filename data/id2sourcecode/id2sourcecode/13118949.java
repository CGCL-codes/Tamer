    public static void check_blue_server() {
        FileLock lock = null;
        File file = new File(lock_file);
        if (!file.exists()) {
            cgiutils.blue_process_state = blue_h.STATE_UNKNOWN;
            return;
        }
        try {
            FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
            lock = channel.tryLock();
            if (lock == null || lock.overlaps(0, Long.MAX_VALUE)) {
                cgiutils.blue_process_state = blue_h.STATE_OK;
            } else {
                lock.release();
                channel.close();
                cgiutils.blue_process_state = blue_h.STATE_UNKNOWN;
            }
        } catch (IOException e) {
            cgiutils.blue_process_state = blue_h.STATE_UNKNOWN;
        } catch (OverlappingFileLockException e) {
            cgiutils.blue_process_state = blue_h.STATE_OK;
        }
    }
