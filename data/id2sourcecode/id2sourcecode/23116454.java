    public void connect() throws Exception {
        try {
            super.connect();
            sessions.clear();
            notifyConnectionInitiated();
            String username = getUserName();
            if (username.indexOf('@') == -1) throw new SecurityException("MSN usernames must contain '@msn.com' or '@hotmail.com'");
            connection = new MSNMessenger("", "");
            connection.setInitialStatus(UserStatus.ONLINE);
            connection.addMsnListener(new ConnectionListener());
            connection.setInitialStatus(UserStatus.ONLINE);
            connection.login(getUserName(), getPassword());
        } catch (Exception e) {
            notifyConnectionFailed(e.getMessage());
            throw e;
        }
    }
