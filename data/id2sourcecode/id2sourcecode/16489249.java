    private void takeLock(FileOutputStream out) throws IOException {
        try {
            lock = out.getChannel().tryLock();
        } catch (java.nio.channels.OverlappingFileLockException e) {
        }
        if (lock == null) {
            throw new IOException("File is already being uploaded: Could not acquire exclusive lock on file");
        }
    }
