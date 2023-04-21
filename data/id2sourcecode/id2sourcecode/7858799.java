    public XMPPConnection connectSLL(int port) throws XMPPException {
        ConnectionConfiguration con = new ConnectionConfiguration(this.getServer(), port);
        con.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
        con.setSASLAuthenticationEnabled(true);
        con.setSocketFactory(new DummySSLSocketFactory());
        this.connection = new XMPPConnection(con);
        if (this.connection != null) {
            this.connection.connect();
            if (this.connection.isConnected()) {
                this.connection.login(getUsername(), getPassword(), getRessource());
                if (this.connection.isAuthenticated()) {
                    this.connection.sendPacket(presence);
                }
            }
        }
        return this.connection;
    }
