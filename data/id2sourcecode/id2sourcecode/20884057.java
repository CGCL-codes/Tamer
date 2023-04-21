    void pushSessionList() {
        for (int i = 0; i < sessions.size(); i++) {
            ChatSession s = (ChatSession) sessions.elementAt(i);
            s.dispatch(getChannelList());
        }
    }
