    public void truncateLog(long fileNum, long offset) throws IOException, DatabaseException {
        try {
            FileHandle handle = makeFileHandle(fileNum, getAppropriateReadWriteMode());
            RandomAccessFile file = handle.getFile();
            try {
                file.getChannel().truncate(offset);
            } finally {
                file.close();
            }
            if (handle.isOldHeaderVersion()) {
                forceNewFile = true;
            }
        } catch (ChecksumException e) {
            throw new EnvironmentFailureException(envImpl, EnvironmentFailureReason.LOG_CHECKSUM, e);
        }
    }
