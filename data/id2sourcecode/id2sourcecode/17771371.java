    public static synchronized void writeLog(Logging.Level lvl, String description, Throwable throwable) {
        for (int t = 0; t < logs.size(); t++) {
            ((Logging) logs.get(t)).writeLog(lvl, ThreadId.getCurrentId(), description, throwable);
        }
    }
