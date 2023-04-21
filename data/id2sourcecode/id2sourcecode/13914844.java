    public boolean login(String user, String pass) {
        if (!connection.isConnected()) {
            System.err.print("Connecting to XMPP Server... ");
            try {
                connection.connect();
                System.err.println("Success!");
            } catch (XMPPException e) {
                System.err.println("FAIL: " + e.getMessage());
                return false;
            }
        }
        if (!connection.isAuthenticated()) {
            System.err.print("Logging into XMPP Server... ");
            try {
                connection.login(user, pass);
                System.err.println("Success!");
                this.user = user;
                this.pass = pass;
            } catch (XMPPException e) {
                System.err.println("FAIL: " + e.getMessage());
                return false;
            }
        }
        return true;
    }
