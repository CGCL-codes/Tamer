    private void removeSession(final SocketSession session) {
        if (!mOldSessions.contains(session)) {
            mOldSessions.add(session);
            mChannels.get("info").send(session.getName() + " closed session.");
            for (String channelName : session.getChannels()) {
                mChannels.get(channelName).remove(session);
            }
        }
    }
