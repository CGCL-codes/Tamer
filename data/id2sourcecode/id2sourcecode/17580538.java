    private static void checkTraceFile() {
        if (!traceInitialiazed) {
            traceInitialiazed = true;
            String requestTraceFile = System.getProperty("requestTraceFile");
            if (requestTraceFile == null) {
                return;
            }
            try {
                tos = new FileOutputStream(requestTraceFile, true).getChannel();
            } catch (IOException e) {
                ZooLog.logException(e);
                return;
            }
            ZooLog.logWarn("*********** Traced requests saved to " + requestTraceFile);
        }
    }
