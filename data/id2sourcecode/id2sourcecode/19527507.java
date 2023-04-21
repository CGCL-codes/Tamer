    public final void connect() throws Exception {
        try {
            super.connect();
            notifyConnectionInitiated();
            if (getUserName() == null || getPassword() == null) {
                throw new SecurityException("Login information was not available");
            }
            connection = new DaimClient();
            connection.login(getUserName(), getPassword());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to login", e);
            connection = null;
            loggedIn = false;
            notifyConnectionFailed(e.getMessage());
        }
    }
