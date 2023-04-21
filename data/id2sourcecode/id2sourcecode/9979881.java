    private void flush() throws ODMGException {
        synchronized (mRandomAccessLogFile) {
            try {
                mRandomAccessLogFile.flush();
                mRandomAccessLogFile.getChannel().force(false);
            } catch (IOException e) {
                throw new ODMGException("Error flushing log to disk: " + e, e);
            }
        }
    }
