    private boolean addSession(SessionInfo session) {
        Long session_id = new Long(session.getSessionId());
        if (sessionList.get(session_id) == null) {
            sessionList.put(session_id, session);
            return true;
        } else {
            writeErrorMessage(session_id + " already exists", null);
            return false;
        }
    }
