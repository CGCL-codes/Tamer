    private void lockAndInit(File fileHnd, boolean autoCreate) throws SQLIOException {
        try {
            if (!fileHnd.exists()) {
                if (autoCreate) {
                    fileHnd.createNewFile();
                } else {
                    throw new SQLIOException("File not found: " + fileHnd);
                }
            }
            file = new RandomAccessFile(fileHnd, "rw");
            channel = file.getChannel();
            lock = channel.tryLock();
        } catch (IOException e) {
            throw new SQLIOException(e);
        }
    }
