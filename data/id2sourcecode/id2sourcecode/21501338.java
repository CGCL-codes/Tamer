    private void process(Set<SelectionKey> keys) {
        Iterator<SelectionKey> it = keys.iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            it.remove();
            try {
                process(key);
            } catch (Exception e) {
                logger.error("Failed in process key!", e);
                Session session = (Session) key.attachment();
                if (session != null) {
                    close(session.getChannel());
                    fireSessionClosed(session);
                }
            }
        }
    }
