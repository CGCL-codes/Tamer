    private void connect() throws XMPPException {
        ConnectionConfiguration conf = new ConnectionConfiguration(Settings.getInstance().getServer(), Settings.getInstance().getPort());
        xmppCon = new XMPPConnection(conf);
        try {
            xmppCon.connect();
            xmppCon.login(Settings.getInstance().getLogin(), Settings.getInstance().getPassword());
            control.getMainFrame().addStatusMessage("Connected to XMPP-Server " + xmppCon.getHost() + ":" + xmppCon.getPort());
            for (RosterEntry re : xmppCon.getRoster().getEntries()) {
                System.out.println("Name: " + re.getName() + " User: " + re.getUser());
            }
        } catch (XMPPException e) {
            xmppCon = null;
            throw e;
        }
    }
