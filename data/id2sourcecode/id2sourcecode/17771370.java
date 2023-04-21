    public static synchronized void writeLog(Logging.Level lvl, String description, String details) {
        for (int t = 0; t < logs.size(); t++) {
            ((Logging) logs.get(t)).writeLog(lvl, ThreadId.getCurrentId(), description, details);
        }
        if (lvl.toString().equals(Logging.CRITICAL.toString())) {
            System.exit(-1);
        }
    }
