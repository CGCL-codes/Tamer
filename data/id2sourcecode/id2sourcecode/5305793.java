    private void respawn(XmppAgent agent) {
        XMPPConnection conn = connections.get(agent.getJID());
        if (conn != null && conn.isConnected()) {
            XmppUtils.closeQuietly(conn);
        }
        ConnectionConfiguration cc = XmppUtils.getConnectionCfg(agent.getJID(), agent.getHost(), agent.getPort(), agent.isSecure());
        conn = null;
        try {
            final JID jid = agent.getJID();
            final int uid = agent.getUid();
            conn = new XMPPConnection(cc);
            conn.connect();
            conn.login(jid.toString(), agent.getPassword());
            Presence presence = new Presence(Presence.Type.available);
            conn.sendPacket(presence);
            conn.getRoster().setSubscriptionMode(SubscriptionMode.accept_all);
            conn.getChatManager().addChatListener(new ChatManagerListener() {

                @Override
                public void chatCreated(Chat chat, boolean locally) {
                    MessageListener ml = new MessageListener() {

                        @Override
                        public void processMessage(Chat chat, Message message) {
                            try {
                                String peer = chat.getParticipant();
                                JID from = new JID(peer);
                                String reply = handler.handle(uid, from, jid, message.getBody());
                                if (reply != null) {
                                    XmppUtils.sendSilently(reply, chat);
                                }
                            } catch (Exception e) {
                                log.warn(e.getMessage(), e);
                            }
                        }
                    };
                    chat.addMessageListener(ml);
                }
            });
            this.connections.put(jid, conn);
            log.info("XMPP connection respawned: " + jid);
        } catch (Exception e) {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
