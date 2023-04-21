    public XMPPConnection getConnection() {
        if (con != null && con.isConnected()) return con;
        con = new XMPPConnection(server);
        try {
            con.connect();
            con.login(username, password);
        } catch (XMPPException e) {
            log.warn("", e);
        }
        con.getRoster().addRosterListener(new RosterListener() {

            public void presenceChanged(Presence arg0) {
                log.info("{}", arg0);
            }

            public void entriesAdded(Collection<String> arg0) {
            }

            public void entriesDeleted(Collection<String> arg0) {
            }

            public void entriesUpdated(Collection<String> arg0) {
            }
        });
        return con;
    }
