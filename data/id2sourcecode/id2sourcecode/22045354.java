    private synchronized void openFile(long logId) throws IOException {
        final File file = fileFor(logId);
        if (file.exists()) {
            if (mReplayMode) {
                mLogId = logId;
                return;
            }
            throw new FileNotFoundException("Log file already exists: " + file.getPath());
        }
        mReplayMode = false;
        final FileOutputStream oldOut = mOut;
        final FileOutputStream out = new FileOutputStream(file);
        try {
            mOut = out;
            mChannel = out.getChannel();
            writeLong(MAGIC_NUMBER);
            writeInt(ENCODING_VERSION);
            writeLong(logId);
            timestamp();
            doFlush();
        } catch (IOException e) {
            mChannel = ((mOut = oldOut) == null) ? null : oldOut.getChannel();
            try {
                out.close();
            } catch (IOException e2) {
            }
            file.delete();
            throw e;
        }
        if (oldOut != null) {
            try {
                oldOut.close();
            } catch (IOException e) {
            }
        }
        mLogId = logId;
    }
