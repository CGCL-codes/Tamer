    public void registerSession(SessionContext session) {
        synchronized (this.sessions) {
            this.sessions.put(session.getChannel().getId(), session);
        }
    }
