    public ConnectionMetaHandler connect(int connectType) throws SystemException, ConnectionException, ServerDownException {
        try {
            try {
                disconnect();
                try {
                    Thread.sleep(2000);
                } catch (Exception k) {
                }
            } catch (Exception k) {
            }
            if (handler == null || !handler.getStore().isConnected()) {
                Properties props = new Properties();
                Session session = Session.getDefaultInstance(props);
                log.debug("session instance initiated");
                handler = new ConnectionMetaHandler();
                handler.setStore(session.getStore(profile.getProtocol()));
                log.debug("session store set");
                handler.getStore().connect(profile.getFetchServer(), profile.getIFetchPort(), auth.getUsername(), auth.getPassword());
                log.debug("Store has been connected... Successful");
                handler.setMbox(handler.getStore().getDefaultFolder());
                handler.setMbox(handler.getMbox().getFolder(Constants.FOLDER_INBOX(profile)));
                log.debug("Got mailbox");
                handler.getMbox().open(connectType);
                log.debug("Mailbox open");
                pop3Folders.put(auth.getUsername(), handler.getMbox());
                handler.setTotalMessagesCount(handler.getMbox().getMessageCount());
                log.debug("Message Count:" + handler.getTotalMessagesCount());
            }
        } catch (AuthenticationFailedException e) {
            log.debug("Pop3 Mailbox was busy with another session and there is a read write lock. A few minutes later when the lock is released everything will be fine.", e);
        } catch (NoSuchProviderException e) {
            log.fatal(profile.getProtocol() + " provider could not be found.");
            throw new SystemException(e);
        } catch (MessagingException e) {
            log.error("Connection could not be established.");
            throw new ConnectionException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handler;
    }
