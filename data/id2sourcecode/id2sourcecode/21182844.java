    private void getFile() throws LowLevelStorageException {
        try {
            random = new RandomAccessFile(file, "rw");
            channel = random.getChannel();
        } catch (IOException x) {
            throw new LowLevelStorageException("Could not open lock", x);
        }
    }
