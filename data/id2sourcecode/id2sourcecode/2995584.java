    private void traceLock(Session session, boolean exclusive, String s) {
        if (traceLock.isDebugEnabled()) {
            traceLock.debug(session.getId() + " " + (exclusive ? "exclusive write lock" : "shared read lock") + " " + s + " " + getName());
        }
    }
