    public static boolean isFileLocked(String database) {
        File file = new File(database + FILE_EXT);
        try {
            FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
            FileLock lock = channel.tryLock();
            if (lock != null) {
                lock.release();
                return false;
            }
        } catch (FileNotFoundException e) {
            log.fine("database file: " + database + FILE_EXT + " was not found");
        } catch (IOException e) {
            return true;
        }
        return true;
    }
