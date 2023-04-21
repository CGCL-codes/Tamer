    public boolean deleteUser(String server, String jid, String password) {
        XMPPConnection conn = new XMPPConnection(server);
        try {
            conn.connect();
            conn.login(jid, password);
            AccountManager aMgr = new AccountManager(conn);
            aMgr.deleteAccount();
            conn.disconnect();
        } catch (XMPPException e2) {
            return false;
        } finally {
            conn.disconnect();
        }
        return true;
    }
