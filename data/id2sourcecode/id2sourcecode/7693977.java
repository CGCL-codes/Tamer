    private void initConnection() {
        connection = new XMPPConnection(bindAddr.getHost());
        try {
            connection.connect();
            connection.login(bindAddr.getUser(), bindAddr.getPass());
        } catch (XMPPException e) {
            log.fatal(e, e);
            System.exit(4);
        }
        XMPPConnection.DEBUG_ENABLED = true;
        log.info("Jabber socket ready : " + bindAddr);
        chatmanager = connection.getChatManager();
        chatmanager.addChatListener(new ChatManagerListener() {

            private Logger log = Logger.getLogger(this.getClass());

            public void chatCreated(Chat chat, boolean b) {
                if (b) return;
                log.debug("new JABER connetion (" + chat.getThreadID() + ") local=" + b);
                ch = chat;
                System.out.println("notifyAll ");
                synchronized (flagServer) {
                    pleaseWaitServer = false;
                    flagServer.notifyAll();
                }
            }
        });
    }
