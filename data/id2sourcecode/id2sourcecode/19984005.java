    public void connect() {
        if (jabberUser == null || jabberPass == null || jabberServer == null || jabberPort == 0) {
            System.out.println("You must to specify connection settings.");
            return;
        }
        try {
            ConnectionConfiguration c = new ConnectionConfiguration(jabberServer);
            c.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
            if (isConnected()) this.close();
            jabberConnection = new XMPPConnection(c);
            jabberConnection.connect();
            jabberConnection.login(jabberUser, jabberPass);
            connectionStatus = true;
        } catch (XMPPException e) {
            System.out.println("Connect exception: " + e.toString());
        }
    }
