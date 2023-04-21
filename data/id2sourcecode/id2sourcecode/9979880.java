    private void init(String aLogFileName) throws ODMGException {
        mLogFileName = aLogFileName;
        try {
            sLogger.fine("Opening ArchivingRedoLogServer on log file " + mLogFileName);
            mRandomAccessLogFile = new BufferedRandomAccessLogFile(mLogFileName, "rw");
            if (mRandomAccessLogFile.getChannel().tryLock() == null) {
                throw new DatabaseOpenException("Log file " + mLogFileName + " is in use by another process.");
            }
        } catch (IOException e) {
            throw new ODMGException("Unable to open log file: " + mLogFileName + ": " + e, e);
        }
    }
