    public final void logMessage(LogRecord lr) {
        if (!enabled) {
            new IllegalStateException("logger not enabled for " + lr.target.getClass().getName() + "@" + Integer.toHexString(lr.target.hashCode()) + ", logging the message anyway").printStackTrace();
        }
        if (lr == null) {
            write(new LogRecord(null, Thread.currentThread(), this, LOG_ALERT, LogAware.CH_LOGGER, "null log record was supplied, you may want to check what's going on", new IllegalArgumentException("Trace this to see where the null record originated from")));
            return;
        }
        if (filter == null || filter.isEnabled(lr)) {
            write(lr);
        }
    }
