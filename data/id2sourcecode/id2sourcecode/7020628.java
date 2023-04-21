    public void sawsStart() {
        if (debugLevel > SAWSConstant.VerboseInfo && logFileList.size() != 0) {
            for (int i = logFileList.size() - 1; i >= 0; --i) {
                String logFilename = (String) logFileList.get(i);
                sawsDebugLog.write("Log file name in SAWSServer start: " + logFilename);
            }
        }
        if (this.closed) {
            this.showMessage("SAWS log file is already closed. SAWS have to be initialized again.", SAWSTextOutputCallback.WARNING);
            if (debugLevel > SAWSConstant.NoInfo) {
                sawsDebugLog.write("SAWS log file is already closed when trying to start SAWS.");
            }
        }
        String[] options = { "Yes, continue", "No, stop SAWS" };
        int selection = this.createConfirmCallback("SAWS has finished its initilisation process. " + "\nNow SAWS can start to record client log records. Do you want to continue? \n", options, SAWSChoiceCallback.WARNING, "StartRecordingLogs");
        if (selection == 1) {
            closeLog();
            System.exit(-1);
        }
        thread = new WritingThread();
        thread.start();
        currentTime = System.currentTimeMillis();
        if (heartbeatInterval != 0) {
            setHeartbeatWriter(heartbeatInterval);
        }
    }
