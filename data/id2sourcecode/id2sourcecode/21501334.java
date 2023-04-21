    private void doWrite() {
        for (int i = 0; i < 50; i++) {
            KeyValuePair<Session, Object> pair = outing.poll();
            if (pair == null) {
                break;
            } else {
                Session session = pair.getKey();
                Object msg = pair.getValue();
                try {
                    doWrite(session, msg);
                } catch (Exception e) {
                    logger.error("Failed in write object!", e);
                    close(session.getChannel());
                    fireSessionClosed(session);
                }
            }
        }
    }
