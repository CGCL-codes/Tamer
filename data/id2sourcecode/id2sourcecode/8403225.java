    public void sessionClosed(IoSession session) throws Exception {
        log.debug("sessionClosed : " + session);
        ClientSessionImpl client = (ClientSessionImpl) session.removeAttribute(SessionAttributeKey.CLIENT_SESSION);
        if (client != null) {
            try {
                Iterator<Channel> channels = channel_manager.getChannels();
                while (channels.hasNext()) {
                    try {
                        channels.next().leave(client);
                    } catch (Throwable e) {
                        log.error(e.getMessage(), e);
                    }
                }
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
            try {
                if (client.Listener != null) {
                    client.Listener.disconnected(client);
                }
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }
