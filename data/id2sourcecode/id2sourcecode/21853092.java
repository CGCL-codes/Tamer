    protected void acquireTileStoreLock() throws TileStoreException {
        try {
            File file = new File(tileStoreDir, "lock");
            if (!tileStoreDir.isDirectory()) try {
                Utilities.mkDirs(tileStoreDir);
            } catch (IOException e) {
                throw new TileStoreException("Unable to create tile store directory: \"" + tileStoreDir.getPath() + "\"");
            }
            FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
            tileStoreLock = channel.tryLock();
            if (tileStoreLock == null) throw new TileStoreException("Unable to obtain tile store lock - " + "another instance of TrekBuddy Atlas Creator is running!");
        } catch (Exception e) {
            log.error("", e);
            throw new TileStoreException(e.getMessage(), e.getCause());
        }
    }
