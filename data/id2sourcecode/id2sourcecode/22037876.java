    @Override
    public void logIn(PresenceType presenceType, String verboseStatus) {
        setPendingPresenceAndStatus(presenceType, verboseStatus);
        if (!isLoggedIn()) {
            try {
                connection = new MSIMConnection();
                connection.connect();
                connection.login(getRegistration().getUsername(), getRegistration().getPassword());
                this.setLoginStatus(TransportLoginStatus.LOGGED_IN);
                listener = new MySpaceIMListener(this);
                connection.addMessageListener(listener);
                connection.getContactManager().addContactListener(listener);
                connection.getContactManager().requestContacts();
            } catch (MSIMException e) {
                Log.error("MySpace: Failure while logging in:", e);
                setFailureStatus(ConnectionFailureReason.UNKNOWN);
                sessionDisconnected("Failure while logging in.");
            }
        }
    }
