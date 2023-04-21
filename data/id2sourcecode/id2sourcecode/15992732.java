    public static boolean login(XMPPConnection conn, String username, String password) {
        try {
            conn.connect();
            conn.login(username, password);
        } catch (XMPPException e) {
            logger.severe("could not connect to XMPPServer");
            e.printStackTrace();
            return false;
        }
        return true;
    }
