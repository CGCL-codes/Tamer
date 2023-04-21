    protected void login() {
        try {
            connection.connect();
            connection.login(username, password);
            connection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.accept_all);
            loggedIn();
        } catch (XMPPException e) {
            e.printStackTrace();
            loggedOut();
        }
    }
