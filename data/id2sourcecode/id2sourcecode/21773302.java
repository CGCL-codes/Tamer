    public static void makeConnection() throws XMPPException {
        String user = "elena";
        String pass = "parolica";
        ConnectionConfiguration config = new ConnectionConfiguration("localhost", 5222);
        conn = new XMPPConnection(config);
        try {
            conn.connect();
            if (conn.isConnected()) {
                SASLAuthentication.supportSASLMechanism("PLAIN", 0);
                conn.login(user, pass, "");
                logger.info("I'm connected!");
            }
        } catch (XMPPException ex) {
            logger.fatal(ex);
        }
    }
