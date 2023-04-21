    private void checkConnection() throws XMPPException {
        if (!conn.isConnected()) {
            conn.connect();
            conn.addPacketListener(packetListener, null);
        }
        if (!conn.isAuthenticated()) {
            conn.login(login + "@gmail.com", password, JABBER_CLIENT);
            roster = conn.getRoster();
            roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
            {
                Presence presence = new Presence(Presence.Type.available);
                presence.setStatus("Hello! I am here! Exporting ICQ Users...");
                conn.sendPacket(presence);
            }
        }
    }
