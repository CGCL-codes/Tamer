    public void connect() throws SecurityException, Exception {
        loggedIn = false;
        log.setLevel(Level.ALL);
        super.connect();
        sessions.clear();
        notifyConnectionInitiated();
        String username = getUserName();
        if (username.indexOf('@') == -1) {
            throw new SecurityException("MSN usernames must contain full domain");
        }
        connection = MsnMessengerFactory.createMsnMessenger(username, getPassword());
        connection.setLogIncoming(false);
        connection.setLogOutgoing(false);
        initMessenger(connection);
        connection.login();
    }
