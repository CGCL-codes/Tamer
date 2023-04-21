    protected void acquireTileStoreLock() throws TileStoreException {
        try {
            File file = new File(tileStoreDir, "lock");
            FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
            tileStoreLock = channel.tryLock();
            if (tileStoreLock == null) throw new TileStoreException("Unable to obtain tile store lock - " + "another instance of TrekBuddy Atlas Creator is running!");
        } catch (Exception e) {
            log.error("", e);
            throw new TileStoreException(e.getMessage(), e.getCause());
        }
    }
