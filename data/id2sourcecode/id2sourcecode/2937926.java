    public LogManager() {
        loggersMap = new HashMap<String, ILogger>();
        loggersByLevelsMap = new HashMap<LogLevel, List<ILogger>>();
        for (LogLevel logLevel : LogLevel.values()) {
            loggersByLevelsMap.put(logLevel, new ArrayList<ILogger>());
        }
        queueLogs = new ArrayDeque<LogEntry>();
        runnableLoop = new RunnableLoop();
        writerThread = new Thread(runnableLoop);
        writerThread.setName("LogManager Writer Thread");
    }
