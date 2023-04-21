    public void truncateLog(long fileNum, long offset) throws IOException, DatabaseException {
        FileHandle handle = makeFileHandle(fileNum, FileMode.READWRITE_MODE);
        RandomAccessFile file = handle.getFile();
        try {
            file.getChannel().truncate(offset);
        } finally {
            file.close();
        }
        if (handle.isOldHeaderVersion()) {
            forceNewFile = true;
        }
    }
