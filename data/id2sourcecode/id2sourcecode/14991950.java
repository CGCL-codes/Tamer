    protected final void setFile(final File file) throws DBException {
        this.file = file;
        fileIsNew = !file.exists();
        try {
            if ((!file.exists()) || file.canWrite()) {
                try {
                    raf = new RandomAccessFile(file, "rw");
                    FileChannel channel = raf.getChannel();
                    FileLock lock = channel.tryLock();
                    if (lock == null) readOnly = true;
                } catch (NonWritableChannelException e) {
                    readOnly = true;
                    raf = new RandomAccessFile(file, "r");
                    LOG.warn(e);
                }
            } else {
                readOnly = true;
                raf = new RandomAccessFile(file, "r");
            }
        } catch (IOException e) {
            LOG.warn("An exception occured while opening database file " + file.getAbsolutePath() + ": " + e.getMessage(), e);
        }
    }
