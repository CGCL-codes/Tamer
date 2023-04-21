    public void Login(String userName, String password, Presence.Type presenceType) throws XMPPException {
        try {
            if (chatConnection == null) chatConnection = new XMPPConnection(config);
            if (!this.IsConnected()) chatConnection.connect();
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            presence = new Presence(presenceType);
            if (presenceType.equals(Presence.Type.available)) presence.setMode(Presence.Mode.available);
            chatConnection.login(userName, password, "Nestor");
            chatConnection.sendPacket(presence);
            roster = chatConnection.getRoster();
            for (RosterEntry entry : roster.getEntries()) {
                this.SubscribeToPresence(entry.getUser());
            }
            chatConnection.getRoster().setSubscriptionMode(SubscriptionMode.accept_all);
            chatManager = chatConnection.getChatManager();
            chatStateManager = ChatStateManager.getInstance(chatConnection);
            this.AddChatListener();
            this.AddPacketListener();
        } catch (XMPPException e) {
            chatConnection.disconnect();
            throw e;
        }
    }
