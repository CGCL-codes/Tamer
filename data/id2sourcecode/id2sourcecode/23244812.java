    public void connect(ConnectionConfiguration connectionConfiguration, String username, String password, boolean failSilently) throws Exception {
        if (isConnected()) {
            disconnect();
        }
        prevConnectionConfiguration = connectionConfiguration;
        loginUsername = username;
        loginPassword = password;
        try {
            this.setConnectionState(ConnectionState.CONNECTING, null);
            this.connection = new XMPPConnection(connectionConfiguration);
            this.connection.connect();
            if (this.smackConnectionListener == null) {
                this.smackConnectionListener = new SafeConnectionListener(log, new XMPPConnectionListener());
            }
            connection.addConnectionListener(this.smackConnectionListener);
            Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
            this.connection.login(username, password, Saros.RESOURCE);
            this.myJID = new JID(this.connection.getUser());
            setConnectionState(ConnectionState.CONNECTED, null);
        } catch (IllegalArgumentException e) {
            setConnectionState(ConnectionState.ERROR, null);
            throw (e);
        } catch (XMPPException e) {
            Throwable t = e.getWrappedThrowable();
            Exception cause = (t != null) ? (Exception) t : e;
            setConnectionState(ConnectionState.ERROR, cause);
            throw (e);
        } catch (Exception e) {
            setConnectionState(ConnectionState.ERROR, e);
            throw (e);
        }
    }
