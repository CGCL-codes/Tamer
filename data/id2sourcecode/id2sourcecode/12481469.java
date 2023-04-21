    public static synchronized void print(LogComponent component, String message, Throwable throwable) {
        int componentOrdinal = component.ordinal;
        LogLevel level = LogLevel.severe;
        if (INSTANCE.HasSyncLog[componentOrdinal]) {
            for (LogWriter writer : component.SyncWriters) {
                Calendar time = Calendar.getInstance();
                INSTANCE.WriterThread.writeLog(writer, level, time, message, throwable);
            }
        }
        if (INSTANCE.HasAsyncLog[componentOrdinal]) {
            LogRow row = new LogRow(level, message, throwable);
            for (LogWriter writer : component.AsyncWriters) {
                writer.LogRows.add(row);
            }
        }
    }
