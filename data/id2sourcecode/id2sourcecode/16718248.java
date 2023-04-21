    public void start(JID jid, String password) throws IOException {
        try {
            this.conn = new XMPPConnection(this.config);
            this.conn.connect();
            this.conn.login(jid.toString(), password);
            System.out.println("Logged in as " + jid.toString());
            Presence presence = new Presence(Presence.Type.available);
            this.conn.sendPacket(presence);
            this.conn.getChatManager().addChatListener(new ChatManagerListener() {

                public void chatCreated(Chat chat, boolean locally) {
                    String peer = chat.getParticipant();
                    if (!PJBService.this.handler.isAllowed(peer)) {
                        String reply = PJBService.this.handler.getDenialMessage();
                        XmppUtils.sendSilently(reply, chat);
                    } else {
                        MessageListener ml = new MessageListener() {

                            public void processMessage(Chat chat, Message message) {
                                try {
                                    String peer = chat.getParticipant();
                                    String reply = PJBService.this.handler.onMessage(message.getBody(), peer);
                                    XmppUtils.sendSilently(reply, chat);
                                } catch (Exception e) {
                                    System.err.println("Error: " + e.getMessage());
                                }
                                chat.removeMessageListener(this);
                            }
                        };
                        chat.addMessageListener(ml);
                    }
                }
            });
        } catch (XMPPException e) {
            throw new IOException(e);
        }
    }
