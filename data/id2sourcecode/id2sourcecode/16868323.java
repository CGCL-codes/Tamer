    public XMPPConnection getXMPPConnection(final XMPPURI uri) throws XMPPException {
        final XMPPURI connectionUri = new XMPPURI();
        connectionUri.setHost(uri.getHost());
        connectionUri.setPort(uri.getPort());
        connectionUri.setUser(uri.getUser());
        connectionUri.setUserPassword(uri.getUserPassword());
        final XMPPConnection connection;
        if (managedConnections.containsKey(connectionUri)) {
            connection = managedConnections.get(connectionUri);
        } else {
            connection = new XMPPConnection(new ConnectionConfiguration(connectionUri.getHost(), connectionUri.getPort())) {

                @Override
                protected void shutdown(final Presence unavailablePresence) {
                    final int lease = leases.get(this);
                    if (lease > 1) {
                        leases.put(this, lease - 1);
                    } else {
                        leases.remove(this);
                        super.shutdown(unavailablePresence);
                    }
                }
            };
        }
        final int lease = leases.containsKey(connection.getConnectionID()) ? leases.get(connection.getConnectionID()) + 1 : 1;
        leases.put(connection, lease);
        if (!connection.isConnected()) {
            connection.connect();
        }
        if (!connection.isAuthenticated()) {
            if (connectionUri.getUser() != null) {
                connection.login(connectionUri.getUser(), connectionUri.getUserPassword(), "JSONRPC");
            } else {
                connection.loginAnonymously();
            }
        }
        return connection;
    }
