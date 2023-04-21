    private void writeLogRecord(byte[] logRecord) {
        try {
            rafCurrentLogFile.write(logRecord);
            if (debugLevel > SAWSConstant.NoInfo) {
                sawsDebugLog.write("Log file written. \nFile name: " + this.CurrentLogFile.getAbsolutePath() + "\nChannel size: " + rafCurrentLogFile.getChannel().size());
            }
        } catch (Exception e) {
            if (debugLevel > SAWSConstant.NoInfo) {
                sawsDebugLog.write(e.toString());
            }
        }
        long currentTime = System.currentTimeMillis();
    }
