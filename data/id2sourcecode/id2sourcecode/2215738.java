    public boolean connect(String username, String password) {
        try {
            connection.connect();
            connection.login(username, password);
            connection.addPacketListener(pl, pf);
            connection.getRoster().createGroup(UBIETY_GROUP);
            connection.getRoster().setSubscriptionMode(SubscriptionMode.manual);
            updatePresence();
            return true;
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        return false;
    }
