    public synchronized void sync(Set<SyncFlags> syncFlags) throws DbIOException {
        assert (file != null);
        try {
            OSTRACE("SYNC    %s\n", this.filePath);
            boolean syncMetaData = syncFlags != null && syncFlags.contains(SyncFlags.NORMAL);
            file.getChannel().force(syncMetaData);
        } catch (IOException e) {
            throw new DbIOException(IOErrorCode.IOERR_FSYNC, e);
        }
    }
