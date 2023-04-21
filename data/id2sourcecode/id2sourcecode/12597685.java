    public void removeAllLoggerSessions() {
        synchronized (LOGGER_SESSIONS) {
            final Collection<LoggerSession> loggerSessions = new HashSet<LoggerSession>(getLoggerSessions());
            for (final LoggerSession session : loggerSessions) {
                removeLoggerSession(session.getChannelGroup().getLabel());
            }
        }
    }
