    public XMPPConnection connect() throws XMPPException {
        if (!this.ssl) {
            this.connection = new XMPPConnection(this.getServer());
            if (connection != null) {
                connection.connect();
                if (connection.isConnected()) {
                    connection.login(getUsername(), getPassword(), getRessource());
                    if (connection.isAuthenticated()) {
                        connection.sendPacket(presence);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return this.connectSLL(5223);
        }
        return connection;
    }
