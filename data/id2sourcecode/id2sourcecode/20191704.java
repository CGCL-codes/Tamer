    private void processNewChannels() throws IOReactorException {
        ChannelEntry entry;
        while ((entry = this.newChannels.poll()) != null) {
            SocketChannel channel;
            SelectionKey key;
            try {
                channel = entry.getChannel();
                channel.configureBlocking(false);
                key = channel.register(this.selector, 0);
            } catch (ClosedChannelException ex) {
                SessionRequestImpl sessionRequest = entry.getSessionRequest();
                if (sessionRequest != null) {
                    sessionRequest.failed(ex);
                }
                return;
            } catch (IOException ex) {
                throw new IOReactorException("Failure registering channel " + "with the selector", ex);
            }
            IOSession session = new IOSessionImpl(key, new SessionClosedCallback() {

                public void sessionClosed(IOSession session) {
                    queueClosedSession(session);
                }
            });
            int timeout = 0;
            try {
                timeout = channel.socket().getSoTimeout();
            } catch (IOException ex) {
            }
            session.setAttribute(IOSession.ATTACHMENT_KEY, entry.getAttachment());
            session.setSocketTimeout(timeout);
            this.sessions.add(session);
            try {
                sessionCreated(key, session);
                SessionRequestImpl sessionRequest = entry.getSessionRequest();
                if (sessionRequest != null) {
                    sessionRequest.completed(session);
                }
            } catch (CancelledKeyException ex) {
                queueClosedSession(session);
                key.attach(null);
            }
        }
    }
