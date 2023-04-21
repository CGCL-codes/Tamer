    public void handleEvent(final Object event) {
        if (event instanceof RemoteLoggingEvent) {
            LoggingEvent loggingEvent = ((RemoteLoggingEvent) event).getLoggingEvent();
            if (loggingEvent.getLevel().toInt() == Level.FATAL_INT) {
                writeToEventLog("Critical error in " + loggingEvent.getThreadName());
                triggerMasterAlarm();
            }
        } else if (event instanceof RemoteLogEvent) {
            LogMessage logMessage = ((RemoteLogEvent) event).getLogMessage();
            if (logMessage.level == LogMessage.CRITICAL_ERROR_LEVEL) {
                writeToEventLog("Critical error in " + logMessage.origin);
                triggerMasterAlarm();
            }
        }
        if (this.logPanelVersion1 != null) this.logPanelVersion1.handleEvent(event);
        if (this.logPanelVersion2 != null) this.logPanelVersion2.handleEvent(event);
    }
