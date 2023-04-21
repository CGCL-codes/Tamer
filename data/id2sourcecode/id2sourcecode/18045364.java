    private FileLock lockStage2(boolean shared, long timeout, TimeUnit timeUnit) throws IOException {
        if (shared) this.file.createNewFile();
        RandomAccessFile rf = new RandomAccessFile(this.file, shared ? "r" : "rw");
        return Files.createLock(rf, this.file, rf.getChannel(), shared, timeout, timeUnit);
    }
